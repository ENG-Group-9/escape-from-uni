package group9.eng;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private final List<Entity> entities;

    public Event(int id) {
        entities = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void update() {
        for (Entity e: entities) {
            System.out.println(e);
        }
    }
}
