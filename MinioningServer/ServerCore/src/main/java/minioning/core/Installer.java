package minioning.core;

import org.openide.modules.ModuleInstall;

/**
 *
 * @author Jakob and Mads
 */
public class Installer extends ModuleInstall {

    GameServer gameServer = new GameServer();

    /**
     * Starts the GameServer thread
     */
    @Override
    public void restored() {
        new Thread(new GameServer()).start();
    }

    /**
     * Uninstalls the module
     */
    @Override
    public void uninstalled() {
        super.uninstalled();

    }
}
