package group9.eng.events;

import java.util.ArrayList;
import java.util.List;

import group9.eng.Entity;
import group9.eng.components.ControlComponent;

/**
 * An area that, when an Entity is within it, will cause an event to occur.
 */
public class EventTrigger {
    private final List<Entity> entities;
    private GameEvent event;
    
    /**
     * Creates a new EventTrigger.
     * @param event the GameEvent that will occur when an Entity is within this EventTrigger.
     */
    public EventTrigger(GameEvent event) {
        entities = new ArrayList<>();
        this.event = event;
    }

    /**
     * Should be called when an Entity has just entered this EventTrigger.
     * @param entity the Entity that has just entered this EventTrigger.
     */
    public void addEntity(Entity entity) {
        if (!check_entity_is_player(entity)) return;
        entities.add(entity);
        event.start(entity);
    }

    /**
     * Should be called when an Entity has just exited this EventTrigger.
     * @param entity the Entity that has just exited this EventTrigger.
     */
    public void removeEntity(Entity entity) {
        if (!check_entity_is_player(entity)) return;
        entities.remove(entity);
    }

    /**
     * Will cause the GameEvent associated with this EventTrigger to occur on any Entity within
     * this EventTrigger, if certain conditions defined by the GameEvent are met.
     */
    public void update() {
        for (Entity e: entities) {
            event.update(e);
        }
    }

    /**
     * Checks if an Entity that has entered this EventTrigger is the player (contains a
     * ControlComponent) or not.
     * @param entity the Entity to check.
     * @return is the Entity the player or not?
     */
    private Boolean check_entity_is_player(Entity entity) {
        try {
            entity.getComponent(ControlComponent.class);
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }
}
