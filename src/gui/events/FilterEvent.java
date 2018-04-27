package gui.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author sam
 */
public class FilterEvent extends Event {
    
    private String filterName;
    
    public static final EventType<FilterEvent> FILTER = new EventType(ANY, "FILTER");
    
    public FilterEvent(String filterName) {
        super(FilterEvent.FILTER);
        this.filterName = filterName;
    }

    public String getFilterName() {
        return filterName;
    }

    
}
