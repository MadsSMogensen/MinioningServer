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
 * @author Jakob and Mads
 */
public class GameServer implements Runnable {

    // Constants
    float timeStep = (float) 0.1;
    long lastTime = System.nanoTime();

    // Internal & game data
    private ConcurrentHashMap<UUID, Entity> world = new ConcurrentHashMap(); //burde vi have singleton på den her liste??
    private final Lookup lookup = Lookup.getDefault();
    
    public ConcurrentHashMap<UUID, Entity> getWorld(){
        if(world == null){
            world = new ConcurrentHashMap();
        }
        return world;
    }
    
    public float getDt(){
        return GameData.getDt();
    }
    
    /**
     * running IEntityProcessingServices
     */
    public void update() {
        for (IEntityProcessingService processor : getIEntityProcessingServices()) {
            for (Entity e : world.values()) {
                processor.process(getBus(), world, e);
            }
        }
    }

    /**
     * running IConnectionServices
     */
    public void updateConnection() {
        for (IConnectionService processor : getIConnectionServices()) {
            processor.process(getBus(), world);
        }
    }

    /**
     * running IEntityCreatorServices
     */
    private void create() {
        for (IEntityCreatorService creator : getIEntityCreatorServices()) {
            creator.createNew(getBus(), world);
        }
    }

    /**
     * running ITiledLoaderServices
     */
    private void loadMap() {
        for (ITiledLoaderService loader : getITiledLoaderServices()) {
            loader.load(world);
        }
    }

    /**
     * running IHealthProcessorServices
     */
    private void updateHP() {
        for (IHealthProcessorService processor : getIHealthProcessor()) {
            processor.process(getBus(), world);
        }
    }

    /**
     *
     * @return a Collection of IEntityProcessingServices
     */
    private Collection<? extends IEntityProcessingService> getIEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    /**
     *
     * @return a Collection of IHealthProcessorServices
     */
    private Collection<? extends IHealthProcessorService> getIHealthProcessor() {
        return lookup.lookupAll(IHealthProcessorService.class);
    }

    /**
     *
     * @return a Collection of IEntityCreatorServices
     */
    private Collection<? extends IEntityCreatorService> getIEntityCreatorServices() {
        return lookup.lookupAll(IEntityCreatorService.class);
    }

    /**
     *
     * @return a Collection of IConnectionServices
     */
    private Collection<? extends IConnectionService> getIConnectionServices() {
        return lookup.lookupAll(IConnectionService.class);
    }

    /**
     *
     * @return a Collection of ITiledLoaderServices
     */
    private Collection<? extends ITiledLoaderService> getITiledLoaderServices() {
        return lookup.lookupAll(ITiledLoaderService.class);
    }

    /**
     * The main thread loop
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
