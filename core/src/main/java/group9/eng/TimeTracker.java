package group9.eng;

/**
 * Manages the game's countdown timer logic, as per the architecture plan.
 * This class encapsulates the state and behavior of the timer.
 */
public class TimeTracker extends Tracker {

    private boolean isPaused = false;

    /**
     * Empty constructor for timer (default to time = 0)
     */
    public TimeTracker() {
        super();
    }

    /**
     * Constructor for timer with starting configuration
     * @param startTime The starting time for the timer.
     */
    public TimeTracker(float startTime) {
        super(startTime);
    }

    /**
     * Updates the timer. This should be called once per frame.
     * 
     * @param delta The time in seconds since the last frame.
     */
    @Override
    public void update(float delta) {
        if (!isPaused && this.currentValue > 0) {
            this.currentValue -= delta;
            if (this.currentValue < 0) {
                this.currentValue = 0;
            }
        }
    }

    /**
     * Formats the remaining time into an MM:SS string for display.
     * 
     * @return A string representing the formatted time.
     */
    public String getFormattedTime() {
        int totalSeconds = (int) this.currentValue;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Pauses the timer.
     */
    public void pause() {
        this.isPaused = true;
    }

    /**
     * Resumes the timer.
     */
    public void resume() {
        this.isPaused = false;
    }

    /**
     * Checks if the time has run out.
     * 
     * @return true if time is up, false otherwise.
     */
    public boolean isTimeUp() {
        return this.currentValue <= 0;
    }
}