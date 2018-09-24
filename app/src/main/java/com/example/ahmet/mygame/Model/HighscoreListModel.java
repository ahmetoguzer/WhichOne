package com.example.ahmet.mygame.Model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.ahmet.mygame.DB.DatabaseHelper;
import com.example.ahmet.mygame.Vo.Highscores;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmet on 17.5.2017.
 */

public class HighscoreListModel {

    public List<Highscores> getAllScores(DatabaseHelper myDb) {
        List<Highscores> scores = new ArrayList<Highscores>();

//        Cursor res = myDb.getAllData();
        Cursor res=myDb.descOrder();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
//            buffer.append("Id :"+ res.getString(0));
//            buffer.append("Name :"+ res.getString(1));
//            buffer.append("Score :"+ res.getString(2));
//            buffer.append("Level :"+ res.getString(3));

            Highscores highscores = new Highscores();
            highscores.setName(res.getString(1));
            highscores.setScore(res.getString(2));
            highscores.setLevel(res.getString(3));
            scores.add(highscores);
        }

        return scores;
    }
}
