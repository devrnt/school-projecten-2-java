package gui.events;

import domein.Sessie;
import javafx.event.Event;
import javafx.event.EventType;

public class GeefSessieDoorEvent extends Event {

    private Sessie sessie;

    public static final EventType<GeefSessieDoorEvent> GEEFSESSIEDOOR = new EventType(ANY, "GEEFSESSIEDOOR");

    public GeefSessieDoorEvent(Sessie sessie) {
        super(GeefSessieDoorEvent.GEEFSESSIEDOOR);
        this.sessie = sessie;
    }

    public Sessie getSessie() {
        return sessie;
    }
}
