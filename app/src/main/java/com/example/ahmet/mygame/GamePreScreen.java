package com.example.ahmet.mygame;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ahmet.mygame.Preferences.MySharedPreferences;
import com.squareup.picasso.Picasso;

/**
 * Created by Ahmet on 4.5.2017.
 */

public class GamePreScreen extends AppCompatActivity {

    private ImageButton btnplay;
    private TextView play;
    private ImageView imgProfile;
    private TextView  profileName;

    private FrameLayout frm;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_pre_screen);
        btnplay = (ImageButton) findViewById(R.id.btn_play);
        play = (TextView) findViewById(R.id.txt_play);
        frm = (FrameLayout) findViewById(R.id.frm_pre_screen);



        imgProfile = (ImageView) findViewById(R.id.img_profile_pre_screen);
        profileName = (TextView) findViewById(R.id.txt_profile_name_pre_screen);

        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance(GamePreScreen.this);

        Picasso.with(getApplicationContext()).load(AppController.getInstance().profilImageUrl).into(imgProfile);
        profileName.setText(AppController.getInstance().profilName);

        String data=mySharedPreferences.getData("user_name");
        if(!data.equals("")){
            profileName.setText( mySharedPreferences.getData("user_name"));
        }else {
            profileName.setText(AppController.getInstance().profilName);
        }

        final Animation mAnim = AnimationUtils.loadAnimation(GamePreScreen.this, R.anim.shake);
        final Animation Anim = AnimationUtils.loadAnimation(GamePreScreen.this, R.anim.bottom_up);
        play.startAnimation(Anim);
        btnplay.startAnimation(mAnim);

        final Animation myAnim = AnimationUtils.loadAnimation(GamePreScreen.this, R.anim.bottom_down);
        profileName.setAnimation(myAnim);
        imgProfile.setAnimation(myAnim);

        btnplay.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {

                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    v.setAlpha(0.5f);
                }

                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                {
                    v.setAlpha(1);
                }

                return false;
            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ıntent = new Intent(GamePreScreen.this,MathGame.class);
                startActivity(ıntent);
            }
        });

        Typeface tf = Typeface.createFromAsset(this.getAssets(),"GoodDog.otf");
        play.setTypeface(tf);


    }

    private void playButtonAnim(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LinearLayout.LayoutParams iparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                final ImageView  iv = new ImageView(GamePreScreen.this);

                iv.setImageResource(R.drawable.playbtn);
                iparams.height = 100;
                iparams.width = 100 ;
                iparams.rightMargin = 2;
                iparams.gravity = Gravity.CENTER;
                iv.setLayoutParams(iparams);



                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
                windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
                int deviceWidth = displayMetrics.widthPixels/2;
                int deviceHeight = displayMetrics.heightPixels/2;

                int[] positions = new int[2];
                btnplay.getLocationOnScreen(positions);
                int xtvtime = positions[0]+btnplay.getWidth()/4;
                int ytvtime= positions[1]+btnplay.getHeight()/4;

//                int[] konum = new int[2];
//                mActivity.tvhata[i].getLocationOnScreen(konum);
//                xtv = konum[0];
//                ytv= konum[1];


                TranslateAnimation anim = new TranslateAnimation( deviceWidth,xtvtime, 0, ytvtime );
                anim.setDuration(1500);
                anim.setFillAfter( true );
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        iv.setImageResource(android.R.color.transparent);
                       btnplay.setVisibility(View.VISIBLE);


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                iv.startAnimation(anim);
                frm.addView(iv);


            }
        });

    }
}
