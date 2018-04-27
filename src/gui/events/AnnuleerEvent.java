package gui.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author sam
 */
public class AnnuleerEvent extends Event {
    private int id;
    
    public static final EventType<AnnuleerEvent> ANNULEER = new EventType(ANY, "ANNULEER");
    
    public AnnuleerEvent(int id) {
        super(AnnuleerEvent.ANNULEER);
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
}
