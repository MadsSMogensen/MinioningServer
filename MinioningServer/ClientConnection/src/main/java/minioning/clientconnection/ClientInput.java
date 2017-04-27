package minioning.clientconnection;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import static minioning.clientconnection.Installer.clearTempData;
import static minioning.clientconnection.Installer.getTempData;
import minioning.common.data.Entity;
import minioning.common.data.Event;
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
    public void process(ConcurrentHashMap<UUID, Event> eventBus, ConcurrentHashMap<UUID, Entity> world) {
        //Check the temporary data for errors, put into real eventbus
        for (String[] data : getTempData()) {
            if (data != null && data.length > 1) { //1 eller 4?
                Events eventType;
                try {
                    switch (data[3].trim()) {
                    case "CREATEPLAYER":
                        eventType = CREATEPLAYER;
                        System.out.println("putting event: " + eventType.toString());
                        EventBus.putEvent(eventType, data);
                        break;
                    case "LOGIN":
                        eventType = LOGIN;
                        System.out.println("putting event: " + eventType.toString());
                        EventBus.putEvent(eventType, data);
                        break;
                    case "CREATEACCOUNT":
                        eventType = CREATEACCOUNT;
                        System.out.println("putting event: " + eventType.toString());
                        EventBus.putEvent(eventType, data);
                        break;
                    case "MOVEMENT":
                        eventType = MOVEMENT;
                        System.out.println("putting event: " + eventType.toString());
                        EventBus.putEvent(eventType, data);
                        break;
                    case "PLAY":
                        eventType = PLAY;
                        System.out.println("putting event: " + eventType.toString());
                        EventBus.putEvent(eventType, data);
                        break;
                    case "SKILLQ":
                        eventType = SKILLQ;
                        System.out.println("putting event: " + eventType.toString());
                        EventBus.putEvent(eventType, data);
                        break;
                    case "MINIONSWAP":
                        eventType = MINIONSWAP;
                        System.out.println("putting event: " + eventType.toString());
                        EventBus.putEvent(eventType, data);
                        break;
                    default:
                        eventType = FALSEEVENT;
                        System.out.println("False Event: " + data[3].trim());
                        break;
                }
                } catch (Exception e) {
                }
            }
        }
        clearTempData();
    }
}
