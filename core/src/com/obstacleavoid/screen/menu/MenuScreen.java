package com.obstacleavoid.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.assets.RegionNames;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.entity.Obstacle;
import com.obstacleavoid.screen.game.GameScreen;
import com.obstacleavoid.util.GdxUtils;

public class MenuScreen extends ScreenAdapter
{
    private static final Logger log = new Logger(MenuScreen.class.getName(), Logger.DEBUG);

    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    public MenuScreen(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void render(float delta)
    {
        GdxUtils.clearScreen();
        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height,true);

    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT); //no need for camera, stage does the work
        stage = new Stage(viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);
        initUi();
    }

    private void initUi()
    {
        Table table = new Table();

        TextureAtlas gamePlayAtlas= assetManager.get(AssetDescriptors.GAME_PLAY); //we need this for the background
        TextureAtlas uiAtlas= assetManager.get(AssetDescriptors.UI);

        TextureRegion backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        TextureRegion panelRegion = uiAtlas.findRegion(RegionNames.PANEL);

        table.setBackground(new TextureRegionDrawable( backgroundRegion));

        //play button
        ImageButton playButton = createButton(uiAtlas, RegionNames.PLAY, RegionNames.PLAY_PRESSED);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                play();

            }
        });

        //setup table
        Table buttonTable = new Table();
        buttonTable.defaults().pad(20);
        buttonTable.setBackground(new TextureRegionDrawable(panelRegion));



        //highscore button
        ImageButton highscoreButton = createButton(uiAtlas,RegionNames.HIGH_SCORE, RegionNames.HIGH_SCORE_PRESSED);
        highscoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHighscore();
            }
        });


        //options button
        ImageButton optionsButton = createButton(uiAtlas, RegionNames.OPTIONS, RegionNames.OPTIONS_PRESSED);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showOptions();
            }
        });

        //Quit button


        buttonTable.add(playButton).row();
        buttonTable.add(highscoreButton).row();
        buttonTable.add(optionsButton).row();
        buttonTable.center();


        table.add(buttonTable);

        table.center();
        table.setFillParent(true);
        table.pack();

        stage.addActor(table);


    }

    private void play()
    {
        log.debug("play()");
        game.setScreen(new GameScreen(game));
    }
    private void showHighscore()
    {
        log.debug("showHighscore()");
        game.setScreen(new HighscoreScreen(game));
    }
    private void showOptions()
    {
        log.debug("showOptions()");
        game.setScreen(new OptionsScreen(game));
    }


    private static ImageButton createButton(TextureAtlas atlas, String upRegionName, String downRegionName)
    {
        TextureRegion upRegion = atlas.findRegion(upRegionName);
        TextureRegion downRegion = atlas.findRegion(downRegionName);

        return new ImageButton(new TextureRegionDrawable(upRegion), new TextureRegionDrawable(downRegion));


    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }


}
