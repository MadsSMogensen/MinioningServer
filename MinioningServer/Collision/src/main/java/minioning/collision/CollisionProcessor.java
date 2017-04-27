package minioning.collision;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import minioning.common.data.Entity;
import static minioning.common.data.EntityType.*;
import static minioning.common.data.EntityType.PLAYER;
import minioning.common.data.Event;
import minioning.common.data.EventBus;
import minioning.common.data.Events;
import static minioning.common.data.Events.HPCHANGE;
import minioning.common.data.GameData;
import static minioning.common.data.GameData.getDt;
import minioning.common.data.Location;
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
        try {
            //don't check immobile entities
            if (!entity.isImmobile()) {
//        if (entity.getType().equals(PLAYER)) { //until immobile is implemented
                for (Entry<UUID, Entity> entry : entities.entrySet()) {
                    Entity entryEntity = entry.getValue();
                    //Only look in the same world
                    if (entryEntity.getLocation().equals(entity.getLocation())) {
                        //don't check collision with itself
                        if (entry.getKey() != ID) {
                            if (entity.getType().equals(HOLYBOLT)) {
                                //handle collision for holybolts
                                //don't collide with owner
                                if (!entity.getOwner().toString().equals(entryEntity.getID().toString())) {
                                    //don't collide with owners owner (minion to player etc.)
                                    if (!entities.get(entity.getOwner()).getOwner().toString().equals(entryEntity.getOwner().toString())) {
                                        if (!entities.get(entity.getOwner()).getOwner().toString().equals(entryEntity.getID().toString())) {
                                            //don't collide with your owners minions
                                            if (!entryEntity.getOwner().toString().endsWith(entity.getOwner().toString())) {
                                                //don't collide with other holybolts
                                                if (!entryEntity.getType().equals(entity.getType())) {
                                                    //collision event for holy bolt here!
                                                    if (colliding(entryEntity, entity)) {

                                                        String[] s = new String[2];

                                                        s[0] = entry.getValue().getID().toString();
                                                        s[1] = entity.getType().toString();

                                                        UUID id = UUID.randomUUID();
                                                        Event event = new Event(HPCHANGE, s);
                                                        entities.remove(ID);
                                                        eventBus.put(id, event);

                                                        System.out.println("event");
                                                        System.out.println("A HOLYBOLT COLLIDED!");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                //don't collide with owner
                            } else if (!entryEntity.getOwner().toString().equals(entity.getID().toString())) {
                                //check if colliding
                                if (colliding(entryEntity, entity)) {
                                    switch (entryEntity.getType()) {
                                        case DOOR:
                                            if (entryEntity.getDoorTo() == null) {

                                                regularCollision(entryEntity, entity);

                                                break;
                                            }
                                            if (!entity.getType().equals(HOLYBOLT)) {

                                                int x = entryEntity.getDoorToX();
                                                int y = entryEntity.getDoorToY();

                                                entity.setPosition(x, y, entryEntity.getDoorTo());

                                            }
                                            break;
                                        case HOLYBOLT:
                                            //handled by holybolt specific collision
                                            break;
                                        case MINION:
                                            //not colliding
                                            break;
                                        default:
                                            regularCollision(entryEntity, entity);
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

//                        if (entity.getType().equals(LAVA)) {
//                            if (colliding(entryEntity, entity)) {
//
//                            }
//                            String[] s = new String[2];
//
//                            s[0] = entry.getValue().getID().toString();
//                            s[1] = entity.getType().toString();
//
//                            UUID id = UUID.randomUUID();
//                            Event event = new Event(HPCHANGE, s);
////                            entities.remove(ID);
//                            eventBus.put(id, event);
//
//                        }
    private void regularCollision(Entity entryEntity, Entity entity) {
        //collision detected!
        int x = entity.getX();
        int y = entity.getY();
        int nextx = entity.getNextx();
        int nexty = entity.getNexty();
        int nextEntryx = entryEntity.getX();
        int nextEntryy = entryEntity.getY();
        Vector2D collisionDirection = Vector2D.getDirection(nextEntryx, nextx, nextEntryy, nexty);

        float nextxReal = entity.getNextxReal();
        float nextyReal = entity.getNextyReal();
        Vector2D velocity = entity.getVelocity();
        Vector2D collisionVector = collisionDirection.times(Vector2D.length(velocity)).times(2);
        velocity = velocity.plus(collisionVector);
        float elapsed = getDt();
        nextxReal += velocity.getX() * elapsed;
        nextyReal += velocity.getY() * elapsed;
        nextx = Math.round(nextxReal);
        nexty = Math.round(nextyReal);
        entity.setNextx(nextx);
        entity.setNexty(nexty);
        entity.setNextxReal(nextx);
        entity.setNextyReal(nexty);
    }

    private boolean colliding(Entity entry, Entity entity) {
        int nextx = entity.getNextx();
        int nexty = entity.getNexty();
        float size = entity.getSize();
        int nextxEntry = entry.getNextx();
        int nextyEntry = entry.getNexty();
        float sizeEntry = entry.getSize();

        return length(nextx, nexty, nextxEntry, nextyEntry) < size + sizeEntry;
    }

    private float length(float x1, float y1, float x2, float y2) {
        float length = 0;
        //sqrt(a^2+b^2)
        length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        return length;
    }
}
