package minioning.clientconnection;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static minioning.clientconnection.Installer.clearTempData;
import static minioning.clientconnection.Installer.getTempData;
import minioning.common.data.Entity;
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
    public void process(EventBus eventBus, ConcurrentHashMap<UUID, Entity> world) {
        //Check the temporary data for errors, put into real eventbus
        for (String[] data : getTempData()) {
            if (data != null && data.length > 4) { //1 eller 4?
                Events eventType;
                switch (data[3]) {
                    case "CREATEPLAYER":
                        eventType = CREATEPLAYER;
                        System.out.println("putting event: " + eventType.toString());
                        eventBus.putEvent(eventType, data);
                        break;
                    case "LOGIN":
                        eventType = LOGIN;
                        System.out.println("putting event: " + eventType.toString());
                        eventBus.putEvent(eventType, data);
                        break;
                    case "CREATEACCOUNT":
                        eventType = CREATEACCOUNT;
                        System.out.println("putting event: " + eventType.toString());
                        eventBus.putEvent(eventType, data);
                        break;
                    case "MOVEMENT":
                        eventType = MOVEMENT;
                        System.out.println("putting event: " + eventType.toString());
                        eventBus.putEvent(eventType, data);
                        break;
                    case "PLAY":
                        eventType = PLAY;
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
        clearTempData();
    }
}
