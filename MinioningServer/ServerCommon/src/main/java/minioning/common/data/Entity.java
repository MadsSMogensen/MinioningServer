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

    private final UUID ID = UUID.randomUUID();
    private UUID owner;
    private String name;
    private float x = 0;
    private float y = 0;
    private float dx;
    private float dy;
    private float xSpeed;
    private float ySpeed;
    private float speed = 1;
    private float direction = 45;
    private EntityType type = PLAYER;

    public Entity(UUID owner, String name) {
        this.owner = owner;
        this.name = name;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public Circle getBounds() {
        return new Circle(x, y, 5);
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
        entityString += speed + ";";
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

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public UUID getID() {
        return ID;
    }

}
