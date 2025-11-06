package group9.eng.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import group9.eng.Entity;

/**
 * When added to an Entity, allows it to be moved around using the WASD keys.
 */
public class ControlComponent extends Component {
    private Body body;
    private float speed;
    private float multiplier;
    private AnimationComponent animationComponent;

    /**
     * Creates a new ControlComponent.
     * @param speed The speed that the Entity will move when the WASD keys are pressed.
     */
    public ControlComponent(float speed) {
        this.speed = speed;
        this.multiplier = 1.0f;
    }
    
    @Override
    public void setEntity(Entity entity) {
        super.setEntity(entity);
        body = ((BodyComponent) entity.getComponent(BodyComponent.class)).getBody();

        try {
            animationComponent = (AnimationComponent) entity.getComponent(AnimationComponent.class);
        }
        catch (RuntimeException e) {}
    }

    @Override
    public void update() {
        Vector2 v = new Vector2(
            (Gdx.input.isKeyPressed(Keys.D) ? 1 : 0) - (Gdx.input.isKeyPressed(Keys.A) ? 1 : 0),
            (Gdx.input.isKeyPressed(Keys.W) ? 1 : 0) - (Gdx.input.isKeyPressed(Keys.S) ? 1 : 0)
        ).nor().scl(speed * multiplier * body.getMass());

        body.applyForceToCenter(v, true);

        if (animationComponent != null) {
            if (v.isZero()) {
                animationComponent.setAnimation("idle");
            }
            else {
                animationComponent.setAnimation("walk");
                animationComponent.setDirection(v);
            }
        }

        if (multiplier > 1) {
            multiplier -= 0.0002;
            multiplier = Math.max(1, multiplier);
        }
    }

    public void addSpeedMultiplier(double factor) {
        multiplier += factor;
    }
}
