package minioning.minioningserver;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import static minioning.common.data.EventBus.getBus;
import minioning.common.data.GameData;
import minioning.common.data.Vector2D;
import minioning.common.services.IEntityProcessingService;
import minioning.collision.CollisionProcessor;
import minioning.movement.MovementProcessor;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openide.util.Lookup;

/**
 *
 * @author Jakob and Mads
 */
//@Category(IntegrationTest.class)
public class CollisionIntegrationTest {

    private Lookup lookup;
    UUID ID = UUID.randomUUID();
    private Entity entity;
    private Entity expectedEntity;
    private ConcurrentHashMap<UUID, Entity> world = new ConcurrentHashMap();

    @Before
    public void setData() {
        lookup = Lookup.getDefault();
        //instantiate entity objects
        entity = new Entity(ID, "testEntity", 100, 100);
        expectedEntity = new Entity(ID, "expectedEntity", 110, 110);
        //get a degree direction
        Vector2D direction = new Vector2D(1, 1);
        direction.getDirection();
        //set entity direction
        entity.setDirection(direction);
        //calculate expected goal
        int expectedGoalX = Math.round(expectedEntity.getX() + (expectedEntity.getSpeed() * GameData.getDt() * direction.getX()));
        int expectedGoalY = Math.round(expectedEntity.getY() + (expectedEntity.getSpeed() * GameData.getDt() * direction.getX()));
        //set dx and dy
        entity.setDx(expectedGoalX);
        entity.setDy(expectedGoalY);
        expectedEntity.setDx(expectedGoalX);
        expectedEntity.setDy(expectedGoalY);
        //set expected entity to goal location
        expectedEntity.setX(expectedGoalX);
        expectedEntity.setY(expectedGoalY);
        expectedEntity.setNextx(expectedGoalX);
        expectedEntity.setNexty(expectedGoalY);
        //set Dt to 1 second
        GameData.setDt(1);
        world.put(entity.getID(), entity);
    }

    //@Test
    public void testAssertEquals() {
        for (IEntityProcessingService processor : getIEntityProcessingServices()) {
            if (processor.getClass().equals(CollisionProcessor.class) || processor.getClass().equals(MovementProcessor.class)) {
                for (Entity e : world.values()) {
                    processor.process(getBus(), world, e);
                    processor.process(getBus(), world, e);
                    processor.process(getBus(), world, e);
                    processor.process(getBus(), world, e);
                }
            }
        }
        assertThat(entity.getX(), not(equalTo(expectedEntity.getX())));
        assertThat(entity.getY(), not(equalTo(expectedEntity.getY())));
    }

    private Collection<? extends IEntityProcessingService> getIEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }
    
}
