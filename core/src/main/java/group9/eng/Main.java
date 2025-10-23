package group9.eng;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align; 
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import group9.eng.components.BodyComponent;
import group9.eng.components.ControlComponent;
import group9.eng.components.JiggleComponent;

/**
 * The main game class, acting as GameManager for now.
 */
public class Main extends ApplicationAdapter {
    private World physicsWorld;
    private Box2DDebugRenderer hitboxDebugRenderer;
    private EntityManager entityManager;
    private Viewport viewport;
    private Camera camera;
    private Map map;

    private Entity player;

    // UI Variables
    private Stage uiStage;
    private Skin skin;
    private Label timerLabel;

    // Logic Managers
    private TimeTracker timeTracker;
    private GameMenu gameMenu;

    // Game State
    private boolean isPaused = false;

    @Override
    public void create() {
        physicsWorld = new World(new Vector2(0, 0), true);
        hitboxDebugRenderer = new Box2DDebugRenderer();
        viewport = new FitViewport(200, 200);
        camera = new Camera(viewport);
        entityManager = new EntityManager();
        map = new Map(physicsWorld, viewport);

        // --- Entity Creation ---
        player = entityManager.createEntity(
                new BodyComponent(physicsWorld, 50, 50, 5),
                new ControlComponent(500)
        );
        camera.setTarget(player);
        for (int i = 0; i < 50; i++) {
            entityManager.createEntity(
                new BodyComponent(
                    physicsWorld,
                    (float)Math.random() * map.getWidth(),
                    (float)Math.random() * map.getHeight(),
                    (float)Math.random() * 2 + 1
                ),
                new JiggleComponent(500)
            );
        }
        // ----------------------

        timeTracker = new TimeTracker(300f);

        // --- UI Setup ---
        uiStage = new Stage(new ScreenViewport()); // UI uses screen coordinates
        Gdx.input.setInputProcessor(uiStage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create and configure the root table
        Table table = new Table();
        table.setFillParent(true); // Make the table fill the stage
        uiStage.addActor(table); // Add the table to the stage

        // Timer Label setup using the table
        timerLabel = new Label("05:00", skin);
        timerLabel.setFontScale(2);
        table.add(timerLabel).align(Align.topLeft).pad(10);
        table.row(); 
        table.add().expand(); // Add an expanding cell below the timer to push it up


        // Create the GameMenu 
        gameMenu = new GameMenu(skin, uiStage, this);

    }

    @Override
    public void render() {
        update();
        draw();
    }

    private void update() {
        float delta = Gdx.graphics.getDeltaTime();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
           togglePause();
        }

        if (!isPaused) {
            timeTracker.update(delta);
            physicsWorld.step(delta, 6, 2);
            entityManager.update();
            if (timeTracker.isTimeUp()) {
                // TODO: Game over
            }
        }

        camera.update();

        timerLabel.setText(timeTracker.getFormattedTime());
        uiStage.act(delta);
    }

    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // --- Draw Game World ---
        viewport.apply();
        map.draw();
        hitboxDebugRenderer.render(physicsWorld, viewport.getCamera().combined);

        // --- Draw UI ---
        uiStage.getViewport().apply();
        uiStage.draw();
    }

    public void resumeGame() {
        if (isPaused) {
            togglePause();
        }
    }

    private void togglePause() {
         isPaused = !isPaused;
            if (isPaused) {
                timeTracker.pause();
                gameMenu.displayPauseMenu();
                System.out.println("Game Paused");
            } else {
                timeTracker.resume();
                gameMenu.hidePauseMenu();
                System.out.println("Game Resumed");
            }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiStage.getViewport().update(width, height, true);

         if (gameMenu != null && gameMenu.isPausedDisplayActive()) {
            gameMenu.displayPauseMenu();
         }
    }

    @Override
    public void dispose() {
        if (physicsWorld != null) physicsWorld.dispose();
        if (hitboxDebugRenderer != null) hitboxDebugRenderer.dispose();
        if (skin != null) skin.dispose();
        if (uiStage != null) uiStage.dispose();
    }
}

