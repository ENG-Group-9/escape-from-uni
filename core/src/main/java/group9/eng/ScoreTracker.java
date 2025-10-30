package group9.eng;

/**
 * Manages the game's score, as per the architecture plan.
 * This class tracks the player's current score and provides
 * methods to update it.
 */
public class ScoreTracker {

    /**
     * The player's current score.
     */
    private int currentScore;

    /**
     * Constructor to initialise the ScoreTracker.
     */
    public ScoreTracker() {
        this.currentScore = 0;
    }

    /**
     * Updates the score by adding the given number of points.
     * This is called by other systems, like Events, to modify the score.
     * @param points The number of points to add to the current score.
     */
    public void update(int points) {
        this.currentScore += points;
    }

    /**
     * Gets the current score.
     * @return The current score as an integer.
     */
    public int getScore() {
        return this.currentScore;
    }

    /**
     * Resets the score to 0.
     */
    public void resetScore() {
        this.currentScore = 0;
    }
}