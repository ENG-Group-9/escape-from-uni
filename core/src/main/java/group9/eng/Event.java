package group9.eng;

import java.util.ArrayList;
import java.util.List;

public class Event {
    private int id;
    private List<Entity> entities;

    public Event(int id) {
        this.id = id;
        entities = new ArrayList<Entity>();
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
