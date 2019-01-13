package com.obstacleavoid.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.assets.RegionNames;
import com.obstacleavoid.common.GameManager;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.util.GdxUtils;

public class HighscoreScreen extends ScreenAdapter
{
    private static final Logger log = new Logger(HighscoreScreen.class.getName(), Logger.DEBUG);
    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    public HighscoreScreen(ObstacleAvoidGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void render(float delta) {
        GdxUtils.clearScreen();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width,height,true);
    }

    @Override
    public void show() {

        viewport = new FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);
        createUi();
    }

    private void createUi()
    {
        Table table = new Table();

        TextureAtlas gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
        TextureAtlas uiAtlas = assetManager.get(AssetDescriptors.UI);
        BitmapFont font = assetManager.get(AssetDescriptors.FONT);

        TextureRegion backgroundRegion = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);
        TextureRegion panelRegion = uiAtlas.findRegion(RegionNames.PANEL);

        TextureRegion backRegion = uiAtlas.findRegion(RegionNames.BACK);
        TextureRegion backPressedRegion = uiAtlas.findRegion(RegionNames.BACK_PRESSED);

        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        //Background
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        //highscore text
        Label highscoreText = new Label("HIGHSCORE: ", labelStyle);


        //highscore label
        String highScoreString = GameManager.INSTANCE.getHighScoreString();
        Label highscoreLabel = new Label(highScoreString, labelStyle);

        //back button
        ImageButton backButton = new ImageButton(new TextureRegionDrawable(backRegion), new TextureRegionDrawable(backPressedRegion));

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                back();
            }
        });


        //setup tables]
        Table contentTable = new Table();
        contentTable.defaults().pad(20);
        contentTable.setBackground(new TextureRegionDrawable(panelRegion));

        contentTable.add(highscoreText).row();
        contentTable.add(highscoreLabel).row();
        contentTable.add(backButton).row();

        contentTable.center();
        table.add(contentTable);
        table.center();
        table.setFillParent(true);
        table.pack();
        stage.addActor(table);



    }
    private void back()
    {
        log.debug("back()");
        game.setScreen(new MenuScreen(game));
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
