package group9.eng;

import java.util.ArrayList;
import java.util.List;

import group9.eng.components.Component;

public class EntityManager {
    private List<Entity> entities;

    public EntityManager() {
        this.entities = new ArrayList<Entity>();
    }

    public Entity createEntity(Component... components) {
        Entity entity = new Entity(components);
        entities.add(entity);
        return entity;
    }

    public void update() {
        for (Entity e: entities) {
            e.update();
        }
    }

    public void draw() {
        for (Entity e: entities) {
            e.draw();
        }
    }
}
