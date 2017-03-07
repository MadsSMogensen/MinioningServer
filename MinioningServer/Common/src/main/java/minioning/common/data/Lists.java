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
    private static ConcurrentHashMap<String, String> connectedUsers;
    //IPadress, port
    private static ConcurrentHashMap<String, Integer> connectedUserPorts;
    //UUID, name
    private static ConcurrentHashMap<UUID, String> playingUsers;
    
    //<IPAdress, name>
    public static ConcurrentHashMap<String, String> getConnectedUsers() {
        if(connectedUsers == null){
            connectedUsers = new ConcurrentHashMap<String, String>();
        }
        return connectedUsers;
    }
    
    public static ConcurrentHashMap<String, Integer> getPortList(){
        if(connectedUserPorts == null){
            connectedUserPorts = new ConcurrentHashMap<String, Integer>();
        }
        return connectedUserPorts;
    }
    
    public static ConcurrentHashMap<UUID, String> getPlayingUsers(){
        if(playingUsers == null){
            playingUsers = new ConcurrentHashMap<UUID, String>();
        }
        return playingUsers;
    }
    
    public static void newPlayer(UUID ID, String name){
        getPlayingUsers().put(ID, name);
    }
    
    public static int getPort(String IPAddress){
        int port = 0;
        for(Map.Entry<String, Integer> entry : getPortList().entrySet()){
            String IPAdressKey = entry.getKey();
            int portValue = entry.getValue();
            if(IPAdressKey.equals(IPAddress)){
                port = portValue;
            }
        }
        return port;
    }
    
    
    
    public static void disconnectUser(String user){
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
    
    public static void connectUser(String IPAddress, int port, String name){
        getConnectedUsers().put(IPAddress, name);
        getPortList().put(IPAddress, port);
    }
}
