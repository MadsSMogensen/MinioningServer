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
    private int nextx = 100;
    private int nexty = 100;
    private float nextxReal = 100;
    private float nextyReal = 100;
    private Vector2D velocity = new Vector2D();
    private Vector2D nextVelocity = new Vector2D();
    private boolean collisionChecked = true;
    private boolean hasMoved = false;
    private boolean immobile;
    private float size = 10;
    private float speed = 50;
    private int dx;
    private int dy;
    private int previousDx;
    private int previousDy;
    private Vector2D direction;
    private EntityType type = PLAYER;
    private float deacceleration = 1;
    private Location location;
    private Location doorTo;

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

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public boolean isImmobile() {
        return immobile;
    }

    public void ignoreCollision(){
        collisionChecked = true;
    }

    public int getPreviousDx() {
        return previousDx;
    }

    public void setPreviousDx(int previousDx) {
        this.previousDx = previousDx;
    }

    public int getPreviousDy() {
        return previousDy;
    }

    public void setPreviousDy(int previousDy) {
        this.previousDy = previousDy;
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

    public boolean isCollisionChecked() {
        return collisionChecked;
    }

    public void setCollisionChecked(boolean collisionChecked) {
        this.collisionChecked = collisionChecked;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    
    public void hasMoved(){
        this.hasMoved = true;
    }
    
    public void setImmobile(boolean immobile) {
        this.immobile = immobile;
    }
    
    public void checkCollision(){
        this.collisionChecked = false;
    }

    public Vector2D getNextVelocity() {
        return nextVelocity;
    }

    public void setNextVelocity(Vector2D nextVelocity) {
        this.nextVelocity = nextVelocity;
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
