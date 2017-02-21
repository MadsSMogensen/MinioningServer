package minioning.common.data;

import java.util.UUID;

/**
 *
 * @author Mogensen
 */
public class Entity {

    private final UUID ID = UUID.randomUUID();
    private String name;
    private float x = 0;
    private float y = 0;
    private float dx;
    private float dy;
    private float speed;

    public Entity(String name) {
        this.name = name;
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
