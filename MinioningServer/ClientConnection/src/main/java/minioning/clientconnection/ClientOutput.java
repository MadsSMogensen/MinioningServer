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
import minioning.common.data.EventBus;
import static minioning.common.data.Lists.getConnectedUsers;
import static minioning.common.data.Lists.getIP;
import static minioning.common.data.Lists.getPlayingUsers;
import static minioning.common.data.Lists.getPort;
import minioning.common.services.IConnectionService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mads
 */
@ServiceProvider(service = IConnectionService.class)
public class ClientOutput implements IConnectionService {

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
            System.out.println("datapacket sent!");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void updateClients(ConcurrentHashMap<UUID, Entity> world) {
        ConcurrentHashMap<UUID, String> playingUsers = getPlayingUsers();
        //for printing amount of connected users if more than 0
//        if (getConnectedUsers().size() > 0) {     
//            System.out.println("connectedUsers: " + getConnectedUsers().size());
//        }
        for (Map.Entry<UUID, String> user : playingUsers.entrySet()) {

            try {
                String name = user.getValue();
//                String IPAddressString = user.getKey();
                String IPAddressString = getIP(name);
                IPAddressString = IPAddressString.replace("/", "");
                InetAddress IPAddress = InetAddress.getByName(IPAddressString);
                int port = getPort(IPAddressString);

                byte[] sendData = new byte[256];

                sendData = serializeWorld("WORLD", world);

                sendData(sendData, IPAddress, port);

//                try { //GAMMEL KODE
//                    DatagramSocket serverSocket = getServerSocket();
//                    if (port != 0) {
//                        DatagramPacket sendDataPacket
//                                = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//                        serverSocket.send(sendDataPacket);
////                        System.out.println("sent: " + new String(sendDataPacket.getData()));
//                    }
//                } catch (Exception e) {
//                }
            } catch (Exception e) {
            }
        }
    }

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

    private byte[] serializeWorld(String event, ConcurrentHashMap<UUID, Entity> world) throws IOException {
        String[] stringWorld = stringify(world);
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

//The previous updateClients();
/*
 ConcurrentHashMap<String, String> connectedUsers = getConnectedUsers();
 //for printing amount of connected users if more than 0
 //        if (getConnectedUsers().size() > 0) {     
 //            System.out.println("connectedUsers: " + getConnectedUsers().size());
 //        }
 for (Map.Entry<String, String> user : connectedUsers.entrySet()) {
 try {
 String IPAddressString = user.getKey();
 IPAddressString = IPAddressString.replace("/", "");
 InetAddress IPAddress = InetAddress.getByName(IPAddressString);
 String name = user.getValue();
 int port = getPort(IPAddressString);

 byte[] sendData = new byte[256];
 String test = "THISISATEST;100;200";
                
 sendData = test.getBytes();
 try {
 DatagramSocket serverSocket = getServerSocket();
 if (port != 0) {
 DatagramPacket sendDataPacket
 = new DatagramPacket(sendData, sendData.length, IPAddress, port);
 serverSocket.send(sendDataPacket);
 //                        System.out.println("sent: " + new String(sendDataPacket.getData()));
 }
 } catch (Exception e) {
 }
 } catch (Exception e) {
 }
 }
 */
