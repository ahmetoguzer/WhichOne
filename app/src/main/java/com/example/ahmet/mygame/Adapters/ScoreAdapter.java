package com.example.ahmet.mygame.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmet.mygame.DB.DatabaseHelper;
import com.example.ahmet.mygame.MathGame;
import com.example.ahmet.mygame.ProfileActivity;
import com.example.ahmet.mygame.R;
import com.example.ahmet.mygame.Vo.Highscores;

import java.util.ArrayList;
import java.util.List;

import static com.example.ahmet.mygame.R.id.brain;

/**
 * Created by Ahmet on 12.5.2017.
 */

public class ScoreAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Highscores> highscoresList;
    private ProfileActivity profileActivity;


    public ScoreAdapter(Context context, List<Highscores> highscoresList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.highscoresList = highscoresList;
        profileActivity =new ProfileActivity();
    }

    @Override
    public int getCount() {
        return highscoresList.size();
    }

    @Override
    public Object getItem(int position) {
        return highscoresList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.item_score, null);

        TextView txtScore = (TextView) vi.findViewById(R.id.txt_profile_score);
        TextView txtLevel = (TextView) vi.findViewById(R.id.txt_profile_seviye);


        Highscores country = highscoresList.get(position);

        txtScore.setText("Score : "+country.getScore());
        txtLevel.setText("Level : "+country.getLevel());

        Typeface tf = Typeface.createFromAsset(context.getAssets(),"GoodDog.otf");
        txtLevel.setTypeface(tf);
        txtScore.setTypeface(tf);

        levelDegress(country.getLevel(),vi);
        return vi;


    }

    public void levelDegress(final String level,View convertView){


        final ImageView profileBrain=(ImageView) convertView.findViewById(R.id.img_profile_brain);
        try {

            profileActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if(Integer.valueOf(level)<5){
                        profileBrain.setImageResource(R.drawable.brain1);
                    }

                    if(Integer.valueOf(level)>=5 & Integer.valueOf(level)<8 ){
                        profileBrain.setImageResource(R.drawable.brain2);
                    }
                    if(Integer.valueOf(level)>=8 & Integer.valueOf(level)<11){
                        profileBrain.setImageResource(R.drawable.brain3);
                    }
                    if(Integer.valueOf(level)>=11 & Integer.valueOf(level)<14){
                        profileBrain.setImageResource(R.drawable.brain4);
                    }
                    if(Integer.valueOf(level)>=14){
                        profileBrain.setImageResource(R.drawable.brain5);
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
