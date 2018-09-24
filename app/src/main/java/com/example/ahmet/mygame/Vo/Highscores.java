package com.example.ahmet.mygame.Vo;

import java.io.Serializable;

/**
 * Created by Ahmet on 17.5.2017.
 */

public class Highscores implements Serializable {
    private String name;
    private String score;
    private String level;

    public Highscores() {

    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
