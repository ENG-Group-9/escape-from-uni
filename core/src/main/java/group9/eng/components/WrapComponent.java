package group9.eng.components;

import com.badlogic.gdx.math.Vector2;

import group9.eng.Entity;

public class WrapComponent extends Component {
    private BodyComponent bodyComponent;
    private float radius;
    
    @Override
    public void setEntity(Entity entity) {
        super.setEntity(entity);
        bodyComponent = (BodyComponent) entity.getComponent(BodyComponent.class);
        radius = bodyComponent.getRadius();
    }

    private float mod(float a, float b) {
        return ((a % b) + b) % b;
    }

    @Override
    public void update() {
        Vector2 pos = bodyComponent.getPosition();
        bodyComponent.setPosition(new Vector2(
            mod(pos.x + radius, 100 + radius * 2) - radius,
            mod(pos.y + radius, 100 + radius * 2) - radius
        ));
    }
}
