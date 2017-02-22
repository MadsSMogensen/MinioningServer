package minioning.unit;

import java.util.Map;
import java.util.UUID;
import minioning.common.data.Entity;
import minioning.common.data.EventBus;
import minioning.common.services.IEntityProcessingService;
import org.openide.util.lookup.ServiceProvider;
/**
 *
 * @author Helle
 */
@ServiceProvider (service = IEntityProcessingService.class)
public class UnitProcessor implements IEntityProcessingService{

    @Override
    public void process(EventBus events, Map<UUID, Entity> entities, Entity entity) {
//        System.out.println("Processing: " + entity.getName());
    }
    
}
