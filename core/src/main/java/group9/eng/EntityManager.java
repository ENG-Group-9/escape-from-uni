package group9.eng;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import group9.eng.components.Component;

/**
 * Contains entities and allows for them to be created.
 */
public class EntityManager {
    private List<Entity> entities;
    private SpriteBatch entityBatch;

    /**
     * Initialises the entity manager, creating a list to store all of the entities.
     */
    public EntityManager() {
        entities = new ArrayList<Entity>();
        entityBatch = new SpriteBatch();
    }

    /**
     * Creates a new Entity given a list of components, adds it to this EntityManager and returns
     * it.
     * @param components A list of components that will make up the new Entity.
     * @return A newly created Entity.
     */
    public Entity createEntity(Component... components) {
        Entity entity = new Entity(components);
        entities.add(entity);
        return entity;
    }

    /**
     * Should be called every frame, calls the update method of all entities.
     */
    public void update() {
        for (Entity e: entities) {
            e.update();
        }
    }

    /**
     * Should be called every frame, calls the draw method of all entities.
     */
    public void draw(Viewport viewport) {
        entityBatch.setProjectionMatrix(viewport.getCamera().combined);
        entityBatch.begin();
        for (Entity e: entities) {
            e.draw(entityBatch);
        }
        entityBatch.end();
    }
}
