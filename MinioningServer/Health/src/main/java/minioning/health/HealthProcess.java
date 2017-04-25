/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minioning.health;


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import minioning.common.data.Entity;
import minioning.common.data.EntityType;
import minioning.common.data.Event;
import static minioning.common.data.Events.HPCHANGE;
import minioning.common.services.IHealthProcessorService;
import org.openide.util.lookup.ServiceProvider;
/**
 *
 * @author Jakob
 */


@ServiceProvider(service = IHealthProcessorService.class)
public class HealthProcess implements IHealthProcessorService {

    @Override
    public void process(ConcurrentHashMap<UUID, Event> eventBus, ConcurrentHashMap<UUID, Entity> world) {

        
       //        for (Map.Entry<UUID, Event> entry : eventBus.getBus().entrySet()) {
        for (Map.Entry<UUID, Event> entry : eventBus.entrySet()) {
            UUID key = entry.getKey();
            Event event = entry.getValue();
            System.out.println("Event found!: " + event.getType());
            String[] data = event.getData();
            if (event.getType() == HPCHANGE) {
                if (EntityType.valueOf(data[1]) == EntityType.HOLYBOLT) {
                    UUID entityID = UUID.fromString(data[0]);
                    Entity entity = world.get(entityID);
                    
                    entity.setHp(entity.getHp()-10);
                    
                    if(entity.getHp() <= 0){
                        world.remove(entityID);
                    }else{
                    
                    world.replace(entityID, entity);
                    }
                    System.out.println(entity.getHp());
                    eventBus.remove(key);
//                event.getData()
//                    System.out.println("LOGINSUCCESS FOUND");
//                    loginSuccess(event);
//                    eventBus.remove(key);
//                    break;
                }
            }
        }
    }
}
