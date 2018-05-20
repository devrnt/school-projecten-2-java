package gui.events;

import java.util.List;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author sam
 */
public class InvalidInputEvent extends Event {
    private List<String> velden;
    
    public static final EventType<InvalidInputEvent> INVALIDINPUT = new EventType(ANY, "INVALIDINPUT");
    
    public InvalidInputEvent(List<String> velden) {
        super(InvalidInputEvent.INVALIDINPUT);
        this.velden = velden;
    }

    public List<String> getVelden() {
        return velden;
    }
    
    
}
