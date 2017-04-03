/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.tiledloader;

import com.badlogic.gdx.Gdx;
import java.util.UUID;
import minioning.common.data.Entity;
import org.openide.util.lookup.ServiceProvider;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.services.ITiledLoaderService;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author Jakob
 */
@ServiceProvider(service = ITiledLoaderService.class)
public class TiledProcessor implements ITiledLoaderService, ApplicationListener {

    private static final String RESOURCE_ROOT = "../../../TiledLoader/src/main/resources/";
    ConcurrentHashMap<UUID, Entity> entities;
    private Map tiledMap;

    private boolean loaded = false;

    @Override
    public void load(ConcurrentHashMap<UUID, Entity> entities) {
        System.out.println("load");
        this.entities = entities;
//        create();
//        render();

//            LoadAssets(this.tiledMap);
//
        entities = this.entities;
//        System.out.println("Size " + entities.size());

    }

    private void LoadAssets(Map tiledMap) {

        // Convert Tiled objects to entities
        System.out.println("test 3" + this.tiledMap.getLayers().getCount());

        try {
            System.out.println("Loading Objects");
            Iterable<MapObject> objects = tiledMap.getLayers().get("Collision").getObjects();
            System.out.println("Objects Loaded");
            for (MapObject object : objects) {

                // Get tile properties
                UUID id = UUID.randomUUID();

                MapProperties props = object.getProperties();
                int width = (int) Math.floor(props.get("width", int.class));
                int height = (int) Math.floor(props.get("height", int.class));

                // Create new entity
                Entity e = new Entity(id, "tile");
                e.setX(props.get("x", int.class) + width / 2);
                e.setY(props.get("y", int.class) + height / 2);

                System.out.println(e.getX() + " and " + e.getY());

                // Place entity in world
                entities.putIfAbsent(id, e);
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void create() {
        System.out.println("create");
        Map tiledMap = new TmxMapLoader().load(RESOURCE_ROOT + "map/wilderness.tmx");
    }

    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void render() {
//        System.out.println("render");
//        if (loaded == false) {
//            System.out.println("Load map");
//         
//            
//            
//            Map testMap = new TmxMapLoader().load("map/wilderness.tmx");
//            
//            System.out.println("test 1 " + testMap.getLayers().getCount());
//            System.out.println("test 2" + this.tiledMap.getLayers().getCount());
//            
//            loaded = true;
//            if (!tiledMap.equals(null)) {
//                System.out.println("Map loaded");
//            }
//        }
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
