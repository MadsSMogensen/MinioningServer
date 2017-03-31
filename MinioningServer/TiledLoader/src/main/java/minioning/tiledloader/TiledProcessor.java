/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.tiledloader;

import com.badlogic.gdx.assets.loaders.resolvers.ExternalFileHandleResolver;
import com.badlogic.gdx.maps.Map;
import java.util.UUID;
import minioning.common.data.Entity;
import org.openide.util.lookup.ServiceProvider;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.services.ITiledLoaderService;
import com.badlogic.gdx.ApplicationListener;
/**
 *
 * @author Jakob
 */
@ServiceProvider(service = ITiledLoaderService.class)
public class TiledProcessor implements ITiledLoaderService, ApplicationListener{

    private static final String RESOURCE_ROOT = "../../TiledLoader/src/main/resources/";



    @Override
    public void load(ConcurrentHashMap<UUID, Entity> entities) {

        // Load tiled map

//        tiledMap = mloader.load(RESOURCE_ROOT + "map/level1.tmx");

//        // Convert Tiled objects to entities
//        Iterable<MapObject> objects = tiledMap.getLayers().get("Collision").getObjects();
//        System.out.println("2");
//        for (MapObject object : objects) {
//            // Get tile properties
//            System.out.println("3");
//            UUID id = UUID.randomUUID();
//
////            MapProperties props = object.getProperties();
////            int width = (int) Math.floor(props.get("width", float.class));
////            int height = (int) Math.floor(props.get("height", float.class));
//            // Create new entity
//            Entity e = new Entity(id, "tile");
////            e.setType(EntityType.TILE);
////            e.setX(props.get("x", float.class) + width / 2);
////            e.setY(props.get("y", float.class) + height / 2);
////            e.setSize(width, height);
//
//            // Place entity in world
//            entities.putIfAbsent(id, e);
//        }
    }

    @Override
    public void create() {
        
        Map tiledMap = new TmxMapLoader().load("map/wilderness.tmx");
        
//             Map tiledMap;
//
//     ExternalFileHandleResolver fr = new ExternalFileHandleResolver();
//     TmxMapLoader mloader = new TmxMapLoader(fr);
//        tiledMap = mloader.load(RESOURCE_ROOT + "map/level1.tmx");
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void render() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }

}
