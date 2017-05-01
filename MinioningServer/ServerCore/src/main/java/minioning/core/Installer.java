package minioning.core;

import org.openide.modules.ModuleInstall;

/**
 *
 * @author Jakob
 */
public class Installer extends ModuleInstall {

    GameServer gameServer = new GameServer();

    @Override
    public void restored() {
        new Thread(new GameServer()).start();
    }
    
    @Override
    public void uninstalled() {
        super.uninstalled();
    }
}
