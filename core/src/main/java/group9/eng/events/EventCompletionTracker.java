package group9.eng.events;

import com.badlogic.gdx.math.Vector2;

/**
 * Manages the game's score, as per the architecture plan.
 * This class tracks the player's current score and provides
 * methods to update it.
 */
public class EventCompletionTracker{

    private Vector2[] eventCompletionData;

    /**
     * Constructor for EventCompletionTracker
     * @param numberOfEvents an integer array containing how many of each type of event there is
     * default is length of 3, index "0" being hidden, "1" positive, "2" negative
     * creates a vector2 array with each x value being set to 0
     */
    public EventCompletionTracker(int[] numberOfEvents) {
        eventCompletionData = new Vector2[numberOfEvents.length];
        for(int i = 0; i < numberOfEvents.length; i++) {
            eventCompletionData[i] = new Vector2(0, numberOfEvents[i]);
        }
    }

    /**
     * Returns the event's completion data
     * @return eventCompletionData
     */
    public Vector2[] GetEventCompletionData() {
        return eventCompletionData;
    }

    /**
     * Returns a event type's completion value
     * @param index the index of a specific event type
     * @return the value for the specified event type
     */
    public int GetEventCompletionData(int index) {
        return (int)eventCompletionData[index].x;
    }
    /**
     * Increases the value of an event type by 1
     * @param index the index of a specific event type
     */
    public void AddEventCompletionData(int index) {
        eventCompletionData[index].x++;
    }

    
}