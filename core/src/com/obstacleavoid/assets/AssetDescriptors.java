package com.obstacleavoid.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class AssetDescriptors
{
    public static final AssetDescriptor<BitmapFont> FONT= new AssetDescriptor<BitmapFont>(AssetPaths.UI_FONT, BitmapFont.class);
    public static final AssetDescriptor<TextureAtlas> GAME_PLAY = new AssetDescriptor<TextureAtlas>(AssetPaths.GAME_PLAY, TextureAtlas.class);

    public static final AssetDescriptor<TextureAtlas> UI = new AssetDescriptor<TextureAtlas>(AssetPaths.UI, TextureAtlas.class);
    public static final AssetDescriptor<Sound> HIT_SOUND = new AssetDescriptor<Sound>(AssetPaths.HIT, Sound.class);

    public static final AssetDescriptor<Music> MENU_MUSIC = new AssetDescriptor<Music>(AssetPaths.MENU, Music.class);
    public static final AssetDescriptor<Music> MAIN_MUSIC = new AssetDescriptor<Music>(AssetPaths.MAIN, Music.class);



    private AssetDescriptors(){};


}
