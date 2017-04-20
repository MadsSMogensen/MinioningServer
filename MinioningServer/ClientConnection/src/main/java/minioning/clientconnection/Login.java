package minioning.clientconnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import static minioning.common.data.Events.*;
import static minioning.common.data.Lists.getConnectedUsers;
import static minioning.common.data.Lists.getPlayingUsers;
import static minioning.common.data.Lists.getPortList;
import minioning.common.services.IConnectionService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mogensen
 */
@ServiceProvider(service = IConnectionService.class)
public class Login implements IConnectionService {

    private static Path file;

    @Override
    public synchronized void process(ConcurrentHashMap<UUID, Event> eventBus, ConcurrentHashMap<UUID, Entity> world) {
        if (file == null) {
            file = Paths.get("testMinioningFileCreation.txt");
            if (!file.toFile().isFile()) {
                try {
                    file.toFile().createNewFile();
                    System.out.println("file created");
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        }
        for (Map.Entry<UUID, Event> entry : eventBus.entrySet()) {
            UUID key = entry.getKey();
            Event value = entry.getValue();
            if (value.getType().equals(CREATEACCOUNT)) {
                createLogin(value);
                eventBus.remove(key);
            }
            if (value.getType().equals(LOGIN)) {
                login(value, eventBus);
                eventBus.remove(key);
            }
            if (value.getType().equals(PLAY)) {
                play(value);
                eventBus.remove(key);
            }
        }
    }

    private void createLogin(Event value) {
        //[0]IPAddress
        //[1]port
        //[2]
        //[3]eventType
        //[4]attemptUserName
        //[5]attemptPassword
        String[] data = value.getData();
        String userName = data[4];
        System.out.println("user: " + userName);
        String password = data[5];
        System.out.println("pass: " + password);
        try {
            Writer output;
            output = new BufferedWriter(new FileWriter(file.toString(), true));
            output.append(userName.trim() + ";" + password.trim() + "\n");
            output.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void login(Event value, ConcurrentHashMap<UUID, Event> eventBus) {
        //[0]IPAddress
        //[1]port
        //[2]
        //[3]eventType
        //[4]attemptUserName
        //[5]attemptPassword
        String attemptUserName = value.getData()[4];
        String attemptPassword = value.getData()[5];

        Map<String, String> accounts = getAccounts();
        boolean found = false;
        for (Map.Entry<String, String> accountEntry : accounts.entrySet()) {
            String username = accountEntry.getKey();
            String password = accountEntry.getValue();
            if (username.equals(attemptUserName)) {
                //Username found
                System.out.println("username matching");
                if (password.trim().equals(attemptPassword.trim())) {
                    //Password matching
                    System.out.println("Password matching");
                    String[] data = new String[4];
                    data[0] = value.getData()[0];
                    data[1] = value.getData()[1];
                    data[2] = LOGINSUCCESS.toString();
                    data[3] = UUID.randomUUID().toString();
                    Event success = new Event(LOGINSUCCESS, data);
                    getConnectedUsers().put(data[0], username);
                    getPortList().put(data[0], Integer.parseInt(data[1]));
//                    EventBus.getInstance().putEvent(success);
                    EventBus.putEvent(success);
                    System.out.println("putting event LOGINSUCCESS");
                    found = true;
                } else {
                    //Wrong password
                    System.out.println("Wrong password");
                    String[] data = new String[2];
                    data[0] = value.getData()[0];
                    data[1] = "Wrong Password!";
                    Event wrongPass = new Event(LOGINFAILED, data);
                    eventBus.put(UUID.randomUUID(), wrongPass);

                }
            }
        }
        if (!found) {
            //Username not found
            System.out.println("No such user");
            String[] data = new String[2];
            data[0] = value.getData()[0];
            data[1] = "Wrong Username!";
            Event wrongPass = new Event(LOGINFAILED, data);
            eventBus.put(UUID.randomUUID(), wrongPass);
        }
    }

    private Map<String, String> getAccounts() {

        Map<String, String> accounts = new ConcurrentHashMap<String, String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file.toString()));
            String line = br.readLine();

            while (line != null) {
                String[] lineSplit = line.split(";");
                accounts.put(lineSplit[0], lineSplit[1]);
                line = br.readLine();
            }
            br.close();

        } catch (FileNotFoundException ex) {

        } catch (IOException ex) {
            System.out.println(ex);
        }
        return accounts;
    }

    private void play(Event value) {
        //[0]IPAddress
        //[1]port
        //[2]UUID
        //[3]eventType
        //[4]Name
        String IPAddress = value.getData()[0];
//        IPAddress = IPAddress.replace("/", "");
//        int port = Integer.parseInt(value.getData()[1]);

//        String name = value.getData()[4];
        UUID ID = UUID.fromString(value.getData()[2]);
        String name = getConnectedUsers().get(IPAddress);
        getPlayingUsers().put(ID, name);
    }
}
