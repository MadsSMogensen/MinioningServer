package minioning.core;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.EntityType;
import static minioning.common.data.EventBus.getBus;
import minioning.common.data.GameData;
import minioning.common.data.Location;
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
    public void update() {
//        System.out.println("eventBus.size upd: " + getEventBus().size());
//      Process using all entity processing services
        for (IEntityProcessingService processor : getIEntityProcessingServices()) {
            for (Entity e : world.values()) {
                processor.process(getBus(), world, e);
            }
        }
    }

    public void updateConnection() {
//        System.out.println("eventBus.size con: " + getEventBus().size());
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

//            loader.load(world);
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
        boolean test = true;
        
        loadMap();
        while (true) {
            long currentTime = System.nanoTime();
            float elapsedTime = (currentTime - lastTime);
            elapsedTime /= (float) 1000000000.0;
            GameData.setDt(elapsedTime);
            if (elapsedTime >= timeStep) {
                lastTime = currentTime;
//                System.out.println(getBus().size());
//                System.out.println(world.size());
                updateConnection();
                create();
                update();
                updateHP();
//                System.out.println(world.size());
            }

            if (test) {
                //DOOR
//                Entity testEntity = new Entity(UUID.randomUUID(), "DOORTEST");
//                testEntity.setX(500);
//                testEntity.setY(500);
//                testEntity.setLocation(Location.wilderness);
//                testEntity.setDoorTo(Location.arena);
//                testEntity.setType(EntityType.DOOR);
//                testEntity.setImmobile(true);
//                world.put(testEntity.getID(), testEntity);

                Entity testEntity = new Entity(UUID.randomUUID(), "", 300, 300);
                testEntity.setImmobile(true);
                testEntity.setLocation(Location.arena);
                testEntity.setDoorTo(Location.wilderness);
                testEntity.setType(EntityType.DOOR);
                world.put(testEntity.getID(), testEntity);

                Entity testEntity2 = new Entity(UUID.randomUUID(), "", 400, 400);
                testEntity2.setImmobile(true);
                testEntity2.setLocation(Location.wilderness);
                testEntity2.setDoorTo(Location.arena);
                testEntity2.setType(EntityType.DOOR);
                world.put(testEntity2.getID(), testEntity2);

//                for(int i = 0; i < 10; i++){
//                    Entity newMonster = new Entity(UUID.randomUUID(), "", 150+(5*i), 150+(5*i));
//                    newMonster.setLocation(Location.wilderness);
//                    newMonster.setType(EntityType.ENEMY);
//                    world.put(newMonster.getID(), newMonster);
//                }
//                
//                Entity testCollisionEntity = new Entity(UUID.randomUUID(), "HITME");
//                testCollisionEntity.setX(450);
//                testCollisionEntity.setY(450);
//                testCollisionEntity.setDx(100);
//                testCollisionEntity.setDy(100);
//                world.put(testCollisionEntity.getID(), testCollisionEntity);
//                
//                Entity fun = new Entity(UUID.randomUUID(), "FUN");
//                fun.setX(450);
//                fun.setY(100);
//                fun.setDx(100);
//                fun.setDy(450);
//                world.put(fun.getID(), fun);
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
