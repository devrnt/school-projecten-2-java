package gui.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author sam
 */
public class MenuEvent extends Event {
    
    private int index;
    
    public static final EventType<MenuEvent> MENU = new EventType(ANY, "MENU");
    
    public MenuEvent(int index) {
        super(MenuEvent.MENU);
        this.index = index;
    }

    public int getId() {
        return index;
    }

    
    
}
