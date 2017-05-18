package minioning.unit;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.EntityType;
import static minioning.common.data.EntityType.*;
import minioning.common.data.Event;
import static minioning.common.data.Events.*;
import minioning.common.data.Location;
import static minioning.common.data.Location.wilderness;
import minioning.common.data.Vector2D;
import minioning.common.services.IEntityCreatorService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Jakob and Mads
 */
@ServiceProvider(service = IEntityCreatorService.class)
public class UnitCreator implements IEntityCreatorService {

    /**
     *
     * @param eventBus The map of events yet to be acted upon
     * @param entities The map representing the world as entities
     */
    @Override
    public void createNew(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities) {
//        System.out.println(events.getBus().size());
//        System.out.println(EventBus.getInstance().size());
        for (Entry<UUID, Event> entry : eventBus.entrySet()) {
            UUID key = entry.getKey();
            Event event = entry.getValue();
            if (event.getType().equals(CREATEPLAYER)) {
                System.out.println("CREATEPLAYER found");
                createPlayer(event, entities);
                eventBus.remove(key);
            }
            if (event.getType().equals(SKILLQ)) {
                System.out.println("SKILLQ found");
                createSkill(event, entities);
                eventBus.remove(key);
            }
            if (event.getType().equals(ENEMYQ)) {
                System.out.println("ENEMYQ found");
                createEnemySkill(event, entities);
                eventBus.remove(key);
            }
            if (event.getType().equals(CREATEMONSTER)) {
                System.out.println("CREATEMONSTER found");
                createMonster(event, entities);
                eventBus.remove(key);
            }
            if (event.getType().equals(CREATEMINION)) {
                System.out.println("CREATEMINION found");
                createMinion(event, entities);
                eventBus.remove(key);
            }
            if (event.getType().equals(MINIONQ)) {
                System.out.println("MINIONQ found");
                createMinionSkill(event, entities);
                eventBus.remove(key);
            }
            if (event.getType().equals(MINIONSWAP)) {
                System.out.println("MINIONSWAP found");
                swapMinionType(event, entities);
                eventBus.remove(key);
            }
        }
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     * @param entities The map representing the world as entities
     */
    private void swapMinionType(Event event, Map<UUID, Entity> entities) {
        String[] data = event.getData();
        UUID playerID = UUID.fromString(data[2]);
        Entity player = entities.get(playerID);
        if (player.getLocation().equals(wilderness)) {
            boolean success = false;
            try {
                player.setMinionType(EntityType.valueOf(data[3]));
                success = true;
            } catch (Exception e) {
                System.out.println("incorrect minionType: " + data[3]);
            }
            //find and kill current minion so the new one can spawn
            if (success) {
                for (Map.Entry<UUID, Entity> entry : entities.entrySet()) {
                    UUID ID = entry.getKey();
                    Entity entity = entry.getValue();
                    if (entity.getType().equals(MINION) && entity.getOwner().equals(playerID)) {
                        entities.remove(ID);
                    }
                }
            }

        }
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     * @param entities The map representing the world as entities
     */
    private void createMinionSkill(Event event, Map<UUID, Entity> entities) {
        String[] data = event.getData();
        UUID minionID = UUID.fromString(data[2]);
        Entity minion = entities.get(minionID);
        try {
            if (minion.getSkillqCurrentCD() >= minion.getSkillqCD()) {
                Entity minionSkill = new Entity(minion.getID(), "", minion.getX(), minion.getY());
                minionSkill.setLocation(minion.getLocation());
                minionSkill.setType(EntityType.HOLYBOLT);
                minionSkill.setSpeed(200);
                Vector2D direction = Vector2D.getDirection(minionSkill.getX(), Integer.parseInt(data[4]), minionSkill.getY(), Integer.parseInt(data[5]));
                Vector2D velocity = direction.times(minionSkill.getSpeed());
                minionSkill.setVelocity(velocity);
                entities.putIfAbsent(minionSkill.getID(), minionSkill);
                minion.setSkillqCurrentCD(0);
            }
        } catch (Exception e) {
        }
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     * @param entities The map representing the world as entities
     */
    private void createEnemySkill(Event event, Map<UUID, Entity> entities) {
        String[] data = event.getData();
        UUID enemyID = UUID.fromString(data[2]);
        Entity enemy = entities.get(enemyID);
        try {
            if (enemy.getSkillqCurrentCD() >= enemy.getSkillqCD()) {
                Entity enemySkill = new Entity(enemy.getID(), "", enemy.getX(), enemy.getY());
                enemySkill.setLocation(enemy.getLocation());
                enemySkill.setType(EntityType.HOLYBOLT);
                enemySkill.setSpeed(200);
                Vector2D direction = Vector2D.getDirection(enemySkill.getX(), Integer.parseInt(data[4]), enemySkill.getY(), Integer.parseInt(data[5]));
                Vector2D velocity = direction.times(enemySkill.getSpeed());
                enemySkill.setVelocity(velocity);
                entities.putIfAbsent(enemySkill.getID(), enemySkill);
                enemy.setSkillqCurrentCD(0);
            }
        } catch (Exception e) {
        }

    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     * @param entities The map representing the world as entities
     */
    private void createSkill(Event event, Map<UUID, Entity> entities) {
        String[] data = event.getData();
        UUID owner = UUID.fromString(data[2]);
        Entity ownerEntity = getOwnerEntity(owner, entities);
        try {
            if (ownerEntity.getSkillqCurrentCD() >= ownerEntity.getSkillqCD()) {
                Entity skillEntity = new Entity(ownerEntity.getID(), "", ownerEntity.getX(), ownerEntity.getY());
                skillEntity.setLocation(ownerEntity.getLocation());
                skillEntity.setType(EntityType.HOLYBOLT);
                skillEntity.setSpeed(200);
                Vector2D direction = Vector2D.getDirection(skillEntity.getX(), Integer.parseInt(data[4]), skillEntity.getY(), Integer.parseInt(data[5].trim()));
                Vector2D velocity = direction.times(skillEntity.getSpeed());
                skillEntity.setVelocity(velocity);
                entities.putIfAbsent(skillEntity.getID(), skillEntity);
                ownerEntity.setSkillqCurrentCD(0);
            }
        } catch (Exception e) {
        }

    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     * @param entities The map representing the world as entities
     */
    private void createPlayer(Event event, Map<UUID, Entity> entities) {
        String[] data = event.getData();
        for (String data1 : data) {
            System.out.println(data1);
        }
        UUID owner = UUID.fromString(data[2]);
        String name = data[4];
        Entity newEntity = new Entity(owner, name);
        newEntity.setLocation(Location.wilderness);
        newEntity.setMinionType(MINIONMAGE);
        entities.put(newEntity.getID(), newEntity);
        System.out.println("Player created!");
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     * @param entities The map representing the world as entities
     */
    private void createMonster(Event event, Map<UUID, Entity> entities) {
        String[] data = event.getData();
        UUID owner = UUID.fromString(data[0]);
        String name = data[1];
        int x = Integer.parseInt(data[2]);
        int y = Integer.parseInt(data[3]);
        int dx = Integer.parseInt(data[4]);
        int dy = Integer.parseInt(data[5]);
        Entity newMonster = new Entity(owner, name, x, y);
        newMonster.setDx(x);//dx
        newMonster.setDy(y);//dy
        newMonster.setLocation(Location.valueOf(data[6]));
        newMonster.setType(ENEMY);
        newMonster.setSkillqCD(1.5f);
        entities.putIfAbsent(newMonster.getID(), newMonster);
        System.out.println("new monster created");
    }

    /**
     *
     * @param event An object containing an eventType enum and a String array of
     * data
     * @param entities The map representing the world as entities
     */
    private void createMinion(Event event, Map<UUID, Entity> entities) {
        String[] data = event.getData();
        UUID owner = UUID.fromString(data[0]);
        String name = data[1];
        int x = Integer.parseInt(data[2]);
        int y = Integer.parseInt(data[3]);
        Entity newMinion = new Entity(owner, name, x, y);
        newMinion.setDx(x);
        newMinion.setDy(y);
        newMinion.setLocation(Location.valueOf(data[4]));
        newMinion.setType(MINION);
        newMinion.setMinionType(EntityType.valueOf(data[5]));
        entities.putIfAbsent(newMinion.getID(), newMinion);
    }

    /**
     *
     * @param ownerID UUID representing the owner of the entity
     * @param world The map representing the world as entities
     * @return
     */
    private Entity getOwnerEntity(UUID ownerID, Map<UUID, Entity> world) {
        Entity entity;
        for (Entry<UUID, Entity> entry : world.entrySet()) {
            UUID key = entry.getKey();
            entity = entry.getValue();
            System.out.println("checking for owner in getOwnerEntity");
            if (entity.getOwner().toString().equals(ownerID.toString())) {
                System.out.println("owner found!");
                return entity;
            }
        }
        return null;
    }
}
