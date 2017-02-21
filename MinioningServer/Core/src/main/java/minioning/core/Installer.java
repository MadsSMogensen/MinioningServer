package minioning.core;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
import org.openide.modules.ModuleInstall;

/**
 *
 * @author Jakob
 */
public class Installer extends ModuleInstall /*implements Runnable */ {

    GameServer gameServer = new GameServer();

    @Override
    public void restored() {
        new Thread(new GameServer()).start();
        
//        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//        executor.scheduleAtFixedRate(gameServer.run(), initialDelay, period, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void uninstalled() {
        super.uninstalled(); //To change body of generated methods, choose Tools | Templates.

    }
    /*
    @Override
    public void run() {
        gameServer.update();
    }
     */
}
