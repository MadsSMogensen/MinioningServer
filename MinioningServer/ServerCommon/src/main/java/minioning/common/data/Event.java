package minioning.common.data;

/**
 *
 * @author Jakob & Mads
 */
public class Event {
    
    private Events eventType;
    private String[] data;
    
    /**
     * 
     * @param eventType enum of type eventType
     * @param data a String array of data
     */
    public Event(Events eventType, String[] data){
        this.eventType = eventType;
        this.data = data;
    }
    
    /**
     * 
     * @return an enum of type eventType
     */
    public Events getType(){
        return eventType;
    }
    
    /**
     * 
     * @return a String array of data
     */
    public String[] getData(){
        return data;
    }
}
