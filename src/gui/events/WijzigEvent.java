package gui.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author sam
 */
public class WijzigEvent extends Event {
    
    private int id;
    
    public static final EventType<WijzigEvent> WIJZIG = new EventType(ANY, "WIJZIG");
    
    public WijzigEvent(int id) {
        super(WijzigEvent.WIJZIG);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    
    
}
