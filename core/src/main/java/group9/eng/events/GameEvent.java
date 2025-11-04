package group9.eng.events;

import com.badlogic.gdx.Gdx;

import group9.eng.Entity;
import group9.eng.ScoreTracker;

public class GameEvent {
    private String message;
    private int points;
    private Boolean repeat;
    private Boolean alreadyTriggered;
    private double chance;
    private int when;
    private double timer;
    private double updatePeriod;

    private EventDialogue dialogue;
    private ScoreTracker scoreTracker;

    public GameEvent(
        String message,
        int points,
        Boolean repeat,
        double chance,
        int when,
        double updatePeriod,
        EventDialogue dialogue,
        ScoreTracker scoreTracker
    ) {
        this.message = message;
        this.points = points;
        this.repeat = repeat;
        alreadyTriggered = false;
        this.chance = chance;

        this.when = when;
        this.updatePeriod = updatePeriod;
        timer = updatePeriod;

        this.dialogue = dialogue;
        this.scoreTracker = scoreTracker;
    }

    public void start(Entity entity) {
        if (when == 0) apply(entity);
    }

    public void end(Entity entity) {
        if (when == 2) apply(entity);
    }

    public void update(Entity entity) {
        if (when != 1) return;

        if (timer > 0) {
            timer -= Gdx.graphics.getDeltaTime();
        }
        if (timer <= 0) {
            timer += updatePeriod;
            apply(entity);
        }
    }

    private void apply(Entity entity) {
        if (Math.random() >= chance) return;
        if (alreadyTriggered) return;
        if (!repeat) alreadyTriggered = true;
        dialogue.show(message);
        scoreTracker.update(points);
    }
}
