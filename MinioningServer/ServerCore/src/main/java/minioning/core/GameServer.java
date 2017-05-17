package minioning.core;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import static minioning.common.data.EventBus.getBus;
import minioning.common.data.GameData;
import minioning.common.services.IConnectionService;
import minioning.common.services.IEntityCreatorService;
import minioning.common.services.IEntityProcessingService;
import minioning.common.services.IHealthProcessorService;
import minioning.common.services.ITiledLoaderService;
import org.openide.util.Lookup;

/**
 *
 * @author Jakob & Mads
 */
public class GameServer implements Runnable {

    // Constants
    float timeStep = (float) 0.1;
    long lastTime = System.nanoTime();

    // Internal & game data
    private ConcurrentHashMap<UUID, Entity> world = new ConcurrentHashMap(); //burde vi have singleton på den her liste??

    private final Lookup lookup = Lookup.getDefault();

    /**
     *
     */
    public void update() {
        for (IEntityProcessingService processor : getIEntityProcessingServices()) {
            for (Entity e : world.values()) {
                processor.process(getBus(), world, e);
            }
        }
    }

    /**
     *
     */
    public void updateConnection() {
        for (IConnectionService processor : getIConnectionServices()) {
            processor.process(getBus(), world);
        }
    }

    /**
     *
     */
    private void create() {
        for (IEntityCreatorService creator : getIEntityCreatorServices()) {
            creator.createNew(getBus(), world);
        }
    }

    /**
     *
     */
    private void loadMap() {
        for (ITiledLoaderService loader : getITiledLoaderServices()) {
            loader.load(world);
        }
    }

    /**
     *
     */
    private void updateHP() {
        for (IHealthProcessorService processor : getIHealthProcessor()) {
            processor.process(getBus(), world);
        }
    }

    /**
     *
     * @return
     */
    private Collection<? extends IEntityProcessingService> getIEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    /**
     *
     * @return
     */
    private Collection<? extends IHealthProcessorService> getIHealthProcessor() {
        return lookup.lookupAll(IHealthProcessorService.class);
    }

    /**
     *
     * @return
     */
    private Collection<? extends IEntityCreatorService> getIEntityCreatorServices() {
        return lookup.lookupAll(IEntityCreatorService.class);
    }

    /**
     *
     * @return
     */
    private Collection<? extends IConnectionService> getIConnectionServices() {
        return lookup.lookupAll(IConnectionService.class);
    }

    /**
     *
     * @return
     */
    private Collection<? extends ITiledLoaderService> getITiledLoaderServices() {
        return lookup.lookupAll(ITiledLoaderService.class);
    }

    /**
     *
     */
    @Override
    public void run() {
        loadMap();
        while (true) {
            long currentTime = System.nanoTime();
            float elapsedTime = (currentTime - lastTime);
            elapsedTime /= (float) 1000000000.0;
            GameData.setDt(elapsedTime);
            if (elapsedTime >= timeStep) {
                lastTime = currentTime;
                
                updateConnection();
                create();
                update();
                updateHP();
            }
        }
    }
}
