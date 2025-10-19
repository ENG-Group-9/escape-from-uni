package group9.eng.components;

import group9.eng.Entity;

public class Component {
    public Entity entity;

    public Component() {}

    public void update() {}

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
