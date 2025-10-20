package group9.eng.components;

import group9.eng.Entity;

/**
 * Contains functionality. Can be combined together within an Entity to create behaviour in a
 * modular way.
 */
public class Component {
    protected Entity entity;
    
    public Component() {}
    
    /**
     * Should be called every frame, functionality should be contained within this method.
     */
    public void update() {}

    /**
     * Should be called every frame, anything that results in drawing to the screen should be
     * contained within this method.
     */
    public void draw() {}

    /**
     * Gives this Component a reference to the Entity it is contained within. Should be called as
     * soon as possible after the Entity is created.
     * @param entity The Entity that contains this Component.
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
