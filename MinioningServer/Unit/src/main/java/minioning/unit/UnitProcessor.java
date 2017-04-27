package minioning.unit;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.EntityType;
import static minioning.common.data.EntityType.*;
import minioning.common.data.Event;
import static minioning.common.data.Events.*;
import minioning.common.data.GameData;
import static minioning.common.data.Location.wilderness;
import minioning.common.data.Vector2D;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Helle
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class UnitProcessor implements IEntityProcessingService {

    float standardSpawnDelay = 10;

    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities, Entity entity) {
        //add cd timer to every entity
        if (entity.getSkillqCurrentCD() < 100 && !entity.isImmobile()) {
            entity.setSkillqCurrentCD(entity.getSkillqCurrentCD() + GameData.getDt());
        }

        //check monster spawners, if they should spawn new monsters
        if (entity.getType().equals(PORTAL)) {
            if (entity.getSpawnCount() < entity.getMaxMinions()) {
                float spawnTimer = entity.getSpawnTimer();
                spawnTimer += GameData.getDt();
                entity.setSpawnTimer(spawnTimer);
                if (spawnTimer >= standardSpawnDelay) {
                    spawnNewMonster(entity, eventBus);
                    entity.setSpawnCount(entity.getSpawnCount() + 1);
                    entity.setSpawnTimer(0);
                }
            }
        }
        //handle enemies
        if (entity.getType().equals(ENEMY)) {
            //update skillcooldown
            entity.setSkillqCurrentCD(entity.getSkillqCurrentCD() + GameData.getDt());

            int x = entity.getX();
            int y = entity.getY();
            //looking for players to shoot at
            for (Map.Entry<UUID, Entity> entry : entities.entrySet()) {
                Entity entryEntity = entry.getValue();
                if (entryEntity.getType().equals(PLAYER)) {
                    int entryx = entryEntity.getX();
                    int entryy = entryEntity.getY();
                    //checking if player is within a certain view distance
                    if (distance(x, y, entryx, entryy) <= 300) {
                        //Shooting at player position
                        String[] data = new String[6];
                        data[0] = "";
                        data[1] = "";
                        data[2] = entity.getID().toString();
                        System.out.println("entityID: " + entity.getID().toString());
                        data[3] = "ENEMYQ";
                        data[4] = String.valueOf(entryx);
                        data[5] = String.valueOf(entryy);
                        Event shootQ = new Event(ENEMYQ, data);
                        eventBus.put(UUID.randomUUID(), shootQ);
                    }
                }
            }
            //Set movement
//            if (entity.getX() == entity.getDx() && entity.getY() == entity.getDy()) {
            if (Math.abs(entity.getX() - entity.getDx()) <= 35 && Math.abs(entity.getY() - entity.getDy()) <= 35) {
                Entity owner = entities.get(entity.getOwner());
                int lowerx = owner.getX() - 200;
                int upperx = owner.getX() + 200;
                int lowery = owner.getY() - 200;
                int uppery = owner.getY() + 200;
                Random rnd = new Random();
                int newdx = rnd.nextInt((upperx - lowerx) + 1) + lowerx;
                newdx = Math.min(newdx, GameData.getGameWidth() - 10);
                int newdy = rnd.nextInt((uppery - lowery) + 1) + lowery;
                newdy = Math.min(newdy, GameData.getGameHeight() - 10);

                entity.setDx(newdx);
                entity.setDy(newdy);
            }
        }

        //spawn minions
        if (entity.getType().equals(PLAYER)) {
            if (entity.getSpawnCount() < entity.getMaxMinions()) {
                entity.setSpawnTimer(entity.getSpawnTimer() + GameData.getDt());
                if (entity.getSpawnTimer() >= entity.getMinionSpawnTime()) {
                    spawnNewMinion(entity, eventBus);
                    entity.setSpawnCount(entity.getSpawnCount() + 1);
                    entity.setSpawnTimer(0);
                }
            }
        }
        //handle minions
        if (entity.getType().equals(MINION)) {
            //handle movement
            Entity owner = entities.get(entity.getOwner());
            Vector2D ownerVelocity = owner.getVelocity();
            int dx = Math.round(owner.getX() + (-ownerVelocity.getX() * GameData.getDt()));
            int dy = Math.round(owner.getY() + (-ownerVelocity.getY() * GameData.getDt()));
            entity.setDx(dx);
            entity.setDy(dy);
            //look for targets
            int x = entity.getX();
            int y = entity.getY();
            switch (entity.getMinionType()) {
                case MINIONMAGE:
                    for (Map.Entry<UUID, Entity> entry : entities.entrySet()) {
                        Entity entryEntity = entry.getValue();
                        if (entryEntity.getLocation().equals(entity.getLocation())) {
                            if (!entity.getLocation().equals(wilderness)) {
                                if (!entity.getOwner().equals(entryEntity.getID())) {
                                    minionShootQ(entity, entryEntity, eventBus);
                                }
                            } else if (entryEntity.getType().equals(ENEMY)) {
                                minionShootQ(entity, entryEntity, eventBus);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        //handle entities outside the gameworld
        if (entity.getX() > GameData.getGameWidth() + 5 || entity.getY() > GameData.getGameHeight() + 5) {
            switch (entity.getType()) {
                case ENEMY:
                    Entity owner = entities.get(entity.getOwner());
                    entity.setDx(owner.getX() - 10);
                    entity.setDy(owner.getY() - 10);
                    break;
                case PLAYER:
                    entity.setX(Math.round(entity.getVelocity().getX() * -GameData.getDt()));
                    entity.setY(Math.round(entity.getVelocity().getY() * -GameData.getDt()));
                    break;
                default:
                    entities.remove(entity.getID());
                    System.out.println("entity out of bounds removed");
                    break;
            }
        }
    }

    private void minionShootQ(Entity entity, Entity entryEntity, ConcurrentHashMap<UUID, Event> eventBus) {
        int entryx = entryEntity.getX();
        int entryy = entryEntity.getY();
        int x = entity.getX();
        int y = entity.getY();
        //checking if enemy is within a certain view distance
        if (distance(x, y, entryx, entryy) <= 300) {
            //Shooting at player position
            String[] data = new String[6];
            data[0] = "";
            data[1] = "";
            data[2] = entity.getID().toString();
            data[3] = "MINIONQ";
            data[4] = String.valueOf(entryx);
            data[5] = String.valueOf(entryy);
            Event shootQ = new Event(MINIONQ, data);
            eventBus.put(UUID.randomUUID(), shootQ);
        }
    }

    private void spawnNewMinion(Entity owner, ConcurrentHashMap<UUID, Event> eventBus) {
        String[] data = new String[6];
        data[0] = owner.getID().toString();//owner id
        data[1] = "";//name
        data[2] = String.valueOf(owner.getX());//x
        data[3] = String.valueOf(owner.getY());//y
        data[4] = owner.getLocation().toString();//Location
        data[5] = owner.getMinionType().toString();//minionType
        Event newEvent = new Event(CREATEMINION, data);
        eventBus.putIfAbsent(UUID.randomUUID(), newEvent);
    }

    private void spawnNewMonster(Entity owner, ConcurrentHashMap<UUID, Event> eventBus) {
        String data = "";
        data += owner.getID().toString() + ";";
//        data += owner.getMinionType().toString() + ";";
        data += "ENEMY" + ";";
        data += owner.getX() + ";";
        data += owner.getY() + ";";
        data += owner.getDx() + ";";
        data += owner.getDy() + ";";
        data += owner.getLocation();
        String[] dataArray = data.split(";");
        Event newEvent = new Event(CREATEMONSTER, dataArray);
        eventBus.putIfAbsent(UUID.randomUUID(), newEvent);
    }

    private float distance(int x1, int y1, int x2, int y2) {
        float length = 0;
        //sqrt(a^2+b^2)
        length = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        return length;
    }
}
