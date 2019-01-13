package com.obstacleavoid.screen.loading;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.screen.game.GameScreen;
import com.obstacleavoid.screen.menu.MenuScreen;
import com.obstacleavoid.util.GdxUtils;

public class LoadingScreen extends ScreenAdapter {
    //constants

    private static final float PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2f;
    private static final float PROGRESS_BAR_HEIGHT = 60;

    //attributes
    private OrthographicCamera camera;
    private Viewport viewport;
    private ShapeRenderer renderer;
    private float progress;
    private float waitTime = 0.75f;

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    private boolean changeScreen;
    private Music menu;


    public LoadingScreen(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    //methods

    @Override
    public void render(float delta) {

        update(delta);

        GdxUtils.clearScreen();

        viewport.apply();
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        draw();


        renderer.end();

        if(changeScreen)
        {
            game.setScreen(new MenuScreen(game));

        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height,true);
    }

    @Override
    public void show()
    {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        assetManager.load(AssetDescriptors.FONT);
        assetManager.load((AssetDescriptors.GAME_PLAY));
        assetManager.load(AssetDescriptors.UI);
        assetManager.load(AssetDescriptors.HIT_SOUND);
        assetManager.load(AssetDescriptors.MENU_MUSIC);
        assetManager.load(AssetDescriptors.MAIN_MUSIC);







    }

    @Override
    public void hide() {

        //screens dont dispose automatically
        dispose();

    }

    @Override
    public void dispose() {
        renderer.dispose();

    }

    private static void waitMillis(long millis)
    {
        try
        {
            Thread.sleep(millis);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void update(float delta)
    {
        progress = assetManager.getProgress(); //progress is between 0 and 1;

        //update returns true when all assets are loaded
        if(assetManager.update())
        {
            waitTime -= delta;

            if(waitTime <= 0)
            {
                changeScreen = true;
            }

        }
    }

    private void draw()
    {
        float progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH)/2f;
        float progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT)/2f;
        renderer.rect(progressBarX,progressBarY, progress * PROGRESS_BAR_WIDTH,PROGRESS_BAR_HEIGHT);

    }
}
