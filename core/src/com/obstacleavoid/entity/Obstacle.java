package com.obstacleavoid.entity;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Pool;
import com.obstacleavoid.config.GameConfig;

public class Obstacle extends GameObjectBase implements Pool.Poolable
{


    private float ySpeed = GameConfig.MEDIUM_OBSTACLE_SPEED;
    private boolean hit;

     // represents the area of the body of obstacle

    public Obstacle()
    {
        super(GameConfig.OBSTACLE_BOUNDS_RADIUS);
        setSize(GameConfig.OBSTACLE_SIZE, GameConfig.OBSTACLE_SIZE);
    }


    public void update()
    {
        //obstacles will appear at the top of our world, and they are moving down, so subtracting speed
        setY(getY()-ySpeed);

    }




    public boolean isPlayerColliding(Player player)
    {
        Circle playerBounds = player.getBounds();
        //this will check if the player body and obstacle overlap(aka collide)
        //Intersector a class offered by libgdx
        boolean overlaps = Intersector.overlaps(playerBounds, getBounds());
        hit = overlaps; //when overlaps is true, hit is true, when overlap = false, hit = false;, so no if statement required.

        return overlaps;

    }

    public boolean isNotHit() {return !hit;}

    public void setYspeed(float obstacleSpeed)
    {
        this.ySpeed = obstacleSpeed;
    }

    @Override
    public void reset() {
        hit = false; //now if recycled obstacle hits the player, the player will lose life
    }


}
