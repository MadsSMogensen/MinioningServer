package minioning.movement;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import minioning.common.data.GameData;
import minioning.common.data.Vector2D;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mogensen
 */
@ServiceProvider(service = IEntityProcessingService.class)
public class TestMovement implements IEntityProcessingService {

    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, Map<UUID, Entity> entities, Entity entity) {
//        System.out.println("running");
        float dt = GameData.getDt();

        //check for new movement calls
        for (Map.Entry<UUID, Event> eventEntry : eventBus.entrySet()) {

            UUID key = eventEntry.getKey();
            Event event = eventEntry.getValue();
            switch (event.getType()) {
                case MOVEMENT:  //setting the velocity vector of the entity
                    String[] data = event.getData();
                    UUID owner = UUID.fromString(data[2]);
                    if (entity.getOwner().equals(owner)) {
                        int destinationX = Integer.parseInt(data[4]);
                        int destinationY = Integer.parseInt(data[5].trim());
                        entity.setDestinationX(destinationX);
                        entity.setDestinationY(destinationY);
                        eventBus.remove(key);
                    }
                    break;
            }
        }
        setMovement(entity);
        moveEntity(entity, dt);

    }

    private void setMovement(Entity entity) {
        int x = entity.getX();
        int y = entity.getY();
        int destinationX = entity.getDestinationX();
        int destinationY = entity.getDestinationY();

        //the new vector to be added to the entitie's movement
        float B = getAngle(x, destinationX, y, destinationY);
        float A = 180 - 90 - B;
        float b = entity.getCSpeed() * (float) Math.cos(A);
        float a = b * (float) Math.tan(A);
        Vector2D newVector = new Vector2D(a, b);
        entity.setVelocity(newVector);

        //the deacceleration vector to be added to the entities movement
//        entity.getVelocity().times(entity.getDeacceleration());
    }

    private void moveEntity(Entity entity, float dt) {
        //variables
        int x = entity.getX();
        int y = entity.getY();
        //turn the entity
        //not implemented yet
        //move the entity
        Vector2D velocity = entity.getVelocity();
        x += Math.round(velocity.getX() * dt);
        y += Math.round(velocity.getY() * dt);
        entity.setX(x);
        entity.setY(y);

    }

    private static float getAngle(int x, int dx, int y, int dy) {
        float angle = (float) Math.toDegrees(Math.atan2(dy - y, dx - x));

        if (angle < 0) {
            angle += 360;
        }
        if(angle > 360){
            angle -= 360;
        }

        return angle;
    }
}
