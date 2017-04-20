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

    public Vector2D() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2D(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getDirection() {
        calculateDirection();
        return direction;
    }
    
    public static Vector2D getDirection(int x1, int x2, int y1, int y2){
        float vx = x2-x1;
        float vy = y2-y1;
        Vector2D direction = normalize(new Vector2D(vx,vy));
        return direction;
    }

    private void calculateDirection() {
        this.direction = (float) Math.asin(x/getMagnitude());
//        this.direction = (float) Math.atan(y / x);
    }

    public float getMagnitude() {
        calculateMagnitude();
        return magnitude;
    }

    private void calculateMagnitude() {
        this.magnitude = (float) Math.sqrt((x * x + y * y));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2D plus(Vector2D v2) {
        float newX = this.x + v2.getX();
        float newY = this.y + v2.getY();
        Vector2D newVector = new Vector2D(newX, newY);
        return newVector;
    }

    public Vector2D minus(Vector2D v2) {
        float newX = this.x - v2.getX();
        float newY = this.y - v2.getY();
        Vector2D newVector = new Vector2D(newX, newY);
        return newVector;
    }

    public Vector2D times(float scalar) {
        float newX = Math.round(this.x * scalar);
        float newY = Math.round(this.y * scalar);
        Vector2D newVector = new Vector2D(newX, newY);
        return newVector;
    }
    
    public static float distance(Vector2D currentPos, Vector2D goalPos) {
        return length(currentPos) - length(goalPos);
    }
    
    public static float length(Vector2D vector){
        return (float)Math.sqrt(vector.x*vector.x + vector.y*vector.y);
    }
    
    public void normalize(){
        this.x = this.x / length(this); 
        this.y = this.y / length(this);
    }
    
    public static Vector2D normalize(Vector2D vector){
        float x = vector.x / length(vector);
        float y = vector.y / length(vector);
        return new Vector2D(x,y);
    }

}
