package gui.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author sam
 */
public class BeheerEvent extends Event {
    
    private int id;
    
    public static final EventType<BeheerEvent> BEHEER = new EventType(ANY, "BEHEER");
    
    public BeheerEvent(int id) {
        super(BeheerEvent.BEHEER);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    
    
}
