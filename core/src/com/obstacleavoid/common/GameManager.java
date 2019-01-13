package com.obstacleavoid.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.obstacleavoid.ObstacleAvoidGame;
import com.obstacleavoid.config.DifficultyLevel;

public class GameManager
{
    private static final String HIGH_SCORE_KEY = "highscore"; //key inside our map/preferences
    private static final String DIFFICULTY_KEY = "difficulty";
    private GameManager(){
        PREFS = Gdx.app.getPreferences(ObstacleAvoidGame.class.getSimpleName());
        highscore = PREFS.getInteger(HIGH_SCORE_KEY, 0);
        String difficultyName = PREFS.getString(DIFFICULTY_KEY, DifficultyLevel.MEDIUM.name());
        difficultyLevel = DifficultyLevel.valueOf(difficultyName);
    };

    //preferences a simple way to store small data, works like a hashmap, have key-value pairs.
    //whenever we put something in our preferences, we need to call flush so values get flushed to file
    private Preferences PREFS;

    private int highscore;
    private DifficultyLevel difficultyLevel = DifficultyLevel.MEDIUM; //by default

    public static final GameManager INSTANCE = new GameManager();
    //singleton class, can only have one instance, and this is it.

    public String getHighScoreString()
    {
        return String.valueOf(highscore); //convert integer to string
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void updateDifficulty(DifficultyLevel newDifficultyLevel)
    {
        if(newDifficultyLevel == difficultyLevel)
        {
            return;
        }
        difficultyLevel = newDifficultyLevel;
        PREFS.putString(DIFFICULTY_KEY, difficultyLevel.name());
        PREFS.flush();
    }

    public void updateHighscore(int score)
    {
        if(score < highscore)
        {
            return;
        }
        highscore = score;
        PREFS.putInteger(HIGH_SCORE_KEY, highscore); //updating highscore in map
        PREFS.flush();
    }


}
