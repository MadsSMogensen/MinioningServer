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
    private int dx;
    private int dy;
    private int destinationX;
    private int destinationY;
    private float xSpeed;
    private float ySpeed;
    private float cSpeed = 5;
    private float mSpeed = 5;
    private float direction = 45;
    private float radians;
    private EntityType type = PLAYER;

    public Entity(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
        this.ID = UUID.randomUUID();
        this.dx = x;
        this.dy = y;
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
        return new Circle(x, y, 12);
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
        entityString += x + ";";
        entityString += y + ";";
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
        entityString += x + ";";
        entityString += y + ";";
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
