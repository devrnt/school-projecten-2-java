package gui.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author sam
 */
public class DownloadEvent extends Event {
    
    public static final EventType<DownloadEvent> DOWNLOAD = new EventType(ANY, "DOWNLOAD");
    
    public DownloadEvent() {
        super(DownloadEvent.DOWNLOAD);
    }
    
}
