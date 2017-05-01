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

public class GameServer implements Runnable {

    // Constants
    float timeStep = (float) 0.1;
    long lastTime = System.nanoTime();

    // Internal & game data
    private ConcurrentHashMap<UUID, Entity> world = new ConcurrentHashMap();

    private final Lookup lookup = Lookup.getDefault();

    /*
     private void loadGamePlugins() {
     // Lookup game plugins
     Lookup.Result<IGamePluginService> result = lookup.lookupResult(IGamePluginService.class);
     result.addLookupListener(lookupListener);
     synchronized(gamePlugins) {
     gamePlugins.addAll(result.allInstances());
     }
     result.allItems();
        
     // Start game plugins
     synchronized(gamePlugins) {
     for (IGamePluginService plugin : gamePlugins) {
     plugin.start(gameData, world);
     }
     }
     }
     */
    public void update() {
//      Process using all entity processing services
        for (IEntityProcessingService processor : getIEntityProcessingServices()) {
            for (Entity e : world.values()) {
                processor.process(getBus(), world, e);
            }
        }
    }

    public void updateConnection() {
        for (IConnectionService processor : getIConnectionServices()) {
            processor.process(getBus(), world);
        }
    }

    private void create() {
//      Process using all entity processing services
        for (IEntityCreatorService creator : getIEntityCreatorServices()) {
            creator.createNew(getBus(), world);
        }
    }

    private void loadMap() {
        for (ITiledLoaderService loader : getITiledLoaderServices()) {
            loader.load(world);
        }
    }

    private void updateHP() {
        for (IHealthProcessorService processor : getIHealthProcessor()) {
            processor.process(getBus(), world);
        }
    }

    private Collection<? extends IEntityProcessingService> getIEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    private Collection<? extends IHealthProcessorService> getIHealthProcessor() {
        return lookup.lookupAll(IHealthProcessorService.class);
    }

    private Collection<? extends IEntityCreatorService> getIEntityCreatorServices() {
        return lookup.lookupAll(IEntityCreatorService.class);
    }

    private Collection<? extends IConnectionService> getIConnectionServices() {
        return lookup.lookupAll(IConnectionService.class);
    }

    private Collection<? extends ITiledLoaderService> getITiledLoaderServices() {
        return lookup.lookupAll(ITiledLoaderService.class);
    }

    /*
     private final LookupListener lookupListener = new LookupListener() {
     //Listens for changes in components, starting and stopping them when needed
     @Override
     public void resultChanged(LookupEvent le) {
     Collection<? extends IGamePluginService> updatedPluginList = lookup.lookupAll(IGamePluginService.class);
     for (IGamePluginService updatedGamePlugin : updatedPluginList) {
     synchronized(gamePlugins) {
     if (!gamePlugins.contains(updatedGamePlugin)) {
     updatedGamePlugin.start(gameData, world);
     gamePlugins.add(updatedGamePlugin);
     }
     }
     }
     synchronized(gamePlugins) {
     for (IGamePluginService gamePlugin : gamePlugins) {
     if(!updatedPluginList.contains(gamePlugin)) {
     gamePlugin.stop(gameData);
     gamePlugins.remove(gamePlugin);
     }
     }
     }
     }
     };
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
