package minioning.collision;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import minioning.common.data.Entity;
import static minioning.common.data.EntityType.DOOR;
import static minioning.common.data.EntityType.PLAYER;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import minioning.common.data.GameData;
import static minioning.common.data.GameData.getDt;
import minioning.common.data.Vector2D;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mogensen
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class CollisionProcessor implements IEntityProcessingService {
    
    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities, Entity entity) {
        UUID ID = entity.getID();

        //don't check immobile entities
//        if (!entity.isImmobile()) {
        if (entity.getType().equals(PLAYER)) { //until immobile is implemented
            System.out.println("checking collision");
//            System.out.println("checking collision for: " + entity.getName());
            for (Entry<UUID, Entity> entry : entities.entrySet()) {
                //don't check collision with itself
                if (entry.getKey() != ID) {
                    Entity entryEntity = entry.getValue();
                    //Only look in the same world
//                    System.out.println("checking if other entity is in same world");
                    if (entryEntity.getLocation().equals(entity.getLocation())) {
                        //check if colliding
//                        System.out.println("checking collision between: " + entity.getType().toString() + " & " + entryEntity.getType().toString());
//                        System.out.println("checking if other entity is colliding with the above");

                        if (colliding(entryEntity, entity)) {
                            System.out.println("colliding entry: " + entryEntity.getType());
                            //collision detected!
//                            System.out.println("collision detected!");
                            int x = entity.getX();
                            int y = entity.getY();
                            int entryx = entryEntity.getX();
                            int entryy = entryEntity.getY();
                            
//                            Vector2D collisionDirection = Vector2D.getDirection(x, entryx, y, entryy);
//                            Vector2D collisionVector = collisionDirection.times(length(x, y, entryx, entryy));
//                            System.out.println("collisionVector: " + collisionVector.getX() + "," + collisionVector.getY());
                            int nextx = entity.getNextx();
                            int nexty = entity.getNexty();
                            int nextEntryx = entryEntity.getX();
                            int nextEntryy = entryEntity.getY();
//                            Vector2D collisionDirection = Vector2D.getDirection(nextx, nextEntryx, nexty, nextEntryy);
                            Vector2D collisionDirection = Vector2D.getDirection(nextEntryx, nextx, nextEntryy, nexty);
//                            Vector2D collisionVector = collisionDirection.times(length(nextx, nexty, nextEntryx, nextEntryy));
                            
                            float nextxReal = entity.getNextxReal();
                            float nextyReal = entity.getNextyReal();
                            Vector2D velocity = entity.getVelocity();
                            Vector2D collisionVector = collisionDirection.times(Vector2D.length(velocity)).times(2);
                            velocity = velocity.plus(collisionVector);
//                            float speed = entity.getSpeed();
                            float elapsed = getDt();
                            nextxReal += velocity.getX() * elapsed;
                            nextyReal += velocity.getY() * elapsed;
                            nextx = Math.round(nextxReal);
                            nexty = Math.round(nextyReal);
                            entity.setNextx(nextx);
                            entity.setNexty(nexty);
                            entity.setNextxReal(nextx);
                            entity.setNextyReal(nexty);
//                            entity.setVelocity(entity.getVelocity().plus(collisionVector));
//                            entity.setNextVelocity(entity.getNextVelocity().plus(collisionVector));
                        } else {
//                            System.out.println("not collided!:");
//                            System.out.println("entity: " + entity.getX() + "," + entity.getY() + ", size: " + entity.getSize());
//                            System.out.println("entry : " + entryEntity.getX() + "," + entryEntity.getY() + ", size: " + entryEntity.getSize());
//                            System.out.println("---------------------");
                        }
                    }
                }
            }
        }
    }

    //THIS METHOD IS CALCULATING A WRONG X AND Y! vector might use a full dt, instead of elapsed?
    private boolean colliding(Entity entry, Entity entity) {
        //common data
//        float dt = GameData.getDt();
//        int bounds;

////        //current entity data
////        int x = entity.getX();
////        int y = entity.getY();
////        Vector2D velocity = entity.getVelocity();
////        float speed = entity.getSpeed();
////        float vx = x;
////        float vy = y;
////        vx += velocity.getX() * dt * speed;
////        vy += velocity.getY() * dt * speed;
////        float size = entity.getSize();
////
////        //current entry data
////        int entryx = entry.getX();
////        int entryy = entry.getY();
////        Vector2D entryVelocity = entry.getVelocity();
////        float entrySpeed = entry.getSpeed();
////        float entryvx = entryx;
////        float entryvy = entryy;
////        entryvx += entryVelocity.getX() * dt * entrySpeed;
////        entryvy += entryVelocity.getY() * dt * entrySpeed;
////        float entrySize = entry.getSize();
        int nextx = entity.getNextx();
        int nexty = entity.getNexty();
        float size = entity.getSize();
        int nextxEntry = entry.getNextx();
        int nextyEntry = entry.getNexty();
        float sizeEntry = entry.getSize();
        System.out.println("entity next: " + nextx + "," + nexty);
        System.out.println("entry  next: " + nextxEntry + "," + nextyEntry);

//        //checking vx is within bounds
//        float lowerx = entryvx - entrySize;
//        float upperx = entryvx + entrySize;
//        float lowery = entryvy - entrySize;
//        float uppery = entryvy + entrySize;
//        float entityLowerx = vy - size;
//        float entityUpperx = vy + size;
//        float entityLowery = vy - size;
//        float entityUppery = vy + size;
//        if (entityUpperx >= lowerx && entityLowerx <= upperx) {
////            System.out.println("x is within now!");
//            //checking vy is within bounds
//            if (entityUppery >= lowery && entityLowery <= uppery) {
////                System.out.println("y is within now!");
//                return true;
//            }
//        }
//        System.out.println("checking collision: " + vx + "," + vy + " ; " + entryvx + "," + entryvy);
//        return length(vx, vy, size, entryvx, entryvy, entrySize) < entity.getSize() + entry.getSize();
        return length(nextx, nexty, nextxEntry, nextyEntry) < size + sizeEntry;
    }
    
    private float length(float x1, float y1, float x2, float y2) {
        float length = 0;
        //sqrt(a^2+b^2)
        length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        System.out.println("Length: " + length);
        return length;
    }

//    //returns true if c1 and c2 collides
//    private boolean collide(Circle c1, Circle c2) {
//        double distanceX = c2.centerXProperty().getValue() - c1.centerXProperty().getValue();
//        double distanceY = c2.centerYProperty().getValue() - c1.centerYProperty().getValue();
//        double radiusSum = c2.getRadius() + c1.getRadius();
//        return distanceX * distanceX + distanceY * distanceY <= radiusSum * radiusSum;
//    }
//    private boolean collide(Shape s1, Shape s2) {
//        return s1.intersects(s2.getBoundsInLocal());
//    }
}
