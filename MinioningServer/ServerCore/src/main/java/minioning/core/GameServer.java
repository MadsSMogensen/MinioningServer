package minioning.core;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import static minioning.common.data.EventBus.getBus;
import minioning.common.services.IConnectionService;
import minioning.common.services.IEntityCreatorService;
import minioning.common.services.IEntityProcessingService;
import minioning.common.services.ITiledLoaderService;
import org.openide.util.Lookup;

public class GameServer implements Runnable {

    // Constants
    double timeStep = 0.1;
    long lastTime = System.nanoTime();

    // Internal & game data
    private ConcurrentHashMap<UUID, Entity> world = new ConcurrentHashMap(); //burde vi have singleton på den her liste??
//    private EventBus eventBus = new EventBus();

    private final Lookup lookup = Lookup.getDefault();

//    private EventBus getEventBus() {
//        if (eventBus == null) {
//            eventBus = new EventBus();
//            System.out.println("new instance created");
//        }
//        return eventBus;
//    }

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
<<<<<<< HEAD
    private void update() {
=======
    public void update() {
//        System.out.println("eventBus.size upd: " + getEventBus().size());
>>>>>>> master
//      Process using all entity processing services
        for (IEntityProcessingService processor : getIEntityProcessingServices()) {
            for (Entity e : world.values()) {
                processor.process(getBus(), world, e);
            }
        }
    }

<<<<<<< HEAD
    private void updateConnection() {
=======
    public void updateConnection() {
//        System.out.println("eventBus.size con: " + getEventBus().size());
>>>>>>> master
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
    
    private void loadMap(){
             for (ITiledLoaderService loader : getITiledLoaderServices()) {
           
                loader.load(world);
            
        }
    }

    private Collection<? extends IEntityProcessingService> getIEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
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
        boolean test = true;

        System.out.println("1");
        loadMap();
        System.out.println("2");
        while (true) {
            long currentTime = System.nanoTime();
            double elapsedTime = (currentTime - lastTime) / 1000000000.0;

            if (elapsedTime >= timeStep) {
                lastTime = currentTime;
//                System.out.println(getBus().size());
//                System.out.println(world.size());
                updateConnection();
                create();
                update();
            }
            
            
            if (test) {
                
                Entity testEntity = new Entity(UUID.randomUUID(), "TEST");
                testEntity.setDx(500);
                testEntity.setDy(500);
                world.put(testEntity.getID(), testEntity);
                
                Entity testCollisionEntity = new Entity(UUID.randomUUID(), "HITME");
                testCollisionEntity.setX(450);
                testCollisionEntity.setY(450);
                world.put(testCollisionEntity.getID(), testCollisionEntity);
//                String[] data = new String[6];
//                data[0] = "localhost";
//                data[1] = "9876";
//                data[2] = "";
//                data[3] = "LOGIN";
//                data[4] = "hit";
//                data[5] = "me";
//                Event testEvent = new Event(LOGIN, data);
//                EventBus.putEvent(testEvent);
//                System.out.println("login put");
//
//                String[] data1 = new String[6];
//                data[0] = "localhost";
//                data[1] = "9876";
//                data[2] = UUID.randomUUID().toString();
//                data[3] = "PLAY";
//                data[4] = "hit";
//                Event playEvent = new Event(PLAY, data);
//                EventBus.getInstance().putEvent(playEvent);
//                System.out.println("playEvent put");

                test = false;
            }
        }
    }
}
