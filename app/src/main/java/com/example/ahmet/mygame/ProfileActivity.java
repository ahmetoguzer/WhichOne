package com.example.ahmet.mygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ahmet.mygame.Adapters.ScoreAdapter;
import com.example.ahmet.mygame.DB.DatabaseHelper;
import com.example.ahmet.mygame.Model.HighscoreListModel;
import com.example.ahmet.mygame.Preferences.MySharedPreferences;
import com.example.ahmet.mygame.Vo.Highscores;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.sql.Statement;
import java.util.List;

/**
 * Created by Ahmet on 10.5.2017.
 */

public class ProfileActivity extends Activity {

    private ImageView imgProfile;
    private TextView  profileName;
    private ImageButton logout;
    private MySharedPreferences mySharedPreferences;
    private ScoreAdapter scoreAdapter;
    private ListView listscore;
    private MathGame mathGame;
    private ImageButton btnShare;
    private ImageButton btnDelete;
    private ImageView imgmaxlevel;
    private TextView txtbrainlevel;
    private ImageButton btnbackplay;

    private TextView title;
    private TextView highscoreAndlevel;

    private DatabaseHelper myDb;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);

        init();
        mathGame = new MathGame();

        myDb =new DatabaseHelper(ProfileActivity.this);
        HighscoreListModel highscoreListModel = new HighscoreListModel();
        List<Highscores> countries=highscoreListModel.getAllScores(myDb);
        scoreAdapter = new ScoreAdapter(ProfileActivity.this,countries);
        listscore.setAdapter(scoreAdapter);

        profileName.setText(AppController.getInstance().profilName);
        Picasso.with(getApplicationContext()).load(AppController.getInstance().profilImageUrl).into(imgProfile);

         mySharedPreferences = MySharedPreferences.getInstance(ProfileActivity.this);
        String data=mySharedPreferences.getData("user_name");
        if(!data.equals("")){
            profileName.setText( mySharedPreferences.getData("user_name"));
        }else {
            profileName.setText(AppController.getInstance().profilName);
        }

        //Banner
        MobileAds.initialize(this, "ca-app-pub-1227913993742958~6662956029");

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        AdView mAdView = (AdView) findViewById(R.id.adsView);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("A2AF805313EF56F88565C1EC2B035C56")
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mySharedPreferences.saveData("user_name","");
                LoginManager.getInstance().logOut();

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


        btnbackplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MathGame.class);
                startActivity(intent);
            }
        });


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getAllData();
                if(res.getCount() == 0) {
                    // show message
                    showMessage("Error","Nothing found");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append("Id :"+ res.getString(0)+"\n");
                    buffer.append("Name :"+ res.getString(1)+"\n");
                    buffer.append("Score :"+ res.getString(2)+"\n");
                    buffer.append("Level :"+ res.getString(3)+"\n\n");
                }

                // Show all data
                showMessage("Data",buffer.toString());
            }
        });

        String level = myDb.getmaxLevel();int a=level.length();
        brainDegrees(level);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deleteAllDatabase();
            }
        });

    }

    private void init(){
        imgProfile = (ImageView) findViewById(R.id.img_profile);
        profileName = (TextView) findViewById(R.id.txt_profile_name);
        logout = (ImageButton) findViewById(R.id.btn_logout);
        listscore = (ListView) findViewById(R.id.list_score);
        btnShare = (ImageButton) findViewById(R.id.btn_share);
        btnDelete = (ImageButton) findViewById(R.id.btn_delete);
        imgmaxlevel = (ImageView) findViewById(R.id.img_max_level);
        txtbrainlevel = (TextView) findViewById(R.id.txt_scale);
        btnbackplay = (ImageButton) findViewById(R.id.btn_back_play);
        title = (TextView) findViewById(R.id.txt_profile_title);
        highscoreAndlevel = (TextView) findViewById(R.id.txt_Highscore_and_level);

        Typeface tf = Typeface.createFromAsset(this.getAssets(),"GoodDog.otf");
        profileName.setTypeface(tf);
        txtbrainlevel.setTypeface(tf);
        title.setTypeface(tf);
        highscoreAndlevel.setTypeface(tf);
    }

    public void showMessage(String title,String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public void brainDegrees(final String level){

        try {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if(Integer.valueOf(level)<5){
                        imgmaxlevel.setImageResource(R.drawable.brain1);
                        txtbrainlevel.setText("Bright Normal");
                    }

                    if(Integer.valueOf(level)>=5 & Integer.valueOf(level)<8 ){
                        imgmaxlevel.setImageResource(R.drawable.brain2);
                        txtbrainlevel.setText("Moderately Gifted");
                    }
                    if(Integer.valueOf(level)>=8 & Integer.valueOf(level)<11){
                        imgmaxlevel.setImageResource(R.drawable.brain3);
                        txtbrainlevel.setText("Highly Gifted");
                    }
                    if(Integer.valueOf(level)>=11 & Integer.valueOf(level)<14){
                        imgmaxlevel.setImageResource(R.drawable.brain4);
                        txtbrainlevel.setText("Exceptionally Gifted");
                    }
                    if(Integer.valueOf(level)>=14){
                        imgmaxlevel.setImageResource(R.drawable.brain5);
                        txtbrainlevel.setText("Profoundly Normal");
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

    }
}
