package minioning.common.data;

/**
 *
 * @author Mogensen
 */
public class Vector2D {

    private float x; //the magnitude on the x-axis
    private float y; //the magnitude on the y-axis
    private float direction; //the direction of the vector
    private float magnitude; //the length of the vector

    /**
     * constructor, constructing a 0-vector
     */
    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Constructor for a specific vector
     *
     * @param x vector x coordinate
     * @param y vector y coordinate
     */
    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return a directiononal unit vector
     */
    public float getDirection() {
        calculateDirection();
        return direction;
    }

    /**
     *
     * @param x1 x of coordinate 1
     * @param x2 x of coordinate 2
     * @param y1 y of coordinate 1
     * @param y2 y of coordinate 2
     * @return a directional unit vector
     */
    public static Vector2D getDirection(int x1, int x2, int y1, int y2) {
        float vx = x2 - x1;
        float vy = y2 - y1;
        Vector2D direction = normalize(new Vector2D(vx, vy));
        return direction;
    }

    /**
     * calculates the directional unit vector
     */
    private void calculateDirection() {
        this.direction = (float) Math.asin(x / getMagnitude());
    }

    /**
     *
     * @return the length of the vector
     */
    public float getMagnitude() {
        calculateMagnitude();
        return magnitude;
    }

    /**
     * Calculates the length of the vector
     */
    private void calculateMagnitude() {
        this.magnitude = (float) Math.sqrt((x * x + y * y));
    }

    /**
     *
     * @return the vector x coordinate
     */
    public float getX() {
        return x;
    }

    /**
     *
     * @return the vector y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     *
     * @param v2 a Vector2D object
     * @return the result of adding v2 to the original vector
     */
    public Vector2D plus(Vector2D v2) {
        float newX = this.x + v2.getX();
        float newY = this.y + v2.getY();
        Vector2D newVector = new Vector2D(newX, newY);
        return newVector;
    }

    /**
     *
     * @param v2 a Vector2D object
     * @return the result of subtracting v2 from the original vector
     */
    public Vector2D minus(Vector2D v2) {
        float newX = this.x - v2.getX();
        float newY = this.y - v2.getY();
        Vector2D newVector = new Vector2D(newX, newY);
        return newVector;
    }

    /**
     *
     * @param scalar a float representing a constant
     * @return the result of multiplying the original vector with the scalar
     */
    public Vector2D times(float scalar) {
        float newX = Math.round(this.x * scalar);
        float newY = Math.round(this.y * scalar);
        Vector2D newVector = new Vector2D(newX, newY);
        return newVector;
    }

    /**
     *
     * @param currentPos a Vector2D object
     * @param goalPos a Vector2D object
     * @return the length between the two vectors
     */
    public static float distance(Vector2D currentPos, Vector2D goalPos) {
        return length(currentPos) - length(goalPos);
    }

    /**
     *
     * @param vector a Vector2D object
     * @return the length of the vector
     */
    public static float length(Vector2D vector) {
        return (float) Math.sqrt(vector.x * vector.x + vector.y * vector.y);
    }

    /**
     * normalized the vector
     */
    public void normalize() {
        this.x = this.x / length(this);
        this.y = this.y / length(this);
    }

    /**
     *
     * @param vector a Vector2D object
     * @return a normalized version of the vector
     */
    public static Vector2D normalize(Vector2D vector) {
        float x = vector.x / length(vector);
        float y = vector.y / length(vector);
        return new Vector2D(x, y);
    }
}
