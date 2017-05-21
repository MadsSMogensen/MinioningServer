
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.Event;
import minioning.common.data.Events;
import minioning.common.data.GameData;
import minioning.movement.MovementProcessor;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Jakob og Mads
 */
public class TestMovement {

    MovementProcessor mp = new MovementProcessor();

    @Test
    public void process() {
        //Arrange
        UUID ownerID = UUID.randomUUID();
        Entity entity = new Entity(ownerID, "Test");
        String[] eventData = new String[]{"", "", ownerID.toString(), "", "150", "100"};
        Event event = new Event(Events.MOVEMENT, eventData);
        ConcurrentHashMap<UUID, Event> eventBus = new ConcurrentHashMap<UUID, Event>();
        eventBus.put(UUID.randomUUID(), event);
        ConcurrentHashMap<UUID, Entity> world = new ConcurrentHashMap<UUID, Entity>();
        GameData.setDt(1);
        int expectedDx = 150;
        int expectedNextx = 150;
        int exptectedX = 150;
        //Act
        mp.process(eventBus, world, entity);
        //Assert
        assertThat(entity.getDx(), equalTo(expectedDx));
        assertThat(entity.getNextx(), equalTo(expectedDx));
        //Act
        mp.process(eventBus, world, entity);
        //Assert
        assertThat(entity.getX(), equalTo(exptectedX));
    }

    @Test
    public void processEventBus() {
        //Arrange
        UUID ownerID = UUID.randomUUID();
        Entity entity = new Entity(ownerID, "Test");
        String[] eventData = new String[]{"", "", ownerID.toString(), "", "150", "150"};
        Event event = new Event(Events.MOVEMENT, eventData);
        ConcurrentHashMap<UUID, Event> eventBus = new ConcurrentHashMap<UUID, Event>();
        eventBus.put(UUID.randomUUID(), event);
        int expected = 150;
        //Act
        mp = new MovementProcessor();
        mp.processEventBus(eventBus, entity);
        //Assert
        assertThat(entity.getDx(), equalTo(expected));
        assertThat(entity.getDy(), equalTo(expected));
    }

    @Test
    public void processNextMovement() {
        //Arrange
        Entity entity = new Entity(UUID.randomUUID(), "Test");
        entity.setDx(150);
        entity.setDy(100);
        float elapsed = 1f;
        int expectedX = 150;
        int expectedy = 100;
        //Act
        mp.processNextMovement(entity, elapsed);
        //Assert
        assertThat(entity.getNextx(), equalTo(expectedX));
        assertThat(entity.getNexty(), equalTo(expectedy));
    }

    @Test
    public void goalReached() {
        //Arrange
        Entity entity = new Entity(UUID.randomUUID(), "Test");
        entity.setSize(11);
        entity.setDx(110);
        entity.setDx(110);
        //Act
        boolean result = mp.goalReached(entity, entity.getX(), entity.getY(), entity.getDx(), entity.getDy());
        //Assert
        assertThat(result, equalTo(true));
    }

    @Test
    public void distance() {
        //Arrange
        int x1 = 0;
        int y1 = 0;
        int x2 = 50;
        int y2 = 50;
        float expected = (float)Math.sqrt(5000);
        //Act
        float result = mp.distance(x1,y1,x2,y2);
        //Assert
        assertThat(result, equalTo(expected));
    }

    @Test
    public void processMove() {
        //Arrange
        Entity entity = new Entity(UUID.randomUUID(), "Test");
        entity.setNextx(110);
        entity.setNexty(110);
        entity.setNextxReal(110);
        entity.setNextyReal(110);
        int expected = 110;
        //Act
        mp.processMove(entity);
        //Assert
        assertThat(entity.getX(), equalTo(expected));
        assertThat(entity.getY(), equalTo(expected));
    }
}
