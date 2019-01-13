package com.obstacleavoid.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ViewportUtils {

    private static final Logger log = new Logger(ViewportUtils.class.getName(), Logger.DEBUG);

    private static final int DEFAULT_CELL_SIZE = 1;

    public static void drawGrid (Viewport viewport, ShapeRenderer renderer)
    {
        drawGrid(viewport, renderer, DEFAULT_CELL_SIZE);

    }

    public static void drawGrid (Viewport viewport, ShapeRenderer renderer, int cellSize)//by default 1;
    {
        //validate parameters/arguments
        if(viewport == null)
        {
            throw new IllegalArgumentException("viewport is null");
        }
        if(renderer == null)
        {
            throw new IllegalArgumentException("renderer is null");
        }

        if (cellSize < DEFAULT_CELL_SIZE)
        {
            cellSize = DEFAULT_CELL_SIZE;
        }

        //copy oldColor from renderer
        Color oldColor = new Color(renderer.getColor());

        int worldWidth = (int)viewport.getWorldWidth(); // got these values when we instantiated viewport.
        int worldHeight = (int)viewport.getWorldHeight();
        int  doubleWorldWidth = worldWidth *2;
        int doubleWorldHeight = worldHeight * 2;

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);

        //draw vertical lines
        for (int x = - doubleWorldHeight; x< doubleWorldHeight; x+= cellSize)
        {
            renderer.line(x, -doubleWorldHeight, x, doubleWorldHeight); //(x, -worldHeight) to (x, worldHeight) -> coordinates

        }

        //draw horizontal lines
        for (int y= - doubleWorldHeight; y < doubleWorldHeight; y+= cellSize)
        {
            renderer.line(-doubleWorldWidth,y, doubleWorldWidth, y);
        }

        //draw x,y-axis lines:
        renderer.setColor(Color.RED);
        renderer.line(0, -doubleWorldHeight, 0, doubleWorldHeight);
        renderer.line(-doubleWorldWidth, 0, doubleWorldWidth, 0);

        //draw world bounds
        renderer.setColor(Color.GREEN);
        renderer.line(0, worldHeight, worldWidth, worldHeight);
        renderer.line(worldWidth, 0 , worldWidth, worldHeight);

        renderer.setColor(oldColor);

        renderer.end();



    }

    public static void debugPixelPerUnit(Viewport viewport)
    {
        if(viewport == null)
        {
            throw new IllegalArgumentException("viewport is null");
        }
        float screenWidth = viewport.getScreenWidth(); //got these when updated viewport
        float screenHeight = viewport.getScreenHeight();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        //PPU: pixels per unit

        float xPPU = screenWidth/worldWidth;
        float yPPU = screenHeight/worldHeight;

        log.debug("x PPU: " + xPPU + " y PPU: " + yPPU);

    }

    private ViewportUtils(){}; //only has static methods

}
