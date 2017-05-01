/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.tiledloader;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import minioning.common.data.Entity;
import org.openide.util.lookup.ServiceProvider;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.EntityType;
import minioning.common.data.GameData;
import minioning.common.data.Location;
import minioning.common.services.ITiledLoaderService;
import minioning.tiledloader.tiled.Map;
import minioning.tiledloader.tiled.MapLayer;
import minioning.tiledloader.tiled.MapObject;
import minioning.tiledloader.tiled.ObjectGroup;
import minioning.tiledloader.tiled.TMXMapReader;
import org.openide.util.Exceptions;

/**
 *
 * @author Jakob & Mads
 */
@ServiceProvider(service = ITiledLoaderService.class)
public class TiledProcessor implements ITiledLoaderService {

    private static final String RESOURCE_ROOT = "../../../TiledLoader/src/main/resources/";


    /**
     * Tries to load entities and put them into the HashMap containing all entities for processing from a tmx file
     * 
     * @param entities HashMap of every entity
     * @param fileName This is the name of the tmx file that will be read
     */
    
    
    private void loadFromFile(ConcurrentHashMap<UUID, Entity> entities, String fileName){

        
        try {
            Path file = Paths.get(RESOURCE_ROOT + "map/" + fileName +".tmx");

            Map tiledMap = new TMXMapReader().readMap(file.toFile().getAbsolutePath());


            MapLayer mapLayer = tiledMap.getLayer(2);
            ObjectGroup test = (ObjectGroup) mapLayer;
            int height = tiledMap.getHeight();
            int width = tiledMap.getWidth();

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    MapObject newTile = test.getObjectAt(i * 32, j * 32);
                    try {
                        
                        String name = "";
                        UUID owner = UUID.randomUUID();
                        int x = (int) Math.round(newTile.getX() + newTile.getWidth() / 2); 
                        int y = (int) Math.round(newTile.getY() + newTile.getWidth() / 2); 
                        y = GameData.getGameHeight() - y;

                        Entity newEntity = new Entity(owner, name, x, y);
                        
                        newEntity.setLocation(Location.valueOf(fileName));
                        newEntity.setType(EntityType.valueOf(newTile.getType()));
                        System.out.println(newEntity.getType());
                        if (newEntity.getType() == EntityType.DOOR) {
                            
                            String s = newTile.getName();
                            
                            String[] sa = s.split(";");
                            
                            
                            System.out.println(sa[0] + sa[1] + sa[2]);
                            newEntity.setDoorTo(Location.valueOf(sa[0]));
                            
                            newEntity.setDoorToX(Integer.parseInt(sa[1]));
                            newEntity.setDoorToY(GameData.getGameHeight() - Integer.parseInt(sa[2]));
                           
                        }
                        newEntity.setImmobile(true);
                        System.out.println(newEntity.getType());

                        entities.putIfAbsent(newEntity.getID(), newEntity);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        } catch (Exception ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    
    
    
    /**
     * Loads all tmx files for the game
     * 
     * @param entities HashMap of every entity
     */
    
    @Override
    public void load(ConcurrentHashMap<UUID, Entity> entities) {
       
        System.out.println("Loading entities from Tiled");
        loadFromFile(entities, "wilderness");
        loadFromFile(entities,"wilderness_east");
        loadFromFile(entities,"wilderness_west");
        loadFromFile(entities,"arena");
        loadFromFile(entities,"cave");
    }
}
