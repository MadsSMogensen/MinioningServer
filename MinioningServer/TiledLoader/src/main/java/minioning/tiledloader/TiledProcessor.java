/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.tiledloader;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import minioning.common.data.Entity;
import org.openide.util.lookup.ServiceProvider;
import java.util.concurrent.ConcurrentHashMap;
import javafx.beans.property.MapProperty;
import minioning.common.data.EntityType;
import minioning.common.data.Location;
import minioning.common.services.ITiledLoaderService;
import minioning.tiledloader.tiled.Map;
import minioning.tiledloader.tiled.MapLayer;
import minioning.tiledloader.tiled.MapObject;
import minioning.tiledloader.tiled.ObjectGroup;
import minioning.tiledloader.tiled.TMXMapReader;
import minioning.tiledloader.tiled.Tile;
import minioning.tiledloader.tiled.TileLayer;
import minioning.tiledloader.tiled.TileSet;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
//import tiled.core.Map;
//import tiled.core.MapLayer;
//import tiled.core.ObjectGroup;
//import tiled.core.TileLayer;

/**
 *
 * @author Jakob
 */
@ServiceProvider(service = ITiledLoaderService.class)
public class TiledProcessor implements ITiledLoaderService {

    private static final String RESOURCE_ROOT = "../../../TiledLoader/src/main/resources/";
    
    ConcurrentHashMap<UUID, Entity> entities;



    @Override
    public void load(ConcurrentHashMap<UUID, Entity> entities) {
        System.out.println("load");
        this.entities = entities;
        try {
            Path file = Paths.get(RESOURCE_ROOT + "map/wilderness.tmx");

            Map tiledMap = new TMXMapReader().readMap(file.toFile().getAbsolutePath());
            System.out.println(tiledMap.getLayerCount());

            for(int i = 0; i < tiledMap.getLayerCount(); i++){
            System.out.println("Layer: "+ i + "Name:" + tiledMap.getLayer(0).getName());
            }
            MapLayer mapLayer = tiledMap.getLayer(2);
            ObjectGroup test = (ObjectGroup) mapLayer;
            int height = tiledMap.getHeight();
            int width = tiledMap.getWidth();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    MapObject newTile = test.getObjectAt(i * 32, j * 32);
                    try {
                        
                        String name = "Map";
                        UUID owner = UUID.randomUUID();
                        int x = (int)Math.round(newTile.getX()) * 32;
                        int y = (int)Math.round(newTile.getY()) * 32;
                        
                        
                        Entity newEntity = new Entity(owner, name);
                        newEntity.setLocation(Location.wilderness);
                        newEntity.setX(x);
                        newEntity.setY(y);
                        newEntity.setType(EntityType.valueOf(newTile.getType()));
                        System.out.println(newEntity.getType());
//                        newEntity.setDoorTo((Location)newTile.getDoorTo());
//                        newEntity.setLocation((Location)newTile.getLocation());
                        entities.putIfAbsent(newEntity.getID(), newEntity);
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
