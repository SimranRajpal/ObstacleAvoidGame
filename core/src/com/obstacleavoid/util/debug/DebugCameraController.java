package com.obstacleavoid.util.debug;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Logger;


public class DebugCameraController
{
    private static final Logger log = new Logger(DebugCameraController.class.getName(), Logger.DEBUG);
    //***Constants***




    //***Attributes***
    private Vector2 position = new Vector2();
    private Vector2 startPosition = new Vector2();
    private float zoom = 1.0f; //by default, the default zoom is 1.0f;
    private DebugCameraConfig config;

    //***Constructor***


    public DebugCameraController()
    {
        config = new DebugCameraConfig();
        log.info(" cameraCongif: " + config);
    }

    //public API(methods
    public void setStartPosition(float x, float y)
    {
        startPosition.set(x,y); //start position of our camera
        position.set(x,y); //current position
    }

    public void applyTo(OrthographicCamera camera) //this will apply the position in our controller to our camera
    {
        camera.position.set(position, 0); //0 for the z-axis
        camera.zoom = zoom;
        camera.update();


    }

    public void handleDebugInput(float delta)
    {
        //if you see a parameter being passed as delta, it usually means that the method is being called every frame;

        if(Gdx.app.getType() != Application.ApplicationType.Desktop)
        {
            return; //if it's not running on desktop, just exit
        }

        float moveSpeed = config.getMoveSpeed() * delta;
        float zoomSpeed = config.getZoomSpeed() * delta;


        //move controls,done with input polling
        if(config.isLeftPressed())
        {
            moveLeft(moveSpeed);
        }
        if(config.isRightPressed())
        {
            moveRight(moveSpeed);
        }
        if(config.isUpPressed())
        {
            moveUp(moveSpeed);
        }
        if(config.isDownPressed())
        {
            moveDown(moveSpeed);
        }
        //zoom controls
        if(config.isZoomInPressed())
        {
            zoomIn(zoomSpeed);
        }
        if(config.isZoomOutPressed())
        {
            zoomOut(zoomSpeed);
        }
        if(config.isResetPressed())
        {
            reset();
        }
        if(config.isLogPressed())
        {
            logDebug();
        }



    }

    //private methods
    private void setPosition (float x, float y)
    {
        position.set(x,y);
    }
    private void moveCamera(float xSpeed, float ySpeed)
    {
        setPosition(position.x + xSpeed, position.y + ySpeed);

    }

    private void moveLeft(float speed)
    {
        moveCamera(-speed, 0);

    }
    private void moveRight(float speed)
    {
        moveCamera(speed, 0);

    }
    private void moveUp(float speed)
    {
        moveCamera(0, speed);

    }
    private void moveDown(float speed)
    {
        moveCamera(0, -speed);

    }
    //zoom methods
    private void setZoom(float value)
    {
        zoom= MathUtils.clamp(value, config.getMaxZoomIn(), config.getMaxZoomOut()); //this making sure that value isn't outside the min-max range
        //if value below min, zoom set to min, if value above max, zoom set to max
    }

    private void zoomIn(float zoomSpeed)
    {
        setZoom(zoom + zoomSpeed);

    }
    private void zoomOut(float zoomSpeed)
    {
        setZoom(zoom - zoomSpeed);

    }
    private void reset()
    {
        position.set(startPosition);
        setZoom(1.0f); //default


    }
    private void logDebug()
    {
        log.debug("Position: " + position + " zoom: " + zoom);

    }

}