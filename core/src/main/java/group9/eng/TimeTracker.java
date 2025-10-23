package group9.eng;

/**
 * Manages the game's countdown timer logic, as per the architecture plan.
 * This class encapsulates the state and behavior of the timer.
 */
public class TimeTracker {

    private float timeRemaining;
    private boolean isPaused = false;

    /**
     * Constructor to initialise the timer.
     * @param seconds The initial time in seconds for the countdown.
     */
    public TimeTracker(float seconds) {
        this.timeRemaining = seconds;
    }

    /**
     * Updates the timer. This should be called once per frame.
     * @param delta The time in seconds since the last frame.
     */
    public void update(float delta) {
        if (!isPaused && timeRemaining > 0) {
            timeRemaining -= delta;
            if (timeRemaining < 0) {
                timeRemaining = 0;
            }
        }
    }

    /**
     * Formats the remaining time into an MM:SS string for display.
     * @return A string representing the formatted time.
     */
    public String getFormattedTime() {
        int totalSeconds = (int) timeRemaining;
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
     * @return true if time is up, false otherwise.
     */
    public boolean isTimeUp() {
        return timeRemaining <= 0;
    }

    /**
     * Gets the remaining time in seconds.
     * @return The raw float value of the time remaining.
     */
    public float getTimeRemaining() {
        return timeRemaining;
    }
}