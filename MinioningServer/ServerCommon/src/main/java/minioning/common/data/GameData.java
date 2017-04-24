package minioning.common.data;

/**
 *
 * @author Helle
 */
public class GameData {
    private static float dt;
    private static int port = 80;
//    private static int port = 9876;

    public static float getDt() {
        return dt;
    }

    public static int getPort() {
        return port;
    }

    public static void setDt(float dt) {
        GameData.dt = dt;
    }
    
    
}
