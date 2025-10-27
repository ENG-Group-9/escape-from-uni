package group9.eng;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import group9.eng.components.BodyComponent;
import group9.eng.components.ControlComponent;
import group9.eng.components.AnimationComponent;

/**
 * The main game class.
 */
public class GameManager extends ApplicationAdapter {

    private enum GameState {
        SPLASH, GAME
    }

    private GameState currentState = GameState.SPLASH;
    private boolean gameInitialized = false;

    // Splash Screen Variables
    private SpriteBatch splashBatch;
    private Texture splashTexture;
    private OrthographicCamera splashCam;

    // Game World Variables
    private World physicsWorld;
    private Box2DDebugRenderer hitboxDebugRenderer;
    private EntityManager entityManager;
    private Viewport viewport;
    private Camera camera;
    private Map map;
    private EventManager eventManager;

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
        // Initialize Splash Screen components
        splashBatch = new SpriteBatch();
        splashTexture = new Texture(Gdx.files.internal("splash-screen.png"));
        splashTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        splashCam = new OrthographicCamera();
        splashCam.setToOrtho(false);
    }

    /**
     * Initializes all game-related objects.
     * This is called once when transitioning from SPLASH to GAME.
     */
    private void initializeGame() {
        if (!gameInitialized) {
            physicsWorld = new World(new Vector2(0, 0), true);
            hitboxDebugRenderer = new Box2DDebugRenderer();
            viewport = new ExtendViewport(200, 200);
            camera = new Camera(viewport);
            entityManager = new EntityManager();
            eventManager = new EventManager(physicsWorld);
            map = new Map(physicsWorld, viewport);

            // --- Entity Creation ---
            player = entityManager.createEntity(
                new BodyComponent(physicsWorld, 50, 50, 4),
                new ControlComponent(500),
                new AnimationComponent("dummy.png", 16, 16)
                    .add_animation("idle", new Animation(
                        0.0f,
                        new int[] {9},
                        new int[] {3},
                        new int[] {0},
                        new int[] {6}
                    ))
                    .add_animation("walk", new Animation(
                        10.0f,
                        new int[] {10, 9, 11, 9},
                        new int[] {4, 3, 5, 3},
                        new int[] {1, 0, 2, 0},
                        new int[] {7, 6, 8, 6}
                    ))
            );
            camera.setTarget(player);
            
            /*
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
            */
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
            timerLabel.setFontScale(4); // Changed from 2 to 4
            table.add(timerLabel).align(Align.topLeft).pad(10);
            table.row();
            table.add().expand(); // Add an expanding cell below the timer to push it up

            // Create the GameMenu
            gameMenu = new GameMenu(skin, uiStage, this);

            gameInitialized = true;
            camera.update();

            // Ensure viewports are set correctly
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }


    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        switch (currentState) {
            case SPLASH:
                updateSplash(Gdx.graphics.getDeltaTime());
                drawSplash();
                break;
            case GAME:
                if (!gameInitialized) {
                     initializeGame();
                 }
                updateGame(Gdx.graphics.getDeltaTime());
                drawGame();
                break;
        }
    }

    private void updateSplash(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
             currentState = GameState.GAME;
         }
        splashCam.update();
    }

     private void drawSplash() {
         splashCam.update();
         splashBatch.setProjectionMatrix(splashCam.combined);

         splashBatch.begin();

         float screenWidth = Gdx.graphics.getWidth();
         float screenHeight = Gdx.graphics.getHeight();
         float textureWidth = splashTexture.getWidth();
         float textureHeight = splashTexture.getHeight();

         float scale = Math.min(screenWidth / textureWidth, screenHeight / textureHeight);
         float drawWidth = textureWidth * scale;
         float drawHeight = textureHeight * scale;
         float x = (screenWidth - drawWidth) / 2;
         float y = (screenHeight - drawHeight) / 2;

         splashBatch.draw(splashTexture, x, y, drawWidth, drawHeight);
         splashBatch.end();
     }


    private void updateGame(float delta) {
        if (!gameInitialized) return;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
           togglePause();
        }

        if (!isPaused) {
             if (timeTracker != null) {
                 timeTracker.update(delta);
                 if (timeTracker.isTimeUp()) {
                     // TODO: Game over
                 }
            }
            if (physicsWorld != null) physicsWorld.step(delta, 6, 2);
            if (entityManager != null) entityManager.update();
            if (eventManager != null) eventManager.update();
        }

        if (camera != null) camera.update();

        if (timerLabel != null && timeTracker != null) {
            timerLabel.setText(timeTracker.getFormattedTime());
        }
        if (uiStage != null) uiStage.act(delta);
    }

    private void drawGame() {
        if (!gameInitialized) return;

        // --- Draw Game World ---
        if (viewport != null) viewport.apply();
        if (map != null) map.draw();
        if (hitboxDebugRenderer != null && physicsWorld != null && viewport != null) {
            hitboxDebugRenderer.render(physicsWorld, viewport.getCamera().combined);
        }
        if (entityManager != null) entityManager.draw(viewport);

        // --- Draw UI ---
        if (uiStage != null) {
            uiStage.getViewport().apply();
            uiStage.draw();
         }
    }

    public void resumeGame() {
        if (isPaused) {
            togglePause();
        }
    }

    private void togglePause() {
        if (!gameInitialized) return;

         isPaused = !isPaused;
            if (isPaused) {
                if (timeTracker != null) timeTracker.pause();
                if (gameMenu != null) gameMenu.displayPauseMenu();
            } else {
                if (timeTracker != null) timeTracker.resume();
                if (gameMenu != null) gameMenu.hidePauseMenu();
            }
    }


    @Override
    public void resize(int width, int height) {
        splashCam.setToOrtho(false, width, height);

        if (currentState == GameState.GAME && gameInitialized) {
            if (viewport != null) viewport.update(width, height, true);
            if (uiStage != null) uiStage.getViewport().update(width, height, true);

            if (gameMenu != null && gameMenu.isPausedDisplayActive()) {
                 gameMenu.displayPauseMenu();
            }
        }
    }

    @Override
    public void dispose() {
        if (splashBatch != null) splashBatch.dispose();
        if (splashTexture != null) splashTexture.dispose();

        if (gameInitialized) {
            if (physicsWorld != null) physicsWorld.dispose();
            if (hitboxDebugRenderer != null) hitboxDebugRenderer.dispose();
            if (skin != null) skin.dispose();
            if (uiStage != null) uiStage.dispose();
        }
    }
}