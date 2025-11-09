package group9.eng.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector2;

/**
 * Manages the game's score, as per the architecture plan.
 * This class tracks the player's current score and provides
 * methods to update it.
 */
public class EventCompletionTracker{

    private List<Vector2> eventCompletionData;
    private Map<String, Boolean> eventFlags;

    /**
     * Constructor for EventCompletionTracker
     */
    public EventCompletionTracker() {
        // eventCompletionData is a list of Vector2s.
        // Their y value represents the number of events of that type in total,
        // and their x value represents the number of events of that type that have been activated.
        eventCompletionData = new ArrayList<Vector2>();
        for (int i = 0; i < 3; i++) eventCompletionData.add(new Vector2());

        // eventFlags contains flags that events can check to determine if they should occur.
        // Each flag is identified by a string name.
        eventFlags = new HashMap<String, Boolean>();
    }

    /**
     * Returns the event's completion data
     * @return eventCompletionData
     */
    public List<Vector2> GetEventCompletionData() {
        return eventCompletionData;
    }

    /**
     * Returns a event type's completion value
     * @param index the index of a specific event type
     * @return the value for the specified event type
     */
    public int GetEventCompletionData(int index) {
        return (int)eventCompletionData.get(index).x;
    }

    /**
     * Increases the count of an event type by 1
     * @param index the index of a specific event type
     */
    public void AddEventToComplete(int index) {
        eventCompletionData.get(index).y++;
    }

    /**
     * Increases the value of an event type by 1
     * @param index the index of a specific event type
     */
    public void CompleteEvent(int index) {
        eventCompletionData.get(index).x++;
    }

    /**
     * Sets the value of an event flag.
     * @param name the string name of the flag
     * @param value the value the flag should be set to, true or false
     */
    public void setEventFlag(String name, Boolean value) {
        eventFlags.put(name, value);
    }

    /**
     * Returns the value of an event flag.
     * @param name the string name of the flag
     * @return the Boolean value of the flag, or false if the flag doesn't exist
     */
    public Boolean getEventFlag(String name) {
        if (name.isEmpty()) return true;
        return eventFlags.getOrDefault(name, false);
    }
}