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
     * @param numberOfEvents an integer array containing how many of each type of event there is
     * default is length of 3, index "0" being hidden, "1" positive, "2" negative
     * creates a vector2 array with each x value being set to 0
     */
    public EventCompletionTracker() {
        eventCompletionData = new ArrayList<Vector2>();
        for (int i = 0; i < 3; i++) eventCompletionData.add(new Vector2());
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

    public void setEventFlag(String name, Boolean value) {
        eventFlags.put(name, value);
    }

    public Boolean getEventFlag(String name) {
        if (name.isEmpty()) return true;
        return eventFlags.getOrDefault(name, false);
    }
}