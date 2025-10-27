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
 * Handles the UI display of any in-game event dialogue.
 * Creates a reusable pop-up containing a message with a clickable "Ok" button to discard it.
 */
public class EventDialogue {
    private final Table dialogueTable;
    private final Label messageLabel;
    private final Stage stage;

    /** Creates a new EventDialogue with the specified Skin and Stage.
     * The dialogue is hidden until activated.
     *
     * @param skin the {@link Skin} which is used for a consistent style across the game.
     * @param stage the {@link Stage} the dialogue is added to.
     */
    public EventDialogue(Skin skin, Stage stage) {
        this.stage = stage;

        dialogueTable = new Table();
        dialogueTable.setBackground(skin.getDrawable("default-window"));
        dialogueTable.setVisible(false);
        stage.addActor(dialogueTable);

        messageLabel = new Label("", skin);
        messageLabel.setFontScale(2f);
        messageLabel.setAlignment(Align.center);
        messageLabel.setWrap(true);

        TextButton okButton = new TextButton("OK", skin);
        okButton.getLabel().setFontScale(2f);
        okButton.addListener(new ChangeListener() {
            @Override // listener hides dialogue when button is clicked
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });

        dialogueTable.add(messageLabel).width(400).pad(40).row();
        dialogueTable.add(okButton).size(200, 60).pad(20);
        dialogueTable.pack();
    }

    /**
     * Used to display the given text dialogue message.
     * The dialogue is centred in the middle of the screen on the top layer.
     *
     * @param message the text message displayed to the user.
     */
    public void show(String message) {
        messageLabel.setText(message);
        dialogueTable.setVisible(true);
        dialogueTable.toFront();

        dialogueTable.setPosition(
            (stage.getWidth() - dialogueTable.getWidth()) / 2f,
            (stage.getHeight() - dialogueTable.getHeight()) / 2f
        );
    }

    /**
     * Hides the dialogue.
     */
    public void hide() {
        dialogueTable.setVisible(false);
    }
}
