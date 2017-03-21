package minioning.common.data;

/**
 *
 * @author Mogensen
 */
public class Event {
    
    private Events eventType;
    private String[] data;
    
    public Event(Events eventType, String[] data){
        this.eventType = eventType;
        this.data = data;
    }
    
    public Events getType(){
        return eventType;
    }
    
    public String[] getData(){
        return data;
    }
    
    
}
