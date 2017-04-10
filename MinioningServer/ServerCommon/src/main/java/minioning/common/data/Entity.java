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
    private Position position = new Position(100, 100);
    private Vector2D velocity = new Vector2D();
    private int dx;
    private int dy;
    private int destinationX;
    private int destinationY;
    private float xSpeed;
    private float ySpeed;
    private float cSpeed = 50;
    private float mSpeed = 5;
    private float direction = 45;
    private float radians;
    private EntityType type = PLAYER;
    private float deacceleration = 1;
    private Location location;
    private Location doorTo;

    public Entity(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        this.ID = UUID.randomUUID();
        this.dx = position.getX();
        this.dy = position.getY();
        this.destinationX = position.getX();
        this.destinationY = position.getY();
    }

    public Location getDoorTo() {
        return doorTo;
    }

    public void setDoorTo(Location doorTo) {
        this.doorTo = doorTo;
    }

    public float getcSpeed() {
        return cSpeed;
    }

    public void setcSpeed(float cSpeed) {
        this.cSpeed = cSpeed;
    }

    public float getmSpeed() {
        return mSpeed;
    }

    public void setmSpeed(float mSpeed) {
        this.mSpeed = mSpeed;
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
    
    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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

    public float getRadians() {
        return radians;
    }

    public void setRadians(float radians) {
        this.radians = radians;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public void setDestinationX(int destinationX) {
        this.destinationX = destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(int destinationY) {
        this.destinationY = destinationY;
    }
    
    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public Circle getBounds() {
        return new Circle(getX(), getY(), 12);
    }

    public float getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(float xSpeed) {
        this.xSpeed = xSpeed;
    }

    public float getySpeed() {
        return ySpeed;
    }

    public void setySpeed(float ySpeed) {
        this.ySpeed = ySpeed;
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
        entityString += xSpeed + ";";
        entityString += ySpeed + ";";
        entityString += cSpeed + ";";
        entityString += direction;
        return entityString;
    }
    
    public String toClients(){
        String entityString;
        entityString = ID + ";";
        entityString += type.toString() + ";";
        entityString += getX() + ";";
        entityString += getY() + ";";
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
        return position.getX();
    }

    public void setX(int x) {
        position.setX(x);
    }

    public int getY() {
        return position.getY();
    }

    public void setY(int y) {
        position.setY(y);
    }

    public float getCSpeed() {
        return cSpeed;
    }

    public void setCSpeed(float speed) {
        this.cSpeed = speed;
    }
    public float getMSpeed(){
        return mSpeed;
    }

    public UUID getID() {
        return ID;
    }

}
