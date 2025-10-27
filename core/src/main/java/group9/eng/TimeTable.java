package group9.eng;

/**
 * Manages the player's timetable state.
 * Tracks whether the player has lost or found their timetable.
 */
public class TimeTable {
    private boolean hasTimeTable;

    /**
     * Constructor to initialise player to start with a timetable.
     */
    public TimeTable() {
        this.hasTimeTable = true;
    }

    /**
     * Called when the player loses their timetable.
     */
    public void lostTimeTable() {
        this.hasTimeTable = false;
    }

    /**
     * Called when the player finds their timetable.
     */
    public void foundTimeTable() {
        this.hasTimeTable = true;
    }

    /**
     * Checks if the player has their timetable.
     * @return true if the player has their timetable and false otherwise.
     */
    public boolean hasTimeTable() {
        return hasTimeTable;
    }
}