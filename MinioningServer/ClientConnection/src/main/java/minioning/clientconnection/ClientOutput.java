package minioning.clientconnection;

import java.io.ByteArrayOutputStream;
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
import static minioning.common.data.Lists.connectUser;
import static minioning.common.data.Lists.getConnectedUsers;
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
    public void process(EventBus eventBus, ConcurrentHashMap<UUID, Entity> world) {
        //Find the World in the eventbus, send it to all clients
        for (Map.Entry<UUID, Event> entry : eventBus.getBus().entrySet()) {

            UUID key = entry.getKey();
            Event event = entry.getValue();
            switch (event.getType()) {
                case LOGINSUCCESS:
                    try {
                        String[] data = event.getData();
                        byte[] sendData = new byte[256];
                        String resultPackage = /*data[2] + ";" + */ data[3];
                        sendData = resultPackage.getBytes();
                        String IPAddressString = data[0].split("/")[1];
                        InetAddress IPAddress = InetAddress.getByName(IPAddressString);
                        int port = Integer.parseInt(data[1]);
                        DatagramSocket serverSocket = getServerSocket();
                        DatagramPacket sendDataPacket
                                = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                        serverSocket.send(sendDataPacket);
                        System.out.println("datapacket sent!");
                        System.out.println(resultPackage);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    eventBus.getBus().remove(key);
                    break;
            }
        }
        //outside the for eventbus
        ConcurrentHashMap<String, String> connectedUsers = getConnectedUsers();
//        if (getConnectedUsers().size() > 0) {     //for printing amount of connected users > 0
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
                
                
                
                
                
                
//                if (getPlayingUsers().containsValue(name)) { //for difference on connected users and playing users
//                    byte[] sendData = new byte[256];
//                    try {
//                        ByteArrayOutputStream out = new ByteArrayOutputStream();
//                        ObjectOutputStream outputStream = new ObjectOutputStream(out);
//                        outputStream.writeObject(world);
//                        outputStream.close();
//                        sendData = out.toByteArray();
//                    } catch (Exception e) {
//                    }
//                    try {
//                        DatagramSocket serverSocket = getServerSocket();
//                        if (port != 0) {
//                            DatagramPacket sendDataPacket
//                                    = new DatagramPacket(sendData, sendData.length, IPAddress, port);
//                            serverSocket.send(sendDataPacket);
//                        }
//                    } catch (Exception e) {
//                    }
//                }
            } catch (Exception e) {
            }
        }
    }
}
