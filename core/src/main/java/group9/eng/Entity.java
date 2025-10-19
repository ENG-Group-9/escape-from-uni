package group9.eng;

import java.util.ArrayList;
import java.util.List;

import group9.eng.components.Component;

public class Entity {
    private List<Component> components;

    public Entity(Component... components) {
        this.components = new ArrayList<Component>();
        for (Component c: components) {
            this.components.add(c);
        }
        for (Component c: this.components) {
            c.setEntity(this);
        }
    }

    public void update() {
        for (Component c: components) {
            c.update();
        }
    }

    public void draw() {
        
    }

    public Component getComponent(Class<?> componentType) {
        for (Component c: components) {
            if (c.getClass() == componentType) {
                return c;
            }
        }
        throw new RuntimeException(String.format("%s has no %s component", this, componentType));
    }
}
