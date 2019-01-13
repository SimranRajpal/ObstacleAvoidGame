package com.obstacleavoid.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

public class GdxUtils
{
    private GdxUtils() //private constructor means you can't instantiate it, all methods will be static
    {
    }

    public static void clearScreen()
    {
        clearScreen(Color.BLACK); //full clear

    }

    public static void clearScreen(Color color) // clears specified color
    {
        Gdx.gl.glClearColor(color.r, color.g, color.b, color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
}
