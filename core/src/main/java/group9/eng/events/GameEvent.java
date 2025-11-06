package group9.eng.events;

import com.badlogic.gdx.Gdx;

import group9.eng.Entity;
import group9.eng.ScoreTracker;
import group9.eng.components.ControlComponent;

public class GameEvent {
    private String message;
    private int points;
    private Boolean repeat;
    private Boolean alreadyTriggered;
    private double chance;
    private int when;
    private int eventType;
    private double timer;
    private double updatePeriod;
    private double speedBoost;

    private EventDialogue dialogue;
    private ScoreTracker scoreTracker;
    private EventCompletionTracker eventCompletionTracker;

    public GameEvent(
        String message,
        int points,
        Boolean repeat,
        double chance,
        int when,
        double updatePeriod,
        double speedBoost,
        int eventType, // "0" hidden, "1" positive, "2" negative, "3" null/other
        EventDialogue dialogue,
        ScoreTracker scoreTracker,
        EventCompletionTracker eventCompletionTracker
        
    ) {
        this.message = message;
        this.points = points;
        this.repeat = repeat;
        alreadyTriggered = false;
        this.chance = chance;
        this.speedBoost = speedBoost;

        this.when = when;
        this.updatePeriod = updatePeriod;
        timer = updatePeriod;

        this.dialogue = dialogue;
        this.scoreTracker = scoreTracker;
        this.eventType = eventType;
        this.eventCompletionTracker = eventCompletionTracker;
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
        if (speedBoost != 0) {
            ((ControlComponent) entity.getComponent(ControlComponent.class)).addSpeedMultiplier(speedBoost);
        }
        if (eventType != 3) eventCompletionTracker.AddEventCompletionData(eventType);
    }
}
