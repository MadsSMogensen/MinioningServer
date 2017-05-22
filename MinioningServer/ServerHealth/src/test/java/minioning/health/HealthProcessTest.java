/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.health;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.EntityType;
import minioning.common.data.Event;
import static minioning.common.data.Events.HPCHANGE;
import static org.junit.Assert.*;

/**
 *
 * @author Jakob and Mads
 */
public class HealthProcessTest {

    public HealthProcessTest() {
    }

    /**
     * Test of process method, of class HealthProcess.
     */
    @org.junit.Test
    public void testProcess() {
        System.out.println("process");

        // Arrange
        ConcurrentHashMap<UUID, Event> eventBus = new ConcurrentHashMap<UUID, Event>();
        ConcurrentHashMap<UUID, Entity> world = new ConcurrentHashMap<UUID, Entity>();
        HealthProcess instance = new HealthProcess();
        

        Entity entity = new Entity(UUID.randomUUID(), "test");

        entity.setType(EntityType.PLAYER);
        

        world.put(entity.getID(), entity);
        
        String[] s = new String[2];
        s[0] = entity.getID().toString();
        s[1] = EntityType.HOLYBOLT.toString();

        
        UUID id = UUID.randomUUID();
        
        Event event = new Event(HPCHANGE, s);
        eventBus.put(id, event);
        
        eventBus.put(id, event);

        
        // Act
        instance.process(eventBus, world);

        
        // Assert
        assertTrue(entity.getHp() < 100);
        
    }

}
