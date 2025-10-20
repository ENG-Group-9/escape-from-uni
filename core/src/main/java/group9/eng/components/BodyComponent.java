package group9.eng.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * When added to an Entity, gives it a circular collision shape.
 */
public class BodyComponent extends Component {
    private Body body;
    private float radius;

    /**
     * Creates and initialises the relevant Box2D classes to create a circular collision shape,
     * and adds it to the world.
     * @param physicsWorld The Box2D physics world used by the game.
     * @param x The x position of the shape.
     * @param y The y position of the shape.
     * @param radius The radius of the shape.
     */
    public BodyComponent(World physicsWorld, float x, float y, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        body = physicsWorld.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        this.radius = radius;
        circle.setRadius(this.radius);
        body.createFixture(circle, 0.01f);
        body.setLinearDamping(10f);
        body.setFixedRotation(true);
    }

    /**
     * Provides a reference to the physics body created by this component.
     * @return The physics body created by this component.
     */
    public Body getBody() {
        return body;
    }

    /**
     * Returns the position of this component's body.
     * @return The body position.
     */
    public Vector2 getPosition() {
        return body.getPosition();
    }

    /**
     * Sets the position of this component's body.
     * @param position The position to move the body to.
     */
    public void setPosition(Vector2 position) {
        body.setTransform(position, body.getAngle());
    }

    /**
     * Returns the radius of this component's body.
     * @return The radius of this component's body.
     */
    public float getRadius() {
        return radius;
    }
}
