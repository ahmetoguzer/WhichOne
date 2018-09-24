package com.example.ahmet.mygame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Ahmet on 4.5.2017.
 */

public class SplashActivity extends Activity
{
    private static long SPLASH_MILLIS = 3600;
    private TextView txtsplash;
    private ImageView imgsplash;
    private ImageView imgplus;
    private ImageView imgeksi;
    private ImageView imgcarpi;
    private ImageView imgesittir;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.activity_splash, null, false);

        addContentView(layout, new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT));

        txtsplash = (TextView) findViewById(R.id.txt_splash);
        imgsplash = (ImageView) findViewById(R.id.img_splash);
        imgplus = (ImageView) findViewById(R.id.img_plus);
        imgeksi = (ImageView) findViewById(R.id.img_eksi);
        imgcarpi = (ImageView) findViewById(R.id.img_carpi);
        imgesittir = (ImageView) findViewById(R.id.img_esittir);


        final Animation leftupAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.left_up_move);
        final Animation rightupAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.right_up_move);
        final Animation rightbottomAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.right_bottom_move);
        final Animation leftbottomAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.left_bottom_move);
        imgplus.setAnimation(leftupAnim);
        imgeksi.setAnimation(rightupAnim);
        imgesittir.setAnimation(rightbottomAnim);
        imgcarpi.setAnimation(leftbottomAnim);


        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                imgcarpi.setVisibility(View.GONE);
                imgplus.setVisibility(View.GONE);
                imgesittir.setVisibility(View.GONE);
                imgeksi.setVisibility(View.GONE);
                imgsplash.setVisibility(View.VISIBLE);
                txtsplash.setVisibility(View.VISIBLE);
                final Animation myAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.bottom_up);
                final Animation mysplashAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.grow_up);
                imgsplash.setAnimation(mysplashAnim);
                txtsplash.startAnimation(myAnim);


            }
        }.start();


//        imgsplash.startAnimation(myAnim);

        Typeface tf = Typeface.createFromAsset(SplashActivity.this.getAssets(),"GoodDog.otf");
        txtsplash.setTypeface(tf);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {

            @Override
            public void run()
            {

                int position=0;
                Intent intent = new Intent(SplashActivity.this,
                        IntroActivity.class);
                startActivity(intent);

            }

        }, SPLASH_MILLIS);
    }

}

