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
    private final TextButton resumeButton;
    private final TextButton quitButton; // Added
    private final GameManager mainGame;
    private final Stage stage;

    /**
     * Constructor for the GameMenu.
     * @param skin The Skin to use for UI elements.
     * @param stage The main UI stage to add the menu table to.
     * @param mainGame A reference to the GameManager class to call back to.
     */
    public GameMenu(Skin skin, Stage stage, GameManager mainGame) {
        this.mainGame = mainGame;
        this.stage = stage;

        pauseMenuTable = new Table();
        pauseMenuTable.setBackground(skin.getDrawable("default-window"));
        pauseMenuTable.setVisible(false);
        stage.addActor(pauseMenuTable);

        // Create the "PAUSED" label
        pauseLabel = new Label("PAUSED", skin);
        pauseLabel.setFontScale(4); // Changed from 3 to 4
        pauseLabel.setAlignment(Align.center);

        // Create the Resume Button
        resumeButton = new TextButton("Resume", skin);
        resumeButton.getLabel().setFontScale(2.0f); // Added font scale

        // Create the Quit Button
        quitButton = new TextButton("Quit", skin);
        quitButton.getLabel().setFontScale(2.0f); // Added font scale

        // Add elements to the table (Layout updated from GameMenu2)
        pauseMenuTable.add(pauseLabel).center().padTop(80).padBottom(50).padLeft(100).padRight(100);
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
    }

    /**
     * Makes the pause menu table visible and centers it.
     */
    public void displayPauseMenu() {
        // Recalculate position based on current stage size
        pauseMenuTable.setPosition(
                (stage.getWidth() - pauseMenuTable.getWidth()) / 2f,
                (stage.getHeight() - pauseMenuTable.getHeight()) / 2f
        );
        pauseMenuTable.setVisible(true);
        pauseMenuTable.toFront();
    }

    /**
     * Hides the pause menu table.
     */
    public void hidePauseMenu() {
        pauseMenuTable.setVisible(false);
    }

    /**
     * Returns whether the pause menu is currently visible.
     * @return true if the pause menu table is visible, false otherwise.
     */
    public boolean isPausedDisplayActive() {
        return pauseMenuTable.isVisible();
    }
}