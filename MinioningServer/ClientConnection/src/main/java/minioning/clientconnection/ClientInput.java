package minioning.clientconnection;

import static minioning.clientconnection.Installer.getTempData;
import minioning.common.data.EventBus;
import minioning.common.data.Events;
import static minioning.common.data.Events.*;
import minioning.common.services.IConnectionService;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Mads
 */
@ServiceProvider(service = IConnectionService.class)
public class ClientInput implements IConnectionService {

    @Override
    public void process(EventBus eventBus) {
        //Check the temporary data for errors, put into real eventbus
        for (String[] data : getTempData()) {
            if (data != null && data.length > 1) {
                Events eventType;
                switch (data[0]) {
                    case "CREATEPLAYER":
                        eventType = CREATEPLAYER;
                        System.out.println("putting event: " + eventType.toString());
                        eventBus.putEvent(eventType, data);
                        break;
                    default:
                        eventType = FALSEEVENT;
                        System.out.println("False Event");
                        break;
                }
            }
        }
        getTempData().clear();
    }
}
