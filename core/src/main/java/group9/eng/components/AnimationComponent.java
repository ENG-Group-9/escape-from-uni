package group9.eng.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import group9.eng.Animation;
import group9.eng.Entity;
import group9.eng.Spritesheet;

public class AnimationComponent extends Component {
    private Body body;
    private BodyComponent bodyComponent;
    private Spritesheet spritesheet;

    private Map<String, Animation> animations;
    private String currentAnimation;

    private Vector2 direction;

    public AnimationComponent(String filename, int tileWidth, int tileHeight) {
        spritesheet = new Spritesheet(filename, tileWidth, tileHeight);
        animations = new HashMap<String, Animation>();
        direction = new Vector2(1, -1);
    }
    
    @Override
    public void setEntity(Entity entity) {
        super.setEntity(entity);
        bodyComponent = (BodyComponent) entity.getComponent(BodyComponent.class);
        body = bodyComponent.getBody();
    }

    @Override
    public void update() {
        animations.get(currentAnimation).update();
    }

    @Override
    public void draw(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        pos.x -= spritesheet.getTileWidth() / 2;
        pos.y -= bodyComponent.getRadius();
        animations.get(currentAnimation).setAngle(direction.angleDeg());
        batch.draw(
            spritesheet.get(animations.get(currentAnimation).getIndex()),
            pos.x,
            pos.y
        );
    }

    public AnimationComponent add_animation(String name, Animation animation) {
        animations.put(name, animation);
        if (currentAnimation == null) currentAnimation = name;
        return this;
    }

    public void setDirection(Vector2 v) {
        if (v.x != 0) direction.x = v.x;
        if (v.y != 0) direction.y = v.y;
    }

    public void setAnimation(String name) {
        String previousAnimation = currentAnimation;
        currentAnimation = name;
        if (previousAnimation != currentAnimation) animations.get(name).reset();
    }
}
