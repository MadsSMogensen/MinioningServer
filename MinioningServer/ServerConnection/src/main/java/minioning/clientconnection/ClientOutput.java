package minioning.clientconnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static minioning.clientconnection.Installer.getServerSocket;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import static minioning.common.data.Lists.getIP;
import static minioning.common.data.Lists.getPlayingUsers;
import static minioning.common.data.Lists.getPort;
import minioning.common.data.Location;
import minioning.common.services.IConnectionService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jakob and Mads
 */
@ServiceProvider(service = IConnectionService.class)
public class ClientOutput implements IConnectionService {

    /**
     *
     * @param eventBus The map of events yet to be acted upon
     * @param world The map representing the world as entities
     */
    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, ConcurrentHashMap<UUID, Entity> world) {
//        for (Map.Entry<UUID, Event> entry : eventBus.getBus().entrySet()) {
        if (eventBus.size() > 0) {
            System.out.println("output: " + eventBus.size());
        }
        for (Map.Entry<UUID, Event> entry : eventBus.entrySet()) {
            UUID key = entry.getKey();
            Event event = entry.getValue();
            System.out.println("Event found!: " + event.getType());
            switch (event.getType()) {
                case LOGINSUCCESS:
                    System.out.println("LOGINSUCCESS FOUND");
                    loginSuccess(event);
                    eventBus.remove(key);
                    break;
            }
        }
        updateClients(world);
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     */
    private void loginSuccess(Event event) {
        try {
            String[] data = event.getData();
            byte[] sendData = new byte[256];
            String[] resultPackage = new String[1];
            resultPackage[0] = data[3];
            sendData = serializeString("LOGINSUCCESS", resultPackage);
            String IPAddressString = data[0].replace("/", "");
            InetAddress IPAddress = InetAddress.getByName(IPAddressString);
            int port = Integer.parseInt(data[1]);
            DatagramSocket serverSocket = getServerSocket();
            DatagramPacket sendDataPacket
                    = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendDataPacket);
//            System.out.println("datapacket sent!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     *
     * @param world The map representing the world as entities
     */
    private void updateClients(ConcurrentHashMap<UUID, Entity> world) {
        ConcurrentHashMap<UUID, String> playingUsers = getPlayingUsers();
        //for printing amount of connected users if more than 0
//        if (getConnectedUsers().size() > 0) {     
//            System.out.println("connectedUsers: " + getConnectedUsers().size());
//        }
        for (Map.Entry<UUID, String> user : playingUsers.entrySet()) {

            try {
                String name = user.getValue();
                String IPAddressString = getIP(name);
                IPAddressString = IPAddressString.replace("/", "");
                InetAddress IPAddress = InetAddress.getByName(IPAddressString);
                int port = getPort(IPAddressString);
                UUID owner = user.getKey();

                byte[] data = new byte[256];

                data = serializeWorld("WORLD", world, owner);

                sendData(data, IPAddress, port);

            } catch (Exception e) {
            }
        }
    }

    /**
     *
     * @param data the byte array of data to send
     * @param IPAddress the ipaddress of the receiver
     * @param port the port of the receiver
     */
    private void sendData(byte[] data, InetAddress IPAddress, int port) {
        try {
            if (port != 0) {
                byte[] sendData = data;
                DatagramSocket serverSocket = getServerSocket();
                DatagramPacket sendDataPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendDataPacket);
//                System.out.println("packetSent");
            }
        } catch (Exception e) {
        }
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     * @param world The map representing the world as entities
     * @param entityOwner the UUID of the entity's owner
     * @return the byte array representing the data
     * @throws IOException
     */
    private byte[] serializeWorld(String event, ConcurrentHashMap<UUID, Entity> world, UUID entityOwner) throws IOException {
        ConcurrentHashMap<UUID, Entity> localWorld = new ConcurrentHashMap<>();
        Location location = null;
        for (Map.Entry<UUID, Entity> entry : world.entrySet()) {
            if (entry.getValue().getOwner().equals(entityOwner)) {
                location = entry.getValue().getLocation();
            }
        }
        for (Map.Entry<UUID, Entity> entry : world.entrySet()) {
            if (entry.getValue().getLocation().equals(location)) {
                if (!entry.getValue().isImmobile()) {
                    localWorld.put(entry.getKey(), entry.getValue());
                }
            }
        }

        String[] stringWorld = stringify(localWorld);
        String[] resultWorld = new String[stringWorld.length + 1];
        resultWorld[0] = event;
        for (int i = 1; i < resultWorld.length; i++) {
            resultWorld[i] = stringWorld[i - 1];
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(resultWorld);
        objectOutputStream.flush();
        objectOutputStream.close();
        final byte[] byteArray = byteArrayOutputStream.toByteArray();

        return byteArray;
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     * @param data the byte array of data to send
     * @return the byte array representing the data
     * @throws IOException
     */
    private byte[] serializeString(String event, String[] data) throws IOException {
        String[] eventData = new String[data.length + 1];
        eventData[0] = event;
        for (int i = 1; i <= data.length; i++) {
            eventData[i] = data[i - 1];
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(eventData);
        objectOutputStream.flush();
        objectOutputStream.close();
        final byte[] byteArray = byteArrayOutputStream.toByteArray();

        return byteArray;
    }

    /**
     *
     * @param world The map representing the world as entities
     * @return a String array representing the world data
     */
    private String[] stringify(ConcurrentHashMap<UUID, Entity> world) {
        String[] resultString = new String[world.size()];
        int index = 0;
        for (Entity e : world.values()) {
            resultString[index] = e.toClients();
            index++;
        }
        return resultString;
    }
}
