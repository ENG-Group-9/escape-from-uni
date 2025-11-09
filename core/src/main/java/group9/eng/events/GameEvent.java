package group9.eng.events;

import java.util.List;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.math.Vector2;
import group9.eng.Entity;
import group9.eng.ScoreTracker;
import group9.eng.components.ControlComponent;

public class GameEvent {
    private String message;
    private int points;
    private Boolean repeat;
    private Boolean alreadyTriggered;
    private double chance;
    private int when;
    private int eventType;
    private double timer;
    private double updatePeriod;
    private double speedBoost;
    private String flag;
    private Boolean setFlag;

    private EventDialogue dialogue;
    private ScoreTracker scoreTracker;
    private EventCompletionTracker eventCompletionTracker;
    private int completionIndex;

    /**
     * Creates a new GameEvent.
     * 
     * @param message a string to be displayed when this event occurs.
     * @param points the number of points given to the player when this event occurs, can be
     *               negative.
     * @param repeat should this event happen multiple times or not?
     * @param chance a double from 0 to 1, the probability of this event happening
     * @param when an integer from 0 to 2, representing when this event should occur.
     *             0 = when event area is entered
     *             1 = at regular intervals while in event area
     *             2 = when event area is exited
     * @param updatePeriod if when = 1, time between occurences of this event.
     * @param speedBoost a multiplier to be applied to the player's speed when this event occurs.
     * @param eventType an integer denoting the type of event.
     *                  0 = hidden
     *                  1 = positive
     *                  2 = negative
     *                  3 = null/other
     * @param flag the name of the flag that is checked to see if this event can occur, an empty
     *             string means no flag is used.
     * @param setFlag a Boolean value. The flag must not be equal to this value for this event to
     *                occur. When this event occurs, the flag's value is updated to match this
     *                value. e.g. if setFlag = true, wait until the flag is false, then set it to
     *                true.
     * @param dialogue a reference to an EventDialogue instance.
     * @param scoreTracker a reference to a ScoreTracker.
     * @param eventCompletionTracker a reference to an EventCompletionTracker.
     */
    public GameEvent(
        String message,
        int points,
        Boolean repeat,
        double chance,
        int when,
        double updatePeriod,
        double speedBoost,
        int eventType, // "0" hidden, "1" positive, "2" negative, "3" null/other
        String flag,
        Boolean setFlag,
        EventDialogue dialogue,
        ScoreTracker scoreTracker,
        EventCompletionTracker eventCompletionTracker

    ) {
        this.message = message;
        this.points = points;
        this.repeat = repeat;
        alreadyTriggered = false;
        this.chance = chance;
        this.speedBoost = speedBoost;

        this.when = when;
        this.updatePeriod = updatePeriod;
        timer = updatePeriod;

        this.flag = flag;
        this.setFlag = setFlag;

        this.dialogue = dialogue;
        this.scoreTracker = scoreTracker;
        this.eventType = eventType;
        // Gdx.app.log("GameEvent", "Constructed event '" + message + "' type=" + eventType); debugging stuf
        this.eventCompletionTracker = eventCompletionTracker;

        // map eventType 3 (used by hidden events) to tracker index 0
        if (eventType == 3) {
            completionIndex = 0; // hidden
        } else {
            completionIndex = eventType; // 0,1,2 as usual
        }

        eventCompletionTracker.AddEventToComplete(completionIndex);
    }

    /**
     * Should be called when an entity has just entered the event's area.
     * @param entity the Entity that entered the event's area.
     */
    public void start(Entity entity) {
        if (when == 0) apply(entity);
    }

    /**
     * Should be called when an entity has just exited the event's area.
     * @param entity the Entity that exited the event's area.
     */
    public void end(Entity entity) {
        if (when == 2) apply(entity);
    }
    
    /**
     * Should be called when an entity is within the event's area.
     * @param entity the Entity within the event's area.
     */
    public void update(Entity entity) {
        if (when != 1) return;

        if (timer > 0) {
            timer -= Gdx.graphics.getDeltaTime();
        }
        if (timer <= 0) {
            timer += updatePeriod;
            apply(entity);
        }
    }

    /**
     * Causes the behaviour of the event to take place.
     * @param the Entity to apply the event to.
     */
    private void apply(Entity entity) {
        if (Math.random() >= chance) return;
        if (!repeat && alreadyTriggered) return;

        if (!flag.isEmpty()) {
            if (eventCompletionTracker.getEventFlag(flag) == setFlag) return;
            eventCompletionTracker.setEventFlag(flag, setFlag);
        }

        // Gdx.app.log("GameEvent", "Constructed event '" + message + "' type=" + eventType); debug

        dialogue.show(message);
        scoreTracker.update(points);
        if (speedBoost != 0) {
            ((ControlComponent) entity.getComponent(ControlComponent.class)).addSpeedMultiplier(speedBoost);
        }

        // safety: bounds check in case of bad values
        List<Vector2> data = eventCompletionTracker.GetEventCompletionData();
        if (!alreadyTriggered && completionIndex >= 0 && completionIndex < data.size()) {
            eventCompletionTracker.CompleteEvent(completionIndex);
        }
        //else {
            // Gdx.app.log("GameEvent", "Skipped tracker increment (eventType == 3)"); debug
        //}

        alreadyTriggered = true;
    }
}
