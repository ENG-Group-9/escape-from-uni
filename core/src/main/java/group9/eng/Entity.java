package group9.eng;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import group9.eng.components.Component;

/**
 * A container for components.
 */
public class Entity {
    private List<Component> components;

    /**
     * Initialises the Entity using the list of components provided.
     * @param components A list of components used by this Entity.
     */
    public Entity(Component... components) {
        this.components = new ArrayList<Component>();
        for (Component c: components) {
            this.components.add(c);
        }
        for (Component c: this.components) {
            c.setEntity(this);
        }
    }

    /**
     * Should be called every frame, calls the update method of every component used by this
     * Entity.
     */
    public void update() {
        for (Component c: components) {
            c.update();
        }
    }

    /**
     * Should be called every frame, calls the draw method of every component used by this Entity.
     */
    public void draw(SpriteBatch batch) {
        for (Component c: components) {
            c.draw(batch);
        }
    }
    
    /**
     * Accesses a component from this Entity with the given type. The returned component should be
     * casted to allow for the unique methods of each component type to be used.
     * @param componentType The component type to be accessed.
     * @return A component of the requested type.
     * @throws RuntimeException When this Entity doesn't contain the requested component type.
     */
    public Component getComponent(Class<?> componentType) {
        for (Component c: components) {
            if (c.getClass() == componentType) {
                return c;
            }
        }
        throw new RuntimeException(String.format("%s has no %s component", this, componentType));
    }
}
