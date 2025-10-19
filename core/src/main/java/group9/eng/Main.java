package group9.eng;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import group9.eng.components.BodyComponent;
import group9.eng.components.ControlComponent;
import group9.eng.components.JiggleComponent;
import group9.eng.components.WrapComponent;

public class Main extends ApplicationAdapter {
    private World physicsWorld;
    private Box2DDebugRenderer hitboxDebugRenderer;

    private EntityManager entityManager;

    private Viewport viewport;

    @Override
    public void create() {
        physicsWorld = new World(new Vector2(0, 0), true);
        hitboxDebugRenderer = new Box2DDebugRenderer();

        viewport = new FitViewport(100, 100);

        entityManager = new EntityManager();
        entityManager.createEntity(
            new BodyComponent(physicsWorld, 50, 50, 5),
            new ControlComponent(500),
            new WrapComponent()
        );
        for (int i = 0; i < 50; i++) {
            entityManager.createEntity(
                new BodyComponent(
                    physicsWorld,
                    (float)Math.random() * 100,
                    (float)Math.random() * 100,
                    (float)Math.random() * 2 + 1
                ),
                new JiggleComponent(500),
                new WrapComponent()
            );
        }
    }

    @Override
    public void render() {
        update();
        draw();
    }

    private void update() {
        physicsWorld.step(Gdx.graphics.getDeltaTime(), 6, 2);
        entityManager.update();
    }

    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();

        hitboxDebugRenderer.render(physicsWorld, viewport.getCamera().combined);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        physicsWorld.dispose();
    }
}
