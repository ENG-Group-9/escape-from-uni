package group9.eng.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import group9.eng.Entity;

/**
 * A Component for testing, randomly moves an Entity around.
 */
public class JiggleComponent extends Component {
    private Body body;
    private float speed;

    public JiggleComponent(float speed) {
        this.speed = speed;
    }
    
    @Override
    public void setEntity(Entity entity) {
        super.setEntity(entity);
        body = ((BodyComponent) entity.getComponent(BodyComponent.class)).getBody();
    }

    @Override
    public void update() {
        body.applyForceToCenter(
            new Vector2(
                (float)Math.random() * 2 - 1,
                (float)Math.random() * 2 - 1
            ).nor().scl(speed * body.getMass()),
            true
        );
    }
}
