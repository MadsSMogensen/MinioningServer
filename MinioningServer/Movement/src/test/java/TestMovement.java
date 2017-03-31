
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.movement.MovementProcessor;
import org.junit.Test;
import static org.junit.Assert.*;
import minioning.common.data.EventBus;
import minioning.common.data.Entity;

/**
 *
 * @author Mogensen
 */
public class TestMovement {
//
//    EventBus testBus = new EventBus();
//    Map<UUID, Entity> world = new ConcurrentHashMap<UUID, Entity>();
//    Entity entityExpect = new Entity(UUID.randomUUID(), "test");
//    Entity entityResult = new Entity(UUID.randomUUID(), "test");
//    
//    @Test
//    public void testMoveEntity() {
//        MovementProcessor processor = new MovementProcessor();
//
//        //set expected result
//        entityExpect.setX(1);
//        entityExpect.setY(1);
//        entityExpect.setDx(1);
//        entityExpect.setDy(1);
//
//        //set starting position
//        entityResult.setX(0);
//        entityResult.setY(0);
//        entityResult.setDx(1);
//        entityResult.setDy(1);
//
//        processor.process(testBus, world, entityResult);
//
////        System.out.println("result: " + entityExpect.getX() + ", " + entityResult.getX());
//        assertEquals(entityExpect.getX(), entityResult.getX(), 0.49);
//        assertEquals(entityExpect.getY(), entityResult.getY(), 0.49);
////        System.out.println("test done");
//
//    }
}
