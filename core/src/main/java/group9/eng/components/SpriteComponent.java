package group9.eng.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import group9.eng.Entity;

public class SpriteComponent extends Component {
    private Body body;
    private BodyComponent bodyComponent;
    private Texture texture;
    private Sprite sprite;

    public SpriteComponent() {
        texture = new Texture(Gdx.files.internal("dummy.png"));
        sprite = new Sprite(texture);
    }
    
    @Override
    public void setEntity(Entity entity) {
        super.setEntity(entity);
        bodyComponent = (BodyComponent) entity.getComponent(BodyComponent.class);
        body = bodyComponent.getBody();
    }

    @Override
    public void draw(SpriteBatch batch) {
        Vector2 pos = body.getPosition();
        pos.x -= sprite.getWidth() / 2;
        pos.y -= bodyComponent.getRadius();
        sprite.setPosition(pos.x, pos.y);
        sprite.draw(batch);
    }
}
