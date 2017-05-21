package minioning.collision;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import static minioning.common.data.EntityType.*;
import minioning.common.data.Event;
import static minioning.common.data.Events.HPCHANGE;
import static minioning.common.data.GameData.getDt;
import minioning.common.data.Vector2D;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jakob and Mads
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class CollisionProcessor implements IEntityProcessingService {

    /**
     *
     * @param eventBus The map of events yet to be acted upon
     * @param entities The map representing the world as entities
     * @param entity The current entity being processed
     */
    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities, Entity entity) {
        UUID ID = entity.getID();
        try {
            //don't check immobile entities
            if (!entity.isImmobile()) {
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

    /**
     *
     * @param entryEntity The entity object for collision check
     * @param entity The current entity being processed
     */
    public void regularCollision(Entity entryEntity, Entity entity) {
        //collision detected!
        int x = entity.getX();
        int y = entity.getY();
        int nextx = entity.getNextx();
        int nexty = entity.getNexty();
        int nextEntryx = entryEntity.getX();
        int nextEntryy = entryEntity.getY();
        Vector2D collisionDirection = Vector2D.getDirection(nextEntryx, nextx, nextEntryy, nexty);
        System.out.println("cd: " + collisionDirection.getX());
        float nextxReal = entity.getNextxReal();
        float nextyReal = entity.getNextyReal();
        Vector2D velocity = entity.getVelocity();
        Vector2D collisionVector = collisionDirection.times(Vector2D.length(velocity)).times(2);
        velocity = velocity.plus(collisionVector);
        System.out.println(velocity.getX());
        float elapsed = getDt();
        System.out.println(nextxReal);
        nextxReal += velocity.getX() * elapsed;
        nextyReal += velocity.getY() * elapsed;
        nextx = Math.round(nextxReal);
        nexty = Math.round(nextyReal);
        entity.setNextx(nextx);
        entity.setNexty(nexty);
        entity.setNextxReal(nextx);
        entity.setNextyReal(nexty);
        
    }

    /**
     *
     * @param entry The entity object for collision check
     * @param entity The current entity being processed
     * @return true if the entities are colliding
     */
    public boolean colliding(Entity entry, Entity entity) {
        int nextx = entity.getNextx();
        int nexty = entity.getNexty();
        float size = entity.getSize();
        int nextxEntry = entry.getNextx();
        int nextyEntry = entry.getNexty();
        float sizeEntry = entry.getSize();
        
        return length(nextx, nexty, nextxEntry, nextyEntry) < size + sizeEntry;
    }

    /**
     *
     * @param x1 the x of coordinate 1
     * @param y1 the y of coordinate 1
     * @param x2 the x of coordinate 2
     * @param y2 the y of coordinate 2
     * @return
     */
    public float length(float x1, float y1, float x2, float y2) {
        float length;
        //sqrt(a^2+b^2)
        length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        return length;
    }
}
