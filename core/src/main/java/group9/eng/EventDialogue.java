package group9.eng;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class EventDialogue {
    private final Table dialogueTable;
    private final Label messageLabel;
    private final Stage stage;
    
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
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });
        
        dialogueTable.add(messageLabel).width(400).pad(40).row();
        dialogueTable.add(okButton).size(200, 60).pad(20);
        dialogueTable.pack();
    }

    public void show(String message) {
        messageLabel.setText(message);
        dialogueTable.setVisible(true);
        dialogueTable.toFront();
  
        dialogueTable.setPosition(
            (stage.getWidth() - dialogueTable.getWidth()) / 2f,
            (stage.getHeight() - dialogueTable.getHeight()) / 2f
        );
    }

    public void hide() {
        dialogueTable.setVisible(false);
    }
}