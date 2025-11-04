package group9.eng;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align; // Ensure Align is imported
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import group9.eng.components.AnimationComponent;
import group9.eng.components.BodyComponent;
import group9.eng.components.ControlComponent;
import group9.eng.events.EventDialogue;
// import group9.eng.components.JiggleComponent; // JiggleComponent was in the original but not used, uncomment if needed
import group9.eng.events.EventManager;

/**
 * The main game class.
 */
public class GameManager extends ApplicationAdapter {

    private enum GameState {
        SPLASH, FADING_OUT, GAME, FADING_IN
    }

    private GameState currentState = GameState.SPLASH;
    private boolean gameInitialised = false;

    // Splash Screen Variables
    private SpriteBatch splashBatch;
    private Texture splashTexture;
    private OrthographicCamera splashCam;

    private Texture csBuildingTexture;

    // Game World Variables
    private World physicsWorld;
    private Box2DDebugRenderer hitboxDebugRenderer;
    private EntityManager entityManager;
    private Viewport viewport;
    private Camera camera;
    private Map map;
    private EventManager eventManager; // Keep the field

    private Entity player;

    // UI Variables
    private Stage uiStage;
    private Skin skin;
    private Label timerLabel;
    private Label scoreLabel;
    private EventDialogue eventDialogue;

    // Logic Managers
    private TimeTracker timeTracker;
    private ScoreTracker scoreTracker;
    private GameMenu gameMenu;
    private TimeTable timeTable;

    // Game State
    private boolean isPaused = false;

    // Fade Transition Variables
    private final float fadeDuration = 0.8f;
    private float fadeTimer = 0.0f;
    private float fadeAlpha = 1.0f;
    private SpriteBatch fadeBatch;
    private Texture blackPixel;

    @Override
    public void create() {
        // Initialise Splash Screen components
        splashBatch = new SpriteBatch();
        splashTexture = new Texture(Gdx.files.internal("splash-screen.png"));
        splashTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        splashCam = new OrthographicCamera();
        splashCam.setToOrtho(false);

        fadeBatch = new SpriteBatch();
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.fill();
        blackPixel = new Texture(pixmap);
        pixmap.dispose();
    }

    /**
     * Initialises all game-related objects.
     * This is called once when transitioning from SPLASH to GAME.
     */
    private void initialiseGame() {
        if (!gameInitialised) {
            physicsWorld = new World(new Vector2(0, 0), true);
            hitboxDebugRenderer = new Box2DDebugRenderer();
            viewport = new ExtendViewport(200, 200);
            camera = new Camera(viewport);
            entityManager = new EntityManager();

            // --- UI Setup ---
            uiStage = new Stage(new ScreenViewport()); // UI uses screen coordinates
            Gdx.input.setInputProcessor(uiStage);
            skin = new Skin(Gdx.files.internal("uiskin.json"));
            csBuildingTexture = new Texture(Gdx.files.internal("ComputerSciencePixel.png"));

            // Event system setup
            timeTable = new TimeTable();
            eventManager = new EventManager(physicsWorld);
            eventDialogue = new EventDialogue(skin, uiStage);

            timeTracker = new TimeTracker(300f);
            scoreTracker = new ScoreTracker(); // Initialise score tracker

            map = new Map(physicsWorld, viewport, eventDialogue, scoreTracker); // Needs to be initialised before EventManager if EventManager uses map dimensions

            // --- Entity Creation ---
            player = entityManager.createEntity(
                new BodyComponent(physicsWorld, 200, 50, 4),
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

            // Create and configure the root table for UI
            Table table = new Table();
            table.setFillParent(true); // Make the table fill the stage
            uiStage.addActor(table); // Add the table to the stage

            // Timer Label setup using the table
            timerLabel = new Label("05:00", skin);
            timerLabel.setFontScale(4);

            // Score Label setup
            scoreLabel = new Label("Score: 0", skin);
            scoreLabel.setFontScale(4);

            // Add widgets to the table
            table.add(timerLabel).align(Align.topLeft).pad(10);
            table.add().expandX(); // Add an expanding cell to push timer and score apart
            table.add(scoreLabel).align(Align.topRight).padTop(10).padRight(30);
            table.row(); // Move to the next row
            table.add().expand(); // Add an expanding cell to push the top row up

            // Create the GameMenu
            gameMenu = new GameMenu(skin, uiStage, this); // Pass this GameManager instance

            gameInitialised = true;
            camera.update();

            // Ensure viewports are set correctly
            currentState = GameState.FADING_IN;
            fadeTimer = 0.0f;
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateBasedOnState(Gdx.graphics.getDeltaTime());
        drawBasedOnState();
        drawFadeOverlay();
    }

    private void updateBasedOnState(float delta) {
        switch (currentState) {
            case SPLASH:
                updateSplash();
                break;
            case FADING_OUT:
                updateFadeOut(delta);
                break;
            case GAME:
            case FADING_IN:
                if (!gameInitialised) {
                     initialiseGame();
                } // Fall through to updateGame even if just initialised
                updateGame(delta); // Always update game logic if not splash/fading out
                break;
        }
         // Update UI stage regardless of pause state for menu interactions
        if (uiStage != null) uiStage.act(delta);
    }

    private void drawBasedOnState() {
         switch (currentState) {
            case SPLASH:
            case FADING_OUT:
                drawSplash();
                break;
            case GAME:
            case FADING_IN:
                if (gameInitialised) {
                    drawGame();
                }
                break;
        }
         // Draw UI stage on top of everything else (except fade)
        if (uiStage != null && gameInitialised) { // Only draw UI if game is initialised
             uiStage.getViewport().apply(); // Apply UI viewport
             uiStage.draw();
         }
    }

    private void updateSplash() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
             currentState = GameState.FADING_OUT;
             fadeTimer = 0.0f;
             fadeAlpha = 1.0f; // Start fade from black
         }
        splashCam.update();
    }

    private void updateFadeOut(float delta) {
        fadeTimer += delta;
        // Fade out the splash screen
        fadeAlpha = Interpolation.fade.apply(1.0f, 0.0f, Math.min(fadeTimer / fadeDuration, 1.0f));

        if (fadeTimer >= fadeDuration) {
             initialiseGame(); // Initialise game components
        }
        splashCam.update();
    }

     private void drawSplash() {
         splashCam.update();
         splashBatch.setProjectionMatrix(splashCam.combined);

         Color c = splashBatch.getColor();
         splashBatch.setColor(c.r, c.g, c.b, fadeAlpha);

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

         splashBatch.setColor(c.r, c.g, c.b, 1f);
     }


    private void updateGame(float delta) {
        if (currentState == GameState.FADING_IN) {
            fadeTimer += delta;
            // Fade in the game view
            fadeAlpha = Interpolation.fade.apply(1.0f, 0.0f, Math.min(fadeTimer / fadeDuration, 1.0f));
            if (fadeTimer >= fadeDuration) {
                currentState = GameState.GAME;
                fadeAlpha = 0.0f; // Fully faded in
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
           togglePause();
        }

        // Only update game world logic if not paused and game is running or fading in
        if (!isPaused && (currentState == GameState.GAME || currentState == GameState.FADING_IN)) {
             if (timeTracker != null) {
                 timeTracker.update(delta);
                 if (timeTracker.isTimeUp()) {
                     // TODO: Game over logic
                 }
            }
            if (physicsWorld != null) physicsWorld.step(delta, 6, 2);
            if (entityManager != null) entityManager.update();

            // Event system updates
            if (eventManager != null) eventManager.update();
            if (camera != null) camera.update(); // Update camera if not paused
        }

        if (camera != null && !isPaused) camera.update();

        // Update timer label text
        if (timerLabel != null && timeTracker != null) {
            timerLabel.setText(timeTracker.getFormattedTime());
        }

        // Update score label text
        if (scoreLabel != null && scoreTracker != null) {
            scoreLabel.setText("Score: " + scoreTracker.getScore());
        }

        if (uiStage != null) uiStage.act(delta);
    }

    private void drawGame() {
        if (!gameInitialised) return;

        // --- Draw Game World ---
        if (viewport != null) viewport.apply(); // Apply game viewport
        if (map != null) map.draw();

        // Optional Debug Renderer
        // if (hitboxDebugRenderer != null && physicsWorld != null && viewport != null) {
        //     hitboxDebugRenderer.render(physicsWorld, viewport.getCamera().combined);
        // }

        if (entityManager != null && viewport != null) entityManager.draw(viewport);
    }

    // Draws the black overlay during FADING_IN state
    private void drawFadeOverlay() {
        if (currentState == GameState.FADING_IN && fadeAlpha > 0.0f) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            fadeBatch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            fadeBatch.begin();
            Color c = fadeBatch.getColor();
            fadeBatch.setColor(c.r, c.g, c.b, fadeAlpha);
            fadeBatch.draw(blackPixel, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            fadeBatch.end();
            fadeBatch.setColor(c.r, c.g, c.b, 1f);

            Gdx.gl.glDisable(GL20.GL_BLEND);        }
    }

    public void resumeGame() {
        if (isPaused) {
            togglePause();
        }
    }

    private void togglePause() {
        // Allow pausing only when fully in the GAME state
        if (!gameInitialised || currentState != GameState.GAME) return;

         isPaused = !isPaused;
            if (isPaused) {
                if (timeTracker != null) timeTracker.pause();

                if (gameMenu != null) {
                     gameMenu.displayPauseMenu();
                }
            } else {
                if (timeTracker != null) timeTracker.resume();
                if (gameMenu != null) gameMenu.hidePauseMenu();
            }
    }


    @Override
    public void resize(int width, int height) {
        // Update splash camera viewport regardless of state
        if (splashCam != null) splashCam.setToOrtho(false, width, height);

        // Update game and UI viewports only if the game is initialised
        if (gameInitialised) {
            if (viewport != null) viewport.update(width, height, true); // Update game world viewport
            if (uiStage != null) uiStage.getViewport().update(width, height, true); // Update UI viewport

             // If paused when resizing, redisplay menus correctly
            if (gameMenu != null && isPaused) { // Check isPaused flag
                 gameMenu.displayPauseMenu(); // Call without arguments to reposition
            }
        }
    }
    
    /**
     * Provides a reference to the ScoreTracker.
     * @return The game's ScoreTracker instance.
     */
    public ScoreTracker getScoreTracker() {
        return scoreTracker;
    }

    @Override
    public void dispose() {
        if (splashBatch != null) splashBatch.dispose();
        if (splashTexture != null) splashTexture.dispose();
        if (csBuildingTexture != null) csBuildingTexture.dispose();
        if (fadeBatch != null) fadeBatch.dispose();
        if (blackPixel != null) blackPixel.dispose();

        if (gameInitialised) {
            if (physicsWorld != null) physicsWorld.dispose();
            if (hitboxDebugRenderer != null) hitboxDebugRenderer.dispose();
            if (skin != null) skin.dispose();
            if (uiStage != null) uiStage.dispose();

        }
    }
}