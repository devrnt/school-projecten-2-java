package gui.events;

import domein.Sessie;
import javafx.event.Event;
import javafx.event.EventType;

public class GeefSessieDoorEvent extends Event {

    private final Sessie sessie;
    private final int stap;
    public static final EventType<GeefSessieDoorEvent> GEEFSESSIEDOOR = new EventType(ANY, "GEEFSESSIEDOOR");

    public GeefSessieDoorEvent(Sessie sessie, int stap) {
        super(GeefSessieDoorEvent.GEEFSESSIEDOOR);
        this.sessie = sessie;
        this.stap = stap;
    }

    public Sessie getSessie() {
        return sessie;
    }

    public int getStap() {
        return stap;
    }
}
