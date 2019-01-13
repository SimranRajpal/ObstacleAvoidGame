package com.obstacleavoid.screen.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.assets.AssetDescriptors;
import com.obstacleavoid.common.GameManager;
import com.obstacleavoid.config.DifficultyLevel;
import com.obstacleavoid.config.GameConfig;
import com.obstacleavoid.entity.Background;
import com.obstacleavoid.entity.Obstacle;
import com.obstacleavoid.entity.Player;

public class GameController
{
    //here we will copy-paste all the code that belongs to game logic

    //Constants
    private static final Logger log = new Logger(GameController.class.getName(), Logger.DEBUG);

    private Player player;
    private Array<Obstacle> obstacles = new Array<Obstacle>();
    private Background background;
    private float obstacleTimer;
    private float scoreTimer;
    private int lives= GameConfig.LIVES_START;
    private int score; //by default 0;
    private int displayScore;
    private Pool<Obstacle> obstaclePool;
    //calculate positions
    private final  float startPlayerX = GameConfig.WORLD_WIDTH/2f - GameConfig.PLAYER_SIZE/2;;
    private final float startPlayerY = 1 - GameConfig.PLAYER_SIZE/2;
    private Sound hit;
    private Music main;
    private final ObstacleAvoidGame game;
    private final AssetManager assetManager;


    //Constructor

    public GameController(ObstacleAvoidGame game)
    {
        this.game = game;
        assetManager = game.getAssetManager();
        init();
    }

    //we're gonna put all the logic from show() in here
    private void init()
    {
        //initailiaing player
        player= new Player();



        //position player
        player.setPosition(startPlayerX, startPlayerY);

        //create obstacle pool
        obstaclePool = Pools.get(Obstacle.class, 40);// Pool will have max 40 ojects of obstacle class

        //create background
        background = new Background();
        background.setPosition(0,0);
        background.setSize(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT);

        hit = assetManager.get(AssetDescriptors.HIT_SOUND);
        main = assetManager.get(AssetDescriptors.MAIN_MUSIC);






    }

    //methods
    public void update(float delta)
    {
        main.play();

        if(isGameOver())
        {

            return;
        }

        updatePlayer();
        updateObstacles(delta);
        updateScore(delta); //delta is change in time
        updateDisplayScore(delta);

        if(isPlayerCollidingWithObstacle())
        {
            log.debug("Collision Detected");
            lives--;

            if(isGameOver())
            {
                log.debug("GameOver!!!");
                GameManager.INSTANCE.updateHighscore(score);


            }
            else
            {
                restart();
            }
        }
    }

    private void restart()
    {
        obstaclePool.freeAll(obstacles);
        obstacles.clear();
        player.setPosition(startPlayerX, startPlayerY);
    }

    public boolean isGameOver()
    {

        return lives <= 0;


    }

    private boolean isPlayerCollidingWithObstacle ()
    {
        //we will loop through the whole array to check if obstacle is colliding
        for(Obstacle obstacle: obstacles)
        {
            if(obstacle.isNotHit() && obstacle.isPlayerColliding(player)) //basically isNotHit() is giving each obstacle a property of it already
            //hitting the player. so if isNotHit() true, then it will continue to collide
            {
                hit.play();
                return true;

            }
        }

        return false;
    }

    private void updatePlayer()
    {
        float xSpeed = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
        {
            xSpeed = GameConfig.MAX_PLAYER_X_SPEED;

        }
        else if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            xSpeed = -GameConfig.MAX_PLAYER_X_SPEED;

        }
        player.setX(player.getX() + xSpeed); //so if player didn't press any key, our horizontal speed will remain 0;

        blockPlayerFromLeavingTheWorld();

    }
    private void blockPlayerFromLeavingTheWorld()
    {
        float playerX = MathUtils.clamp(player.getX(), 0, GameConfig.WORLD_WIDTH- player.getWidth());

        player.setPosition(playerX, player.getY());




    }

    private void updateObstacles(float delta)
    {
        //we are going to loop through the obstacle array
        for(Obstacle obstacle: obstacles)
        {
            obstacle.update();
        }

        createNewObstacle(delta);
        removePassedObstacles();

    }

    private void createNewObstacle(float delta)
    {
        obstacleTimer = obstacleTimer + delta;

        if(obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) // now time to release another obstacle
        {
            float min = 0;
            float max =GameConfig.WORLD_WIDTH - GameConfig.OBSTACLE_SIZE;
            float obstacleX = MathUtils.random(min, max); //randomly selecting an X position for our obstacle
            float obstacleY = GameConfig.WORLD_HEIGHT; //top of our game

            Obstacle obstacle = obstaclePool.obtain(); //getting a new obstacle from pool
            DifficultyLevel difficultyLevel = GameManager.INSTANCE.getDifficultyLevel();
            obstacle.setYspeed(difficultyLevel.getObstacleSpeed());
            obstacle.setPosition(obstacleX, obstacleY);

            obstacles.add(obstacle); //adding to array
            obstacleTimer = 0f; //resetting the time

        }
    }
    private void removePassedObstacles()
    {
        if(obstacles.size > 0)
        {
            Obstacle first = obstacles.first();

            float minObstacleY= -GameConfig.OBSTACLE_SIZE; //min height an obstacle can have, which is its body size below the x-axis
                                                //(so it doesn't disappear when center touches y=0;

            if(first.getY() < minObstacleY)
            {
                obstacles.removeValue(first, true);
                obstaclePool.free(first); //just putting it back to pool
            }
        }
    }
    private void updateScore(float delta)
    {
        scoreTimer = scoreTimer + delta;
        if(scoreTimer >= GameConfig.SCORE_MAX_TIME) //if score timer + delta is greater than max_score time, then add points
        {
            score += MathUtils.random(1,4); //1 inclusive, 5 exclusive
            scoreTimer = 0.0f; //resetting score timer

        }
    }

    private void updateDisplayScore(float delta)
    {
        if(displayScore < score)
        {
            displayScore = Math.min(score, displayScore + (int) (60 * delta)); //delta is a very small number (60 frames /second)
            //this way score display score goes up by one every frame
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Array<Obstacle> getObstacles() {
        return obstacles;
    }

    public int getLives() {
        return lives;
    }

    public int getDisplayScore() {
        return displayScore;
    }

    public Background getBackground()
    {
        return background;
    }
}
