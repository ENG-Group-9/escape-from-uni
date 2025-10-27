package group9.eng.events;

import com.badlogic.gdx.math.Vector2;

import group9.eng.EventDialogue;
import group9.eng.TimeTable;

/**
 * Abstract class for all game events.
 * Provides functionality for event triggering and dialogue.
 * To create a new event, extend this class and implement the update() method,
 * which should be suited to the event's behaviour.
 */
public abstract class GameEvent {
    protected boolean eventTriggered = false;
    protected EventDialogue dialogue;

    /**
     * Sets the dialogue for the event.
     * @param dialogue The dialogue for the event.
     */
    public void setEventDialogue(EventDialogue dialogue) {
        this.dialogue = dialogue;
    }

    /**
     * Checks each frame if the event should be triggered.
     * Should be implemented in the subclass and suited the events logic.
     * @param playerPos The current position of the player.
     * @param timeTable The state of the timetable.
     */
    public abstract void update(Vector2 playerPos, TimeTable timeTable);

    /**
     * Checks if the event has been triggered.
     * @return true if the event has been triggered and false otherwise.
     */
    public boolean eventTriggered() {
        return eventTriggered;
    }
}
