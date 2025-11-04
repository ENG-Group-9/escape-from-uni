package group9.eng.events;

import java.util.ArrayList;
import java.util.List;

import group9.eng.Entity;
import group9.eng.components.ControlComponent;

public class EventTrigger {
    private final List<Entity> entities;
    private GameEvent event;

    public EventTrigger(GameEvent event) {
        entities = new ArrayList<>();
        this.event = event;
    }

    public void addEntity(Entity entity) {
        if (!check_entity_is_player(entity)) return;
        entities.add(entity);
        event.start(entity);
    }

    public void removeEntity(Entity entity) {
        if (!check_entity_is_player(entity)) return;
        entities.remove(entity);
    }

    public void update() {
        for (Entity e: entities) {
            event.update(e);
        }
    }

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
