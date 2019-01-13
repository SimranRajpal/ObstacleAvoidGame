package com.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.assets.RegionNames;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.entity.Background;
import com.obstacleavoid.entity.Obstacle;
import com.obstacleavoid.entity.Player;
import com.obstacleavoid.util.GdxUtils;
import com.obstacleavoid.util.ViewportUtils;
import com.obstacleavoid.util.debug.DebugCameraController;

public class GameRenderer implements Disposable
{
    //here we have all the code that is used to render objects in the game

    //Fields:
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;

    private OrthographicCamera hudCamera;
    private Viewport hudViewport;

    private BitmapFont font;
    private final GlyphLayout layout = new GlyphLayout();
    private DebugCameraController debugCameraController;
    private final GameController controller;
    private final AssetManager assetManager;
    private final SpriteBatch batch;

    private TextureRegion playerRegion;
    private TextureRegion obstacleRegion;
    private TextureRegion backgroundRegion;

    //Constructor


    public GameRenderer(SpriteBatch batch, AssetManager assetManager, GameController controller)
    {
        this.assetManager = assetManager;
        this.controller = controller;
        this.batch = batch;
        init();
    }

    private void init()
    {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera);
        renderer= new ShapeRenderer();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, hudCamera);
        font = assetManager.get(AssetDescriptors.FONT);

        //create debugCameraController:
        debugCameraController = new DebugCameraController();
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y);

        TextureAtlas gamePlayAtlas = assetManager. get(AssetDescriptors.GAME_PLAY);

        playerRegion = gamePlayAtlas.findRegion(RegionNames.PLAYER);
        obstacleRegion = gamePlayAtlas.findRegion(RegionNames.OBSTACLE);
        backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
    }

    //methods

    public void render(float delta)
    {
        //not inside if because sometimes we want to be able to control the camera even if not alive
        debugCameraController.handleDebugInput(delta);
        debugCameraController.applyTo(camera);

        //we will handle touch input here, not in game controller, bc we need aspects of game renderer for this
        if(Gdx.input.isTouched() && !controller.isGameOver())
        {
            Vector2 screenTouch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            System.out.println("screen touch: " + screenTouch);
            Vector2 worldTouch = viewport.unproject(new Vector2(screenTouch)); // this converts from screen coordinates to world coordinates
            System.out.println("world touch: " + worldTouch);

            Player player = controller.getPlayer();
            worldTouch.x = MathUtils.clamp(worldTouch.x, 0, GameConfig.WORLD_WIDTH - player.getWidth());
            player.setX(worldTouch.x);

        }

        //clears screeen
        GdxUtils.clearScreen();

        //renderUi and renderDebug are like layers of the game, we need to structure so that everything shows up as we want it to;

        renderGamePlay(); //this is going to be rendered on top
        //render ui/debug
        renderUi();// then this

        //render Debug graphics
        renderDebug(); //then this

    }

    @Override
    public void dispose()
    {
        renderer.dispose();

    }


    public void resize(int width, int height)
    {
        viewport.update(width,height,true);
        hudViewport.update(width,height, true);
        ViewportUtils.debugPixelPerUnit(viewport);

    }

    private void renderGamePlay()
    {
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //draw background (has to be draw first to be rendered to the back)
        Background background = controller.getBackground();
        batch.draw(backgroundRegion, background.getX(), background.getY(), background.getWidth(), background.getHeight());



        //draw player
        Player player = controller.getPlayer();

        batch.draw(playerRegion, player.getX(), player.getY(), player.getWidth(), player.getHeight());

        //draw obstacles
        for(Obstacle obstacle: controller.getObstacles())
        {
            batch.draw(obstacleRegion, obstacle.getX(), obstacle.getY(), obstacle.getWidth(), obstacle.getHeight());
        }


        batch.end();
    }

    private void renderUi()
    {
        hudViewport.apply();
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        String livesText = "LIVES: " + controller.getLives();
        layout.setText(font, livesText);

        font.draw(batch, livesText, 20, GameConfig.HUD_HEIGHT - layout.height);

        String scoreText = "SCORE: "  + controller.getDisplayScore();
        layout.setText(font, scoreText);
        font.draw(batch, scoreText, GameConfig.HUD_WIDTH - layout.width - 20, GameConfig.HUD_HEIGHT - layout.height);

        batch.end();
    }

    private void renderDebug ()
    {
        viewport.apply(); //this avoids problems with multiple viewports, ensures the required viewport is applied
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        drawDebug();
        renderer.end();

    }

    private void drawDebug ()
    {
        Player player = controller.getPlayer();
        player.drawDebug(renderer);

        Array<Obstacle> obstacles = controller.getObstacles();

        for(Obstacle obstacle: obstacles)
        {
            obstacle.drawDebug(renderer);
        }

    }
}
