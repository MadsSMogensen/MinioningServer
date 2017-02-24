package minioning.clientconnection;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;
import java.util.UUID;
import static minioning.clientconnection.Installer.getServerSocket;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import minioning.common.services.IConnectionService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mads
 */
@ServiceProvider(service = IConnectionService.class)
public class ClientOutput implements IConnectionService {

    @Override
    public void process(EventBus eventBus) {
        //Find the World in the eventbus, send it to all clients
        for (Map.Entry<UUID, Event> entry : eventBus.getBus().entrySet()) {

            UUID key = entry.getKey();
            Event value = entry.getValue();
            Event event = entry.getValue();
            switch (event.getType()) {
                case LOGINSUCCESS:
                    try {
                        String[] data = event.getData();
                        byte[] sendData = new byte[256];
                        String resultPackage = data[2] + ";" + data[3];
                        sendData = resultPackage.getBytes();
                        String IPAddressString = data[0].split("/")[1];
                        InetAddress IPAddress = InetAddress.getByName(IPAddressString);
                        int port = Integer.parseInt(data[1]);
                        System.out.println("before address already in use");
//                        DatagramSocket serverSocket = new DatagramSocket(port);
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
    }
}
