package minioning.clientconnection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import static minioning.common.data.Events.*;
import minioning.common.services.IConnectionService;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mogensen
 */
@ServiceProvider(service = IConnectionService.class)
public class Login implements IConnectionService {

    private static Path file;

    @Override
    public void process(EventBus eventBus) {
        if (file == null) {
            file = Paths.get("testMinioningFileCreation.txt");
//            try {
//                file.toFile().createNewFile();
//                System.out.println("file created");
//            } catch (IOException ex) {
//                System.out.println(ex);
//            }
        }
        for (Map.Entry<UUID, Event> entry : eventBus.getBus().entrySet()) {

            UUID key = entry.getKey();
            Event value = entry.getValue();

            if (value.getType().equals(CREATELOGIN)) {
                String[] data = value.getData();
                String userName = data[2];
                String password = data[3];
//                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
//                        new FileOutputStream("testMinioningFileCreation.txt"), "utf-8"))) {
//                    writer.append(userName + ";" + password);
//                } catch (IOException ex) {
//                    System.out.println(ex);
//                }
                try {
                    Writer output;
                    output = new BufferedWriter(new FileWriter(file.toString(), true));
                    output.append(userName.trim() + ";" + password.trim() + "\n");
                    output.close();
                } catch (Exception e) {
                    System.out.println(e);
                }

                eventBus.getBus().remove(key);
            }

            if (value.getType().equals(LOGIN)) {
                String attemptUserName = value.getData()[2];
                String attemptPassword = value.getData()[3];

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
                for (Map.Entry<String, String> accountEntry : accounts.entrySet()) {
                    String userName = accountEntry.getKey();
                    String password = accountEntry.getValue();
                    if (userName.equals(attemptUserName)) {
                        //Username found
                        System.out.println("Username Found");
                        if (password.trim().equals(attemptPassword.trim())) {
                            //Password matching
                            System.out.println("Password matching");
                        } else {
                            //Wrong password
                            System.out.println("Wrong password");
                        }
                    } else {
                        //Username not found
                        System.out.println("No such user");
                    }
                }
                eventBus.getBus().remove(key);
            }
        }
    }
}
