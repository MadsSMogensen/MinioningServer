
import java.util.UUID;
import minioning.collision.CollisionProcessor;
import minioning.common.data.Entity;
import minioning.common.data.EntityType;
import minioning.common.data.GameData;
import minioning.common.data.Vector2D;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Jakob and Mads
 */
public class TestCollision {

    CollisionProcessor cp = new CollisionProcessor();

    @Test
    public void process() {
        //Arrange

        //Act
        //Assert
    }

    @Test
    public void regularCollision() {
        //Arrange
        Entity entity = new Entity(UUID.randomUUID(), "Test", 100, 100);
        Vector2D direction = new Vector2D(1, 0);
        Vector2D velocity = new Vector2D(50, 0);
        entity.setDirection(direction);
        entity.setVelocity(velocity);
        Entity wall = new Entity(UUID.randomUUID(), "Test", 130, 100);
        wall.setType(EntityType.WALL);
        wall.setImmobile(true);
        GameData.setDt(1);
        int exptected = 50;
        //Act
        cp.regularCollision(wall, entity);
        //Assert
        assertThat(entity.getNextx(), equalTo(exptected));
    }

    @Test
    public void colliding() {
        //Arrange
        Entity entity = new Entity(UUID.randomUUID(), "Test", 100, 100);
        Entity wall = new Entity(UUID.randomUUID(), "Test", 130, 100);
        wall.setType(EntityType.WALL);
        wall.setImmobile(true);
        //Act
        boolean result = cp.colliding(wall, entity);
        //Assert
        assertThat(result, equalTo(true));
    }

    @Test
    public void length() {
        //Arrange
        float x1 = 0;
        float y1 = 0;
        float x2 = 50;
        float y2 = 50;
        float expected = (float)Math.sqrt(5000);
        //Act
        float result = cp.length(x1, y1, x2, y2);
        //Assert
        assertThat(result, equalTo(expected));
    }

}
