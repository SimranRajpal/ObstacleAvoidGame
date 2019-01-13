package com.obstacleavoid.config;

public enum DifficultyLevel
{
    //enums basically contain a list of constants that are automatically public static final
    //can also have methods, constructors, variables
    //cannot call constructor directly

    //here we are calling our enum constructor
    EASY(GameConfig.EASY_OBSTACLE_SPEED),
    MEDIUM(GameConfig.MEDIUM_OBSTACLE_SPEED),
    HARD(GameConfig.HARD_OBSTACLE_SPEED);

    private final float obstacleSpeed;

    DifficultyLevel(float obstacleSpeed) //not public
    {
        this.obstacleSpeed = obstacleSpeed;

    }

    public float getObstacleSpeed() {
        return obstacleSpeed;
    }
}
