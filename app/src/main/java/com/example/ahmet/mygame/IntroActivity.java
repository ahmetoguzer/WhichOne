package com.example.ahmet.mygame;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ahmet on 11.5.2017.
 */

public class IntroActivity extends Activity {

    private TextView leftbox;
    private TextView rightbox;
    private TextView txtleft;
    private TextView txtright;
    private TextView txtor;
    private TextView txtquestion;
    private ImageView rightarrow;
    private ImageView  leftarrow;
    private Button skip;
    private CountDownTimer introTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        rightbox= (TextView) findViewById(R.id.txt_rightbox);
        leftbox = (TextView) findViewById(R.id.txt_leftbox);
        txtleft = (TextView) findViewById(R.id.txt_left);
        txtright = (TextView) findViewById(R.id.txt_right);
        txtor = (TextView) findViewById(R.id.txt_or);
        txtquestion = (TextView) findViewById(R.id.txt_question);
        rightarrow = (ImageView) findViewById(R.id.img_intro_right_arrow);
        leftarrow = (ImageView) findViewById(R.id.img_intro_left_arrow);
        skip = (Button) findViewById(R.id.btn_skip);


        Typeface tf = Typeface.createFromAsset(this.getAssets(),"GoodDog.otf");
        leftbox.setTypeface(tf);
        rightbox.setTypeface(tf);
        txtright.setTypeface(tf);
        txtleft.setTypeface(tf);
        txtor.setTypeface(tf);
        txtquestion.setTypeface(tf);
        skip.setTypeface(tf);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


        introTimer =new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                final Animation myAnim = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.prescreen);
                leftbox.setVisibility(View.VISIBLE);
                rightbox.setVisibility(View.VISIBLE);
                leftbox.startAnimation(myAnim);
                rightbox.startAnimation(myAnim);



                new CountDownTimer(1500, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        final Animation myAnim = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.prescreen);
                        txtquestion.setVisibility(View.VISIBLE);
                        txtquestion.startAnimation(myAnim);
                        new CountDownTimer(1500, 1000) {

                            public void onTick(long millisUntilFinished) {

                            }

                            public void onFinish() {
                                final Animation myAnim = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.prescreen);
                                final Animation anim = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.left);
                                final Animation mAnim = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.right);
                                txtleft.setVisibility(View.VISIBLE);
                                rightarrow.setVisibility(View.VISIBLE);
                                rightarrow.setAnimation(mAnim);
                                leftarrow.setVisibility(View.VISIBLE);
                                leftarrow.setAnimation(anim);
                                txtleft.startAnimation(anim);
                                txtor.setVisibility(View.VISIBLE);
                                txtor.startAnimation(myAnim);
                                txtright.setVisibility(View.VISIBLE);
                                txtright.startAnimation(mAnim);
                                new CountDownTimer(1500, 1000) {

                                    public void onTick(long millisUntilFinished) {

                                    }

                                    public void onFinish() {
                                        final Animation myAnim = AnimationUtils.loadAnimation(IntroActivity.this, R.anim.prescreen);
                                        skip.setVisibility(View.VISIBLE);
                                        skip.setAnimation(myAnim);

                                    }

                                }.start();
                            }

                        }.start();
                    }

                }.start();
            }

        }.start();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        leftbox.setVisibility(View.VISIBLE);
        rightbox.setVisibility(View.VISIBLE);
        txtquestion.setVisibility(View.VISIBLE);
        txtleft.setVisibility(View.VISIBLE);
        rightarrow.setVisibility(View.VISIBLE);
        leftarrow.setVisibility(View.VISIBLE);
        txtor.setVisibility(View.VISIBLE);
        txtright.setVisibility(View.VISIBLE);
        skip.setVisibility(View.VISIBLE);


        if(introTimer != null) {
            introTimer.cancel();
            introTimer = null;
        }


        leftbox.clearAnimation();
        rightbox.clearAnimation();
        txtquestion.clearAnimation();
        txtleft.clearAnimation();
        rightarrow.clearAnimation();
        leftarrow.clearAnimation();
        txtor.clearAnimation();
        txtright.clearAnimation();
        skip.clearAnimation();

        return super.onTouchEvent(event);
    }
}
