package minioning.clientconnection;

import minioning.common.data.Event;
import minioning.common.data.EventBus;
import minioning.common.services.IConnectionService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mads
 */
@ServiceProvider (service = IConnectionService.class)
public class ClientOutput implements IConnectionService{

    @Override
    public void process(EventBus eventBus) {
        //Find the World in the eventbus, send it to all clients
        for(Event event : eventBus.getBus().values()){
            switch(event.getType()){
                case LOGIN:
                    
            }
        }
    }
}
