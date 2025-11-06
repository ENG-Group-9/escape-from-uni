package group9.eng;

/**
 * Manages the game's score, as per the architecture plan.
 * This class tracks the player's current score and provides
 * methods to update it.
 */
public class ScoreTracker extends Tracker{

    /**
     * Empty constructor for ScoreTracker
     * Defaults to score = 0
     */
    public ScoreTracker() {
        super();
    }

    /**
     * Constructor for ScoreTracker c;ass
     * @param startScore Score that tracker begins with.
     */
    public ScoreTracker(float startScore) {
        super(startScore);
    }

    /**
     * @return A string stating current score value.
     */
    public String toString(){
        String formatString = "Current Score: %d";
        return String.format(formatString, (int)this.currentValue);
    }
}