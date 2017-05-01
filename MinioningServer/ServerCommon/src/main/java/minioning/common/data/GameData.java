package minioning.common.data;

/**
 *
 * @author Helle
 */
public class GameData {
    private static float dt;
    private static int port = 80;
    private static int gameHeight = 576;
    private static int gameWidth = 1024;

    public static float getDt() {
        return dt;
    }

    public static int getPort() {
        return port;
    }

    public static void setDt(float dt) {
        GameData.dt = dt;
    }

    public static int getGameWidth() {
        return gameWidth;
    }

    public static void setGameWidth(int gameWidth) {
        GameData.gameWidth = gameWidth;
    }

    public static int getGameHeight() {
        return gameHeight;
    }

    public static void setGameHeight(int gameHeight) {
        GameData.gameHeight = gameHeight;
    }
    
    
}
