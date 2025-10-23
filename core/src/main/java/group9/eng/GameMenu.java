package group9.eng;

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
        pauseLabel.setFontScale(3);
        pauseLabel.setAlignment(Align.center);

        // Create the Resume Button
        resumeButton = new TextButton("Resume", skin);

        // Add elements to the table
        pauseMenuTable.add(pauseLabel).center().pad(40); // Add padding around label
        pauseMenuTable.row(); // Move to next row
        pauseMenuTable.add(resumeButton).center().size(200, 50).pad(10).padBottom(30);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Tell the Main game class to resume
                mainGame.resumeGame();
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

