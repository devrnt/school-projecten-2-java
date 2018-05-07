package gui.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author sam
 */
public class DetailsEvent extends Event {
    
    private int id;
    
    public static final EventType<DetailsEvent> DETAILS = new EventType(ANY, "DETAILS");
    
    public DetailsEvent(int id) {
        super(DetailsEvent.DETAILS);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    
    
}
