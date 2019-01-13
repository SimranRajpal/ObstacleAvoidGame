package com.obstacleavoid.entity;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public abstract class  GameObjectBase
{



    private float x;
    private float y; //by default 0;
    private float width = 1;
    private float height = 1;
    private Circle bounds; // represents the area of the body of player

    public GameObjectBase(float boundsRadius)
    {
        bounds = new Circle(x,y, boundsRadius);

    }

    public void drawDebug(ShapeRenderer renderer)
    {
        renderer.circle(bounds.x, bounds.y, bounds.radius,30);
    }

    public void setPosition (float x, float y)// sets position for player
    {
        this.x = x;
        this.y = y;
        updateBounds();

    }

    public void updateBounds()
    {
        float halfWidth = width /2;
        float halfHeight = height/2;
        bounds.setPosition(x + halfWidth,y + halfHeight); //now whenever we set position for our player, we will also update the bounds for our player
    }

    public float getX() {
        return x;
    }


    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
        updateBounds();
    }

    public void setY(float y) {
        this.y = y;
        updateBounds();
    }

    public Circle getBounds() {
        return bounds;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setSize(float width, float height)
    {
        this.width = width;
        this.height = height;
        updateBounds();
    }
}
