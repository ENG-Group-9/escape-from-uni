package group9.eng.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.World;

public class BodyComponent extends Component {
    private Body body;
    private float radius;

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

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void setPosition(Vector2 position) {
        body.setTransform(position, body.getAngle());
    }

    public float getRadius() {
        return radius;
    }
}
