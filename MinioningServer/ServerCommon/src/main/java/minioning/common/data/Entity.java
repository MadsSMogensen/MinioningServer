package minioning.common.data;

import java.io.Serializable;
import java.util.UUID;
import javafx.scene.shape.Circle;
import static minioning.common.data.EntityType.PLAYER;

/**
 * 
 * @author Jakob and Mads
 */
public class Entity implements Serializable {

    private final UUID ID;
    private UUID owner;
    private String name;
    private int x = 100;
    private int y = 100;
    private float xReal = x;
    private float yReal = y;
    private int nextx = x;
    private int nexty = y;
    private float nextxReal = x;
    private float nextyReal = y;
    private Vector2D velocity = new Vector2D();
    private boolean immobile;
    private float size = 14;
    private float speed = 50;
    private int dx;
    private int dy;
    private Vector2D direction;
    private EntityType type = PLAYER;
    private Location location;
    private Location doorTo;
    private int SpawnCount = 0;
    private int maxMinions = 1;
    private float spawnTimer;
    private float minionSpawnTime = 10;
    private EntityType minionType;
    private int hp = 100;
    private float skillqCD = 0.75f;
    private float skillqCurrentCD = 0;
    private int doorToX;
    private int doorToY;
/**
 * 
 * @param owner UUID of the owner to the entity
 * @param name String representing the name of the entity
 */
    public Entity(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        this.ID = UUID.randomUUID();
        this.dx = this.x;
        this.dy = this.y;
    }
/**
 * 
 * @param owner UUID of the owner to the entity
 * @param name String representing the name of the entity
 * @param x the x coordinate representing the position of the entity
 * @param y the y coordinate representing the position of the entity
 */
    public Entity(UUID owner, String name, int x, int y) {
        this.owner = owner;
        this.name = name;
        this.ID = UUID.randomUUID();
        this.x = x;
        this.y = y;
        this.xReal = x;
        this.yReal = y;
        this.nextx = x;
        this.nexty = y;
        this.nextxReal = xReal;
        this.nextyReal = yReal;
        this.dx = x;
        this.dy = y;
    }
/**
 * 
 * @return a String representing the entity
 */
    @Override
    public String toString() {
        String entityString;
        entityString = ID + ";";
        entityString += owner + ";";
        entityString += name + ";";
        entityString += getX() + ";";
        entityString += getY() + ";";
        entityString += dx + ";";
        entityString += dy + ";";
        entityString += direction;
        return entityString;
    }
/**
 * 
 * @return a String representing the entity, in a manner readable to the clients
 */
    public String toClients() {
        String entityString;
        entityString = ID + ";";
        entityString += type.toString() + ";";
        entityString += name + ";";
        entityString += x + ";";
        entityString += y + ";";
        entityString += velocity.getX() + ";";
        entityString += velocity.getY() + ";";
        entityString += getOwner().toString() + ";";
        entityString += getLocation() + ";";
        entityString += getDoorTo() + ";";
        entityString += getHp() + ";";
        return entityString;
    }
/**
 * 
 * @param x the new x coordinate
 * @param y the new y coordinate
 * @param location the new enum area
 */
    public void setPosition(int x, int y, Location location) {
        this.x = x;
        this.y = y;
        this.location = location;
        this.xReal = x;
        this.yReal = y;
        this.nextx = x;
        this.nexty = y;
        this.nextxReal = x;
        this.nextyReal = y;
        this.velocity = new Vector2D();
        this.dx = x;
        this.dy = y;
    }

    public float getMinionSpawnTime() {
        return minionSpawnTime;
    }

    public void setMinionSpawnTime(float minionSpawnTime) {
        this.minionSpawnTime = minionSpawnTime;
    }

    public float getSkillqCD() {
        return skillqCD;
    }

    public void setSkillqCD(float skillqCD) {
        this.skillqCD = skillqCD;
    }

    public float getSkillqCurrentCD() {
        return skillqCurrentCD;
    }

    public void setSkillqCurrentCD(float skillqCurrentCD) {
        this.skillqCurrentCD = skillqCurrentCD;
    }

    public int getDoorToX() {
        return doorToX;
    }

    public void setDoorToX(int doorToX) {
        this.doorToX = doorToX;
    }

    public int getDoorToY() {
        return doorToY;
    }

    public void setDoorToY(int doorToY) {
        this.doorToY = doorToY;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public float getSpawnTimer() {
        return spawnTimer;
    }

    public EntityType getMinionType() {
        return minionType;
    }

    public void setMinionType(EntityType minionType) {
        this.minionType = minionType;
    }

    public void setSpawnTimer(float spawnTimer) {
        this.spawnTimer = spawnTimer;
    }

    public int getSpawnCount() {
        return SpawnCount;
    }

    public void setSpawnCount(int SpawnCount) {
        this.SpawnCount = SpawnCount;
    }

    public int getMaxMinions() {
        return maxMinions;
    }

    public void setMaxMinions(int maxMinions) {
        this.maxMinions = maxMinions;
    }

    public float getSpeed() {
        return speed;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public boolean isImmobile() {
        return immobile;
    }

    public int getNextx() {
        return nextx;
    }

    public void setNextx(int nextx) {
        this.nextx = nextx;
    }

    public int getNexty() {
        return nexty;
    }

    public void setNexty(int nexty) {
        this.nexty = nexty;
    }

    public float getNextxReal() {
        return nextxReal;
    }

    public void setNextxReal(float nextxReal) {
        this.nextxReal = nextxReal;
    }

    public float getNextyReal() {
        return nextyReal;
    }

    public void setNextyReal(float nextyReal) {
        this.nextyReal = nextyReal;
    }

    public void setImmobile(boolean immobile) {
        this.immobile = immobile;
    }

    public float getxReal() {
        return xReal;
    }

    public void setxReal(float xReal) {
        this.xReal = xReal;
    }

    public float getyReal() {
        return yReal;
    }

    public void setyReal(float yReal) {
        this.yReal = yReal;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Vector2D getDirection() {
        return direction;
    }

    public void setDirection(Vector2D direction) {
        this.direction = direction;
    }

    public Location getDoorTo() {
        return doorTo;
    }

    public void setDoorTo(Location doorTo) {
        this.doorTo = doorTo;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D newVector) {
        this.velocity = newVector;
    }

    public Circle getBounds() {
        return new Circle(getX(), getY(), 12);
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public int getDx() {
        return dx;
    }

    public void setDx(int dx) {
        this.dx = dx;
    }

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public UUID getID() {
        return ID;
    }
}
