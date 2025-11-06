package group9.eng;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;


/**
 * Handles the display of UI menus
 */
public class GameMenu {

    private final Table pauseMenuTable;
    private final Label pauseLabel;
    private final Label scoreLabel;
    private final TextButton resumeButton;
    private final TextButton quitButton;
    private final GameManager mainGame;
    private final Stage stage;
    private final Table eventTrackerTable;
    private final Label eventTrackerPlaceholderLabel;

    private final Table gameOverMenuTable;
    private final Label gameOverLabel;
    private final Label finalScoreLabel;
    private final TextButton restartButton;
    private final TextButton quitGameOverButton;

    /**
     * Constructor for the GameMenu.
     * @param skin The Skin to use for UI elements.
     * @param stage The main UI stage to add the menu table to.
     * @param mainGame A reference to the GameManager class to call back to.
     */
    public GameMenu(Skin skin, Stage stage, GameManager mainGame) {
        this.mainGame = mainGame;
        this.stage = stage;
        // --- Pause Menu Setup ---
        pauseMenuTable = new Table();
        pauseMenuTable.setBackground(skin.getDrawable("default-window"));
        pauseMenuTable.setVisible(false);
        stage.addActor(pauseMenuTable);

        // Create the "PAUSED" label
        pauseLabel = new Label("PAUSED", skin);
        pauseLabel.setFontScale(4);
        pauseLabel.setAlignment(Align.center);

        // Create the Score tracker
        scoreLabel = new Label("Score: 0", skin);
        scoreLabel.setFontScale(2.0f); // Set font scale
        scoreLabel.setAlignment(Align.center);

        // Create the Resume Button
        resumeButton = new TextButton("Resume", skin);
        resumeButton.getLabel().setFontScale(2.0f);

        // Create the Quit Button
        quitButton = new TextButton("Quit", skin);
        quitButton.getLabel().setFontScale(2.0f);

        pauseMenuTable.add(pauseLabel).center().padTop(80).padBottom(25).padLeft(100).padRight(100);
        pauseMenuTable.row();
        pauseMenuTable.add(scoreLabel).center().padBottom(25); // Added the score label
        pauseMenuTable.row();
        pauseMenuTable.add(resumeButton).center().size(350, 80).pad(25);
        pauseMenuTable.row();
        pauseMenuTable.add(quitButton).center().size(350, 80).pad(25).padBottom(60);


        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Tell the Main game class to resume
                mainGame.resumeGame();
            }
        });

        // Added listener for Quit Button
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        // Make the table size itself based on content
        pauseMenuTable.pack(); // Sizes the table around its children
        pauseMenuTable.setPosition(
                (stage.getWidth() - pauseMenuTable.getWidth()) / 2f,
                (stage.getHeight() - pauseMenuTable.getHeight()) / 2f
        );

        // --- Game Over Menu Setup ---
        gameOverMenuTable = new Table();
        gameOverMenuTable.setBackground(skin.getDrawable("default-window"));
        gameOverMenuTable.setVisible(false);
        stage.addActor(gameOverMenuTable);

        gameOverLabel = new Label("GAME OVER!", skin);
        gameOverLabel.setFontScale(4);
        gameOverLabel.setAlignment(Align.center);

        finalScoreLabel = new Label("Final Score: 0", skin);
        finalScoreLabel.setFontScale(2.0f);
        finalScoreLabel.setAlignment(Align.center);

        restartButton = new TextButton("Restart", skin);
        restartButton.getLabel().setFontScale(2.0f);

        quitGameOverButton = new TextButton("Quit", skin);
        quitGameOverButton.getLabel().setFontScale(2.0f);

        gameOverMenuTable.add(gameOverLabel).center().padTop(80).padBottom(25).padLeft(100).padRight(100);
        gameOverMenuTable.row();
        gameOverMenuTable.add(finalScoreLabel).center().padBottom(25);
        gameOverMenuTable.row();
        gameOverMenuTable.add(restartButton).center().size(350, 80).pad(25);
        gameOverMenuTable.row();
        gameOverMenuTable.add(quitGameOverButton).center().size(350, 80).pad(25).padBottom(60);

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainGame.restartGame();
            }
        });

        quitGameOverButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        gameOverMenuTable.pack();
        gameOverMenuTable.setPosition(
                (stage.getWidth() - gameOverMenuTable.getWidth()) / 2f,
                (stage.getHeight() - gameOverMenuTable.getHeight()) / 2f
        );


        // --- Event Tracker Placeholder Setup ---
        eventTrackerTable = new Table();
        eventTrackerTable.setBackground(skin.getDrawable("default-window")); // Add background
        eventTrackerTable.setVisible(false); // Initially hidden
        stage.addActor(eventTrackerTable);

        eventTrackerPlaceholderLabel = new Label("This will be used to track event types\n\nHidden Events 0/1\nPositive Events 0/1\nNegative Events 0/1", skin);
        eventTrackerPlaceholderLabel.setFontScale(2f); // Adjust scale as needed
        eventTrackerPlaceholderLabel.setWrap(true); // Allow wrapping

        eventTrackerTable.add(eventTrackerPlaceholderLabel).pad(15).width(400); // Add padding and set a width
        eventTrackerTable.pack(); // Pack the table around the label

        // Position event tracker in top right
        repositionEventTracker();
    }

    /**
     * Recalculates and sets the position of the event tracker table to the top right corner.
     */
    private void repositionEventTracker() {
         eventTrackerTable.pack(); // Ensure size is correct based on content
         eventTrackerTable.setPosition(stage.getWidth() - 10, stage.getHeight() - 10, Align.topRight);
    }

    /**
     * Makes the pause menu table and event tracker visible and positions them.
     */
    public void displayPauseMenu() { // Removed the List parameter for now
        if (mainGame.getScoreTracker() != null) {
            scoreLabel.setText("Score: " + mainGame.getScoreTracker().getValue());
        }

        // Recalculate pause menu position
        pauseMenuTable.pack();
        pauseMenuTable.setPosition(
                (stage.getWidth() - pauseMenuTable.getWidth()) / 2f,
                (stage.getHeight() - pauseMenuTable.getHeight()) / 2f
        );
        pauseMenuTable.setVisible(true);
        pauseMenuTable.toFront();

        // Reposition and display event tracker
        repositionEventTracker(); // Update position based on current stage size
        eventTrackerTable.setVisible(true);
        eventTrackerTable.toFront(); // Ensure it's on top
    }

    /**
     * Makes the game over menu table visible and positions it.
     */
    public void displayGameOverMenu() {
        // Ensure pause menu is hidden
        hidePauseMenu();

        if (mainGame.getScoreTracker() != null) {
            finalScoreLabel.setText("Final Score: " + mainGame.getScoreTracker().getValue());
        }

        // Recalculate game over menu position
        gameOverMenuTable.pack();
        gameOverMenuTable.setPosition(
                (stage.getWidth() - gameOverMenuTable.getWidth()) / 2f,
                (stage.getHeight() - gameOverMenuTable.getHeight()) / 2f
        );
        gameOverMenuTable.setVisible(true);
        gameOverMenuTable.toFront();

        eventTrackerTable.setVisible(false);

        if(mainGame.getEventDialogue() != null) {
            mainGame.getEventDialogue().hide();
        }
    }


    /**
     * Hides the pause menu table and the event tracker.
     */
    public void hidePauseMenu() {
        pauseMenuTable.setVisible(false);
        eventTrackerTable.setVisible(false); // Hide tracker too
        gameOverMenuTable.setVisible(false);
    }

    /**
     * Returns whether the pause menu is currently visible.
     * @return true if the pause menu table is visible, false otherwise.
     */
    public boolean isPausedDisplayActive() {
        return pauseMenuTable.isVisible();
    }
}