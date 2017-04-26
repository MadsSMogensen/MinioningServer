package minioning.common.data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Mogensen
 */
public class Lists {

    //IPAdress, name
    private static ConcurrentHashMap<String, String> connectedUsers = new ConcurrentHashMap<String, String>();
    //IPadress, port
    private static ConcurrentHashMap<String, Integer> connectedUserPorts = new ConcurrentHashMap<String, Integer>();
    //UUID, name
    private static ConcurrentHashMap<UUID, String> playingUsers = new ConcurrentHashMap<UUID, String>();

    //<IPAdress, name>
    public static ConcurrentHashMap<String, String> getConnectedUsers() {
        if (connectedUsers == null) {
            connectedUsers = new ConcurrentHashMap<String, String>();
        }
        return connectedUsers;
    }

    public static String getIP(String name) {
        for (Map.Entry<String, String> connectedUser : getConnectedUsers().entrySet()) {
            String key = connectedUser.getKey();
            String value = connectedUser.getValue();
            if (value.equals(name)) {
                return key;
            }
        }
        return null;
    }

    public static ConcurrentHashMap<String, Integer> getPortList() {
        if (connectedUserPorts == null) {
            connectedUserPorts = new ConcurrentHashMap<String, Integer>();
        }
        return connectedUserPorts;
    }

    public static ConcurrentHashMap<UUID, String> getPlayingUsers() {
        if (playingUsers == null) {
            playingUsers = new ConcurrentHashMap<UUID, String>();
        }
        return playingUsers;
    }

    public static void newPlayer(UUID ID, String name) {
        getPlayingUsers().put(ID, name);
    }

    public static int getPort(String IPAddress) {
        int port = 0;
        for (Map.Entry<String, Integer> entry : getPortList().entrySet()) {
            String IPAdressKey = entry.getKey();
            int portValue = entry.getValue();
            if (IPAdressKey.equals(IPAddress)) {
                port = portValue;
            }
        }
        return port;
    }

    //disconnectUser by ipaddress
    public static void disconnectUser(String user) {
        //simply try to remove user by IPAddress
        try {
            getPortList().remove(user);
            getConnectedUsers().remove(user);
            //ip found
            return;
        } catch (Exception e) {
            //No such ip found
        }
        //try to remove user by name
        try {
            getConnectedUsers().values().remove(user);
            //name found
        } catch (Exception e) {
            //no such name found
        }
    }

    //disconnect user by UUID
    public static void disconnectUser(UUID ID) {
        try {
            String name = getPlayingUsers().get(ID);
            String IPAddress = "";
            for (Map.Entry<String, String> entry : getConnectedUsers().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (value.equals(name)) {
                    IPAddress = key;
                }
            }
            getPlayingUsers().remove(ID);
            getConnectedUsers().remove(IPAddress);
            getPortList().remove(IPAddress);
        } catch (Exception e) {
        }
    }

    public static void connectUser(String IPAddress, int port, String name, UUID ID) {
        getConnectedUsers().put(IPAddress, name);
        getPortList().put(IPAddress, port);
        getPlayingUsers().put(ID, name);
    }
}
