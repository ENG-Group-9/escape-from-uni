package group9.eng.components;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import group9.eng.Animation;
import group9.eng.Entity;
import group9.eng.Spritesheet;

/**
 * When added to an Entity, gives it an animated sprite that can change depending on the movement
 * direction of the Entity's BodyComponent (if it has one).
 */
public class AnimationComponent extends Component {
    private Body body;
    private BodyComponent bodyComponent;
    private Spritesheet spritesheet;

    private Map<String, Animation> animations;
    private String currentAnimation;

    private Vector2 direction;

    /**
     * Creates a new AnimationComponent.
     * @param filename the filename of the spritesheet to use for animations.
     * @param tileWidth the width of each frame of the animation.
     * @param tileHeight the height of each frame of the animation.
     */
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

    /**
     * Adds an Animation to this component.
     * @param name the name to identify this animation by.
     * @param animation an Animation.
     * @return this AnimationComponent, to make instantiating AnimationComponents cleaner and
     *         easier.
     */
    public AnimationComponent add_animation(String name, Animation animation) {
        animations.put(name, animation);
        if (currentAnimation == null) currentAnimation = name;
        return this;
    }

    /**
     * Update the direction vector of this component.
     * @param v a direction vector.
     */
    public void setDirection(Vector2 v) {
        if (v.x != 0) direction.x = v.x;
        if (v.y != 0) direction.y = v.y;
    }

    /**
     * Update the current animation being played.
     * @param name the name of the animation to use.
     */
    public void setAnimation(String name) {
        String previousAnimation = currentAnimation;
        currentAnimation = name;
        if (previousAnimation != currentAnimation) animations.get(name).reset();
    }
}
