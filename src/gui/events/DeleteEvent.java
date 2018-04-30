package gui.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author sam
 */
public class DeleteEvent extends Event {
    
    private int id;
    
    public static final EventType<DeleteEvent> DELETE = new EventType(ANY, "DELETE");
    
    public DeleteEvent(int id) {
        super(DeleteEvent.DELETE);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    
    
}
