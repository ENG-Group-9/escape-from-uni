package group9.eng;

/**
 * Superclass for trackers as per architecture plan.
 * This class establishes basic logic for trackers.
 */
public class Tracker {
    protected float currentValue;

    /**
     * Constructor to initialise tracker.
     * Configuration defaulting to zero.
     */
    public Tracker() {
        this.currentValue = 0;
    }

    /**
     * Constructor to initialise tracker.
     * 
     * @param initialValue The initial value the tracker will begin with
     */
    public Tracker(float initialValue) {
        this.currentValue = initialValue;
    }

    /**
     * Gets the current value of the tracker
     * 
     * @return The current tracker value as float
     */
    public float getValue() {
        return this.currentValue;
    }

    /**
     * Updates the tracker.
     * @param delta The float value of change to be made to the tracker value
     */
    public void update(float delta) {
        this.currentValue += delta;
    }

    /**
     * Clears the current value in the tracker
     */
    public void clear() {
        this.currentValue = 0;
    }
}
