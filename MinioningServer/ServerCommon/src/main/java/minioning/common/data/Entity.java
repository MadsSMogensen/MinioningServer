package minioning.common.data;

import java.io.Serializable;
import java.util.UUID;
import javafx.scene.shape.Circle;
import static minioning.common.data.EntityType.PLAYER;

/**
 *
 * @author Mogensen
 */
public class Entity implements Serializable {

    private final UUID ID;
    private UUID owner;
    private String name;
    private int x = 100;
    private int y = 100;
    private float xReal = 100;
    private float yReal = 100;
    private Vector2D velocity = new Vector2D();
    private float speed = 50;
    private int dx;
    private int dy;
    private Vector2D direction;
    private EntityType type = PLAYER;
    private float deacceleration = 1;
    private Location location;
    private Location doorTo;
    private int SpawnCount = 0;

    public int getSpawnCount() {
        return SpawnCount;
    }

    public void setSpawnCount(int SpawnCount) {
        this.SpawnCount = SpawnCount;
    }

    public Entity(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        this.ID = UUID.randomUUID();
        this.dx = this.x;
        this.dy = this.y;
    }

    public float getSpeed() {
        return speed;
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

    public float getDeacceleration() {
        return deacceleration;
    }

    public void setDeacceleration(float deacceleration) {
        this.deacceleration = deacceleration;
    }
    
    public Vector2D getVelocity(){
        return velocity;
    }
    
    public void setVelocity(Vector2D newVector){
        this.velocity = newVector;
    }

    public Circle getBounds() {
        return new Circle(getX(), getY(), 12);
    }

    @Override
    public String toString() {

        String entityString;
        entityString  = ID + ";";
        entityString += owner + ";";
        entityString += name + ";";
        entityString += getX() + ";";
        entityString += getY() + ";";
        entityString += dx + ";";
        entityString += dy + ";";
        entityString += direction;
        return entityString;
    }
    
//    public String toClients(){
//        String entityString;
//        entityString = ID + ";";
//        entityString += type.toString() + ";";
//        entityString += getX() + ";";
//        entityString += getY() + ";";
//        entityString += getOwner().toString() + ";";
//        entityString += getLocation() + ";";
//        entityString += getDoorTo() + ";";
//        return entityString;
//    }
    public String toClients(){
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
        entityString += getType()+ ";";
        return entityString;
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
