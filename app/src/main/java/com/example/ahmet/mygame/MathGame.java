package com.example.ahmet.mygame;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmet.mygame.DB.DatabaseHelper;
import com.example.ahmet.mygame.Preferences.MySharedPreferences;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.ScaleModifier;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import static com.example.ahmet.mygame.R.id.brain;
import static com.example.ahmet.mygame.R.id.start;

public class MathGame extends AppCompatActivity implements View.OnClickListener {

    private TextView leftnumber;
    private TextView rightnumber;
    public TextView time;
    private TextView soru;
    private TextView txtstart;
    private TextView txtbasla;
    private TextView textView3;
    private TextView textView2;
    public TextView textView1;
    private TextView title;
    private int resultl=0;
    private int resultr=0;

    private int sorusayisi=0;
    private int dogrusayisi=1;
    private int yanlissayisi=0;

    private MediaPlayer mPlayergerisayim;


    private ArrayList<String> arrRules;
    private  ArrayList<Integer> arrQuestionsl;
    private  ArrayList arrx;
    private ArrayList arrOperatorsl;

    private ArrayList arrOperatorsr;
    private  ArrayList arry;
    private  ArrayList<Integer> arrQuestionsr;
    public MyCountDownTimer mycounter;
    public int sure=0;

    private ImageView img;
    private ImageView imgerror;
    private ProgressBar progressBar;
    public int level =1 ;

    private int sorucount=0;
    private ArrayList<String> userArray = new ArrayList<>();
    private ImageView animImageView;


    public  int puan;
    private CountDownTimer t;
    private TextView score;
    private TextView seviye;
    private int bonus=10;
    private int firsttime=0;
    private long maxTimeInMilliseconds = 10*1000;
    private  long remainedSecs;
    private long kalansure;
    private AnimationDrawable frameAnimation;
    private int frameCount=0;
    private boolean isTimerEnd=false;

    private ImageButton btnProfile;
    private ImageButton btnmenu;
    private ImageButton playagain;
    private ImageButton btnsound;
    private Button leftbutton;
    private Button rightbutton;
    private ImageButton btnagain;
    private RatingBar ratingBar;
    private ImageView brain;
    private FrameLayout frametv;

    private InterstitialAd gecisReklam;
    private InterstitialAd mInterstitialAd;

    private int open=0;

    private DatabaseHelper myDb;
    private MySharedPreferences mySharedPreferences;

    private boolean startgame=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_math_game);

        init();
        customizeTypface();
        onTouch();
        clickAnswer();
        startAnimation();
        settingVibration(level);


        //Banner
        MobileAds.initialize(this, "ca-app-pub-1227913993742958~6662956029");

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        AdView mAdView = (AdView) findViewById(R.id.adView);

        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("A2AF805313EF56F88565C1EC2B035C56")
                .build();

        // Start loading the ad in the background.
        mAdView.loadAd(adRequest);


//        Picasso.with(getApplicationContext()).load(AppController.getInstance().profilImageUrl).into(btnProfile);
        mySharedPreferences = MySharedPreferences.getInstance(MathGame.this);
        myDb =new DatabaseHelper(MathGame.this);

        gecisReklam = new InterstitialAd(this);

        gecisReklam.setAdUnitId("ca-app-pub-1227913993742958/6015324428");//Reklam İd miz.Admob da oluşturduğumuz geçiş reklam id si

        gecisReklam.setAdListener(new AdListener() { //Geçiş reklama listener ekliyoruz

            @Override
            public void onAdLoaded() { //Geçiş reklam Yüklendiğinde çalışır
                Toast.makeText(getApplicationContext(), "Reklam Yüklendi.", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onAdFailedToLoad(int errorCode) { //Geçiş Reklam Yüklenemediğinde  Çalışır
                Toast.makeText(getApplicationContext(), "Reklam Yüklenirken Hata Oluştu.", Toast.LENGTH_LONG).show();
            }

            public void onAdClosed(){ //Geçiş Reklam Kapatıldığında çalışır
                Toast.makeText(getApplicationContext(), "Reklam Kapatıldı.", Toast.LENGTH_LONG).show();

                //Geçiş reklam kapatıldığı zamanda yeni reklam yükleme işlemimizi başlatabiliriz.
                //loadGecisReklam();
            }
        });


        AdRequest admobRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("A2AF805313EF56F88565C1EC2B035C56")
                .build();

        //Reklam Yükleniyor
        gecisReklam.loadAd(admobRequest);


        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startgame){
                    mycounter.Stop();
                }
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

//        rightbutton.setOnClickListener(this);
        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(open==0){
                    playagain.setVisibility(View.VISIBLE);
                    btnsound.setVisibility(View.VISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    final Animation Anim = AnimationUtils.loadAnimation(MathGame.this, R.anim.again_button);
                    playagain.setAnimation(Anim);
                    btnsound.setAnimation(Anim);
                    open=1;
                    return;
                }

                if(open==1){
                    final Animation Anim = AnimationUtils.loadAnimation(MathGame.this, R.anim.again_button_go_back);
                    playagain.setAnimation(Anim);
                    btnsound.setAnimation(Anim);
                    playagain.setVisibility(View.INVISIBLE);
                    btnsound.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.VISIBLE);
                    open=0;
                }

            }
        });

        playagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MathGame.this,
                        MathGame.class);

                startActivity(intent);

            }
        });


    }

    private void addDatabase(){
        boolean isInserted = myDb.insertData(mySharedPreferences.getData("user_name"),
                puan,
                level );
        if(isInserted == true)
            Toast.makeText(MathGame.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MathGame.this,"Data not Inserted",Toast.LENGTH_LONG).show();
    }
    private void init(){

        btnmenu = (ImageButton) findViewById(R.id.btn_menu);
        leftnumber = (TextView) findViewById(R.id.leftnumber);
        rightnumber = (TextView) findViewById(R.id.rightnumber);
        imgerror=(ImageView) findViewById(R.id.imgerror);
        img=(ImageView) findViewById(R.id.img);
        score = (TextView) findViewById(R.id.score);
        seviye = (TextView) findViewById(R.id.seviye);
        soru  = (TextView) findViewById(R.id.txt_soru);
        time = (TextView) findViewById(R.id.tv_time);
        leftbutton = ( Button) findViewById(R.id.leftbutton);
        rightbutton = ( Button) findViewById(R.id.rightbutton);
        btnagain= ( ImageButton) findViewById(R.id.btnagainstart);
        frametv = (FrameLayout) findViewById(R.id.frame_tv);
        txtstart = (TextView) findViewById(R.id.start);
        txtbasla = (TextView) findViewById(R.id.txt_basla);
         brain = (ImageView) findViewById(R.id.brain);
        btnProfile = (ImageButton) findViewById(R.id.btn_profile);
        playagain = (ImageButton) findViewById(R.id.btn_again_main);
        btnsound = (ImageButton) findViewById(R.id.btn_sound);
        ratingBar = (RatingBar) findViewById(R.id.library_normal_ratingbar);
        title = (TextView)findViewById(R.id.txt_game_title);
    }

    private void clickAnswer(){
        rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(resultl<resultr){
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if(sorucount==1){

                                    puan=puan+bonus;
                                    score.setText("Score : "+puan);
                                    imgerror.setVisibility(View.GONE);
                                    img.setVisibility(View.VISIBLE);
                                    new CountDownTimer(500, 1000) {

                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            try {

                                                runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        imgerror.setVisibility(View.GONE);
                                                        img.setVisibility(View.GONE);

                                                        if(sorucount==0){

                                                            viewDidLoad();
                                                            sorusayisi++;
                                    MediaPlayer mPlayer = MediaPlayer.create(MathGame.this, R.raw.yenisoru);
                                    mPlayer.start();
                                                            sorucount++;

//                                    gecensure.setText("Yeni Soru Gelme Süresi :"+toplamsure+"\n"+
//                                    "Cevapta Geçen Süre :"+toplamsuremethot);
                                                        }
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }.start();
                                    MediaPlayer mPlayer = MediaPlayer.create(MathGame.this, R.raw.dogru);
                                    mPlayer.start();
                                    ratingBar.setRating(ratingBar.getRating()+1);
                                    dogrusayisi++;

                                    //settingVibration(dogrusayisi);
                                    userArray.clear();
                                    sorucount=0; resultl=0; resultr=0;


                                    if(level==1){
                                        arrQuestionsl.clear();
                                        arrQuestionsr.clear();
                                    }else if(level ==2){
                                        arrx.clear();
                                        arrQuestionsr.clear();
                                    }else if(level>3){
                                        arrx.clear();
                                        arry.clear();
                                    }
                                    if(ratingBar.getRating() == 5){
                                        mPlayer.stop();
                                        mPlayer = MediaPlayer.create(MathGame.this, R.raw.yenirekor);
                                        mPlayer.start();
                                        mycounter.Stop();
                                        startgame=false;
                                        ratingBar.setRating(1);
                                        mycounter=new MyCountDownTimer(mycounter.millisInFuture+10*1000,1000);
                                        RefreshTimer();
                                        addExtraTime();

                                        if(isTimerEnd){
                                            mPlayergerisayim.stop();
                                            time.setTextColor(Color.parseColor("#FFFFFF"));
                                            sure=0;

                                    }
                                        mycounter.Start();
                                        startgame=true;
                                        level++;
                                        loadBrain(level);
                                        settingVibration(level);
                                        bonus=bonus+10;
                                        seviye.setText("Level "+level);
                                        final Animation myAnim = AnimationUtils.loadAnimation(MathGame.this, R.anim.bounce);
                                        seviye.startAnimation(myAnim);
                                    }
                                }

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{

                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if(sorucount==1){
                                    imgerror.setVisibility(View.VISIBLE);
                                    img.setVisibility(View.GONE);

                                    new CountDownTimer(500, 1000) {

                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            try {

                                                runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        imgerror.setVisibility(View.GONE);
                                                        img.setVisibility(View.GONE);

                                                        if(sorucount==0){

                                                            viewDidLoad();
                                                            sorusayisi++;
                                    MediaPlayer mPlayer = MediaPlayer.create(MathGame.this, R.raw.yenisoru);
                                    mPlayer.start();
                                                            sorucount++;

//                                    gecensure.setText("Yeni Soru Gelme Süresi :"+toplamsure+"\n"+
//                                    "Cevapta Geçen Süre :"+toplamsuremethot);
                                                        }
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }.start();

                                    MediaPlayer mPlayer = MediaPlayer.create(MathGame.this, R.raw.yanlis);
                                    mPlayer.start();

                                    ratingBar.setRating(1);
                                    userArray.clear();
                                    sorucount=0;resultl=0;resultr=0;
                                    yanlissayisi++;
                                    if(level==1){
                                        arrQuestionsl.clear();
                                        arrQuestionsr.clear();
                                    }else if(level ==2){
                                        arrx.clear();
                                        arrQuestionsr.clear();
                                    }else if(level>3){
                                        arrx.clear();
                                        arry.clear();
                                    }
                                }
                            }

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(resultl>resultr){
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if(sorucount==1){

                                    puan=puan+bonus;
                                    score.setText("Score : "+puan);
                                    imgerror.setVisibility(View.GONE);
                                    img.setVisibility(View.VISIBLE);
                                    new CountDownTimer(500, 1000) {

                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            try {

                                                runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        imgerror.setVisibility(View.GONE);
                                                        img.setVisibility(View.GONE);

                                                        if(sorucount==0){

                                                            viewDidLoad();
                                                            sorusayisi++;
//                                    MediaPlayer mPlayer = MediaPlayer.create(mActivity, R.raw.yenisoru);
//                                    mPlayer.start();
                                                            sorucount++;

//                                    gecensure.setText("Yeni Soru Gelme Süresi :"+toplamsure+"\n"+
//                                    "Cevapta Geçen Süre :"+toplamsuremethot);
                                                        }
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    }.start();
                                    MediaPlayer mPlayer = MediaPlayer.create(MathGame.this, R.raw.dogru);
                                    mPlayer.start();
                                    dogrusayisi++;
//                                    settingVibration(dogrusayisi);
                                    ratingBar.setRating(ratingBar.getRating()+1);
                                    userArray.clear();
                                    sorucount=0; resultl=0; resultr=0;


                                    if(level==1){
                                        arrQuestionsl.clear();
                                        arrQuestionsr.clear();
                                    }else if(level ==2){
                                        arrx.clear();
                                        arrQuestionsr.clear();
                                    }else if(level>3){
                                        arrx.clear();
                                        arry.clear();
                                    }
                                    if(ratingBar.getRating() == 5){
                                        mPlayer.stop();
                                        mPlayer = MediaPlayer.create(MathGame.this, R.raw.yenirekor);
                                        mPlayer.start();
                                        mycounter.Stop();
                                        startgame=false;
                                        ratingBar.setRating(1);
                                        mycounter=new MyCountDownTimer(mycounter.millisInFuture+10*1000,1000);
                                        RefreshTimer();
                                        addExtraTime();


                                        if(isTimerEnd){
                                            mPlayergerisayim.stop();
                                            time.setTextColor(Color.parseColor("#FFFFFF"));
                                            sure=0;

                                        }
                                        mycounter.Start();
                                        startgame=true;
                                        level++;
                                        loadBrain(level);
                                        settingVibration(level);
                                        bonus=bonus+10;
                                        seviye.setText("Level "+level);
                                        final Animation myAnim = AnimationUtils.loadAnimation(MathGame.this, R.anim.bounce);
                                        seviye.startAnimation(myAnim);
                                    }
                                }

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{

                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                if(sorucount==1){
                                    imgerror.setVisibility(View.VISIBLE);
                                    img.setVisibility(View.GONE);

                                    new CountDownTimer(500, 1000) {

                                        public void onTick(long millisUntilFinished) {

                                        }

                                        public void onFinish() {
                                            try {

                                                runOnUiThread(new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        imgerror.setVisibility(View.GONE);
                                                        img.setVisibility(View.GONE);

                                                        if(sorucount==0){

                                                            viewDidLoad();
                                                            sorusayisi++;
                                    MediaPlayer mPlayer = MediaPlayer.create(MathGame.this, R.raw.yenisoru);
                                    mPlayer.start();
                                                            sorucount++;

//                                    gecensure.setText("Yeni Soru Gelme Süresi :"+toplamsure+"\n"+
//                                    "Cevapta Geçen Süre :"+toplamsuremethot);
                                                        }
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }.start();

                                    MediaPlayer mPlayer = MediaPlayer.create(MathGame.this, R.raw.yanlis);
                                    mPlayer.start();
//                                    dogrusayisi=1;
//                                    settingVibration(dogrusayisi);
                                    ratingBar.setRating(1);
                                    userArray.clear();
                                    sorucount=0;resultl=0;resultr=0;
                                    yanlissayisi++;
                                    if(level==1){
                                        arrQuestionsl.clear();
                                        arrQuestionsr.clear();
                                    }else if(level ==2){
                                        arrx.clear();
                                        arrQuestionsr.clear();
                                    }else if(level>3){
                                        arrx.clear();
                                        arry.clear();
                                    }
                                }
                            }

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void onTouch(){
        leftbutton.setOnTouchListener(new View.OnTouchListener()
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

        rightbutton.setOnTouchListener(new View.OnTouchListener()
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


        btnProfile.setOnTouchListener(new View.OnTouchListener()
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

        playagain.setOnTouchListener(new View.OnTouchListener()
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



    }

    public void addExtraTime(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                LinearLayout.LayoutParams iparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                final ImageView  iv = new ImageView(MathGame.this);

                iv.setImageResource(R.drawable.tensecnew);
                iparams.height = 80;
                iparams.width = 80 ;
                iparams.rightMargin = 2;
                iparams.gravity = Gravity.CENTER;
                iv.setLayoutParams(iparams);



                DisplayMetrics displayMetrics = new DisplayMetrics();
                WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
                windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
                int deviceWidth = displayMetrics.widthPixels/2;
                int deviceHeight = displayMetrics.heightPixels/2;

                int[] positions = new int[2];
                time.getLocationOnScreen(positions);
                int xtvtime = positions[0]+ time.getWidth()/4;
                int ytvtime= positions[1]+time.getHeight()/4;

//                int[] konum = new int[2];
//                mActivity.tvhata[i].getLocationOnScreen(konum);
//                xtv = konum[0];
//                ytv= konum[1];


                TranslateAnimation anim = new TranslateAnimation( xtvtime,xtvtime, 0, ytvtime );
                anim.setDuration(1000);
                anim.setFillAfter( true );
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        iv.setImageResource(android.R.color.transparent);
                        final Animation myAnim = AnimationUtils.loadAnimation(MathGame.this, R.anim.bounce);
                        time.startAnimation(myAnim);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                iv.startAnimation(anim);
                frametv.addView(iv);


            }
        });

    }

    public void loadBrain(final int i){

        try {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    if(level>=5 & level<8 ){
                        brain.setImageResource(R.drawable.brain2);
                    }
                    if(level>=8 & level<11){
                        brain.setImageResource(R.drawable.brain3);
                    }
                    if(level>=11 & level<14){
                        brain.setImageResource(R.drawable.brain4);
                    }
                    if(level>=14){
                        brain.setImageResource(R.drawable.brain5);
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void getQuestions(String strRule){


        String[] arrRules = strRule.split(";");
        int ix=0;

        for(int  i=0 ;i<arrRules.length;i++){

            if(i==0){
                String rule = arrRules[i];
                if(rule.equals("S")){
                    Random rand = new Random();
                    int  n = rand.nextInt(Integer.MAX_VALUE)+1;
                    int s=  (1 + n % 30);

                    resultl=s;
                    arrQuestionsl = new ArrayList<>();
                    arrQuestionsl.add(s);
                    ix++;
                    leftnumber.setText(String.valueOf(arrQuestionsl.get(0)));
                    continue;

                }else{

                    int foundedDigits=0;

                    int fS= Integer.parseInt(rule.substring(0,1));
                    arrx = new ArrayList(fS);
                    arrOperatorsl = new ArrayList(fS-1);


                    while (foundedDigits<fS) {

                        Random rand = new Random();
                        int  n = rand.nextInt(Integer.MAX_VALUE)+1;

                        int s=  (1 + n % 30);


                        if(foundedDigits==0){
                            arrx.add(s);
                            foundedDigits++;
                            resultl=s;
                            continue;

                        }

                        Random rank = new Random();
                        String operators = rule.substring(1);
                        int operatorRnd = rank.nextInt(operators.length());
                        String operator = String.valueOf(operators.charAt(operatorRnd));

                        if(operator.equals("+"))
                        {
                            if(resultl+s<30)
                            {
                                arrx.add(s);
                                arrOperatorsl.add(operator);
                                foundedDigits++;
                                resultl=resultl+s;
                                continue;
                            }

                        }else if(operator.equals("-")){

                            if(resultl-s>0 && resultl-s<30)
                            {
                                arrx.add(s);
                                arrOperatorsl.add(operator);
                                foundedDigits++;
                                resultl=resultl-s;
                                continue;
                            }

                        }else if(operator.equals("*")) {

                            if(resultl*s>0 && resultl*s<30)
                            {
                                arrx.add(s);
                                arrOperatorsl.add(operator);
                                foundedDigits++;
                                resultl = resultl * s;
                                continue;
                            }

                        }else if(operator.equals("/")) {

                            if(resultl/s>0 && resultl/s<30 && resultl%s==0)
                            {
                                arrx.add(s);
                                arrOperatorsl.add(operator);
                                foundedDigits++;
                                resultl = resultl / s;
                                continue;
                            }

                        }
                    }
                }

                if(level==2 || level==3 || level==4){
                    leftnumber.setText(String.valueOf(arrx.get(0))+arrOperatorsl.get(0)+arrx.get(1));
                }else if(level==5 || level==6 || level==7 ||level==8 || level==9 || level==10){
                    leftnumber.setText(String.valueOf("("+arrx.get(0)+""+arrOperatorsl.get(0)+arrx.get(1))+")"+arrOperatorsl.get(1)+arrx.get(2) );
                }else if(level==11 || level==12 || level==13 ||level==14 || level==15 || level==16){
                    leftnumber.setText(String.valueOf("("+"("+arrx.get(0)+""+arrOperatorsl.get(0)+arrx.get(1))+")"+
                            arrOperatorsl.get(1)+arrx.get(2)+")"+arrOperatorsl.get(2)+arrx.get(3) );
                }else if(level>16){
                    leftnumber.setText(String.valueOf("("+"("+arrx.get(0)+""+arrOperatorsl.get(0)+arrx.get(1))+")"+
                            arrOperatorsl.get(1)+arrx.get(2)+")"+arrOperatorsl.get(2)+arrx.get(3) );
                }
            }else if(i==1){

                String rule = arrRules[i];
                if(rule.equals("S")){
                    Random rand = new Random();
                    int  n = rand.nextInt(Integer.MAX_VALUE)+1;
                    int s=  (1 + n % 30);

                    resultr=s;
                    arrQuestionsr = new ArrayList<>();
                    arrQuestionsr.add(s);
                    arrQuestionsr.add(ix, s);
                    rightnumber.setText(String.valueOf(arrQuestionsr.get(0)));
                    ix++;
                    continue;

                }else{

                    int foundedDigits=0;

                    int fS= Integer.parseInt(rule.substring(0,1));
                    arry = new ArrayList(fS);
                    arrOperatorsr = new ArrayList(fS-1);


                    while (foundedDigits<fS) {

                        Random rand = new Random();
                        int  n = rand.nextInt(Integer.MAX_VALUE)+1;

                        int s=  (1 + n % 30);


                        if(foundedDigits==0){
                            arry.add(s);
                            foundedDigits++;
                            resultr=s;
                            continue;
                        }

                        Random rank = new Random();

                        String operators = rule.substring(1);
                        int operatorRnd = rank.nextInt(operators.length());
                        String operator = String.valueOf(operators.charAt(operatorRnd));

                        if(operator.equals("+"))
                        {
                            if(resultr+s<30)
                            {
                                arry.add(s);
                                arrOperatorsr.add(operator);
                                foundedDigits++;
                                resultr=resultr+s;
                                continue;
                            }
                        }else if(operator.equals("-")){

                            if(resultr-s>0 && resultr-s<30)
                            {
                                arry.add(s);
                                arrOperatorsr.add(operator);
                                foundedDigits++;
                                resultr=resultr-s;
                                continue;
                            }

                        }else if(operator.equals("*")) {

                            if(resultr*s>0 && resultr*s<30)
                            {
                                arry.add(s);
                                arrOperatorsr.add(operator);
                                foundedDigits++;
                                resultr = resultr * s;
                                continue;
                            }

                        }else if(operator.equals("/")) {

                            if(resultr/s>0 && resultr/s<30 && resultr%s==0)
                            {
                                arry.add(s);
                                arrOperatorsr.add(operator);
                                foundedDigits++;
                                resultr = resultr / s;
                                continue;
                            }

                        }

                    }
                }
                if(level==3 || level==4 || level==5 || level==6 || level==7){
                    rightnumber.setText(String.valueOf(arry.get(0))+arrOperatorsr.get(0)+arry.get(1));
                }else if(level==8 || level==9 || level==10|| level==11 || level==12 || level==13){
                    rightnumber.setText(String.valueOf("("+arry.get(0)+""+arrOperatorsr.get(0)+arry.get(1))+")"+arrOperatorsr.get(1)+arry.get(2) );
                }else if(level==14 ||level==15 || level==16){
                    rightnumber.setText(String.valueOf("("+"("+arry.get(0)+""+arrOperatorsr.get(0)+arry.get(1))+")"+
                            arrOperatorsr.get(1)+arry.get(2)+")"+arrOperatorsr.get(2)+arry.get(3) );
                }else if(level > 16){
                    rightnumber.setText(String.valueOf("("+"("+arry.get(0)+""+arrOperatorsr.get(0)+arry.get(1))+")"+
                            arrOperatorsr.get(1)+arry.get(2)+")"+arrOperatorsr.get(2)+arry.get(3) );
                }

            }

        }

        if(resultl==resultr){
            viewDidLoad();
            return;
        }

    }

    private void viewDidLoad(){

        arrRules = new ArrayList<>();

        arrRules.add("S;S");                                 // level 1
        arrRules.add("2+;S");                                // level 2
        arrRules.add("2+-;2+-");                             // level 3
        arrRules.add("2+-*/;2+-*/");                         // level 4
        arrRules.add("3+++---*/;2+++---*/");                 // level 5
        arrRules.add("3+-/*;2+-/*");                         // level 6
        arrRules.add("3+-///***;2+-///***");                 // level 7
        arrRules.add("3++--/*;3++--/*");                     // level 8
        arrRules.add("3+-/*;3+-/*");                         // level 9
        arrRules.add("3+-///***;3+-///***");                 // level 10
        arrRules.add("4+++---/*;3+++---/*");                 // level 11
        arrRules.add("4+-/*;3+-/*");                         // level 12
        arrRules.add("4+-///***;3+-///***");                 // level 13
        arrRules.add("4+++---/*;4+++---/*");                 // level 14
        arrRules.add("4+-/*/;4+-/*/");                       // level 15
        arrRules.add("4+-///***;4+-///***");                 // level 16

        if(level==1){
            getQuestions(arrRules.get(0));
        }else if(level==2){
            getQuestions(arrRules.get(1));
        }else if(level==3){
            getQuestions(arrRules.get(2));
        }else if(level==4){
            getQuestions(arrRules.get(3));
        }else if(level==5){
            getQuestions(arrRules.get(4));
        }else if(level==6){
            getQuestions(arrRules.get(5));
        }else if(level==7){
            getQuestions(arrRules.get(6));
        }else if(level==8){
            getQuestions(arrRules.get(7));
        }else if(level==9){
            getQuestions(arrRules.get(8));
        }else if(level==10){
            getQuestions(arrRules.get(9));
        }else if(level==11){
            getQuestions(arrRules.get(10));
        }else if(level==12){
            getQuestions(arrRules.get(11));
        }else if(level==13){
            getQuestions(arrRules.get(12));
        }else if(level==14){
            getQuestions(arrRules.get(13));
        }else if(level==15){
            getQuestions(arrRules.get(14));
        }else if(level==16){
            getQuestions(arrRules.get(15));
        }

    }

    @Override
    public void onClick(View arg0) {
//        clickAnswer();
//        new ParticleSystem(this, 100, R.drawable.star_pink, 800)
//                .setSpeedRange(0.1f, 0.25f)
//                .oneShot(arg0, 100);
    }

    public class MyCountDownTimer {
        public long millisInFuture;
        private long countDownInterval;
        private boolean status;
        public  long sec;
        Handler handler;
        Runnable counter;

        public MyCountDownTimer(long pMillisInFuture, long pCountDownInterval) {
            this.millisInFuture = pMillisInFuture;
            this.countDownInterval = pCountDownInterval;
            status = false;
            Initialize();
        }

        public void Stop() {
            status = false;
        }

        public long getCurrentTime() {
            return millisInFuture/1000;
        }

        public void Start() {
            status = true;
        }
        public void Initialize()
        {
            handler = new Handler();
            Log.v("status", "starting");
            final Runnable counter = new Runnable(){

                public void run(){
                    sec = millisInFuture/1000;
                    kalansure = sec;
                    if(status) {

                        if(mycounter.getCurrentTime()==6){
                            sure++;
                            if(sure==1){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPlayergerisayim = MediaPlayer.create(MathGame.this, R.raw.gerisayim);
                                        mPlayergerisayim.start();
                                        time.setTextColor(Color.parseColor("#FF0000"));
                                        isTimerEnd=true;
                                    }
                                });
                            }

                        }

                        if(mycounter.getCurrentTime()<=0){
                            mycounter.Stop();
                            startgame=false;
                            final Dialog dialog = new Dialog(MathGame.this);

                            gecisReklam.show();
                            addDatabase();




                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            Window window = dialog.getWindow();
//                            window.setBackgroundDrawableResource(R.drawable.btn_background_orange);

                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setContentView(R.layout.custom_dialog);
                            Typeface tf = Typeface.createFromAsset(MathGame.this.getAssets(),"GoodDog.otf");
                            textView3 = (TextView) findViewById(R.id.textView3);
                            textView2 = (TextView) findViewById(R.id.textView2);
                            textView1 = (TextView) findViewById(R.id.textView1);

//                            textView3.setText("SCORE :"+""+puan);
//                            textView2.setText("CORRECT :"+""+dogrusayisi);
//                            textView1.setText( "WRONG : "+""+yanlissayisi);
//                            textView1.setTypeface(tf);
//                            textView2.setTypeface(tf);
//                            textView3.setTypeface(tf);

                            ImageButton dialogButtonCall = (ImageButton) dialog.findViewById(R.id.btnagainstart);
                            dialogButtonCall.setOnTouchListener(new View.OnTouchListener()
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
                            dialogButtonCall.setOnClickListener(new Button.OnClickListener() {
                                public void onClick(View v) {

                                    Intent intent = new Intent(MathGame.this,
                                            MathGame.class);

                                    startActivity(intent);
                                }
                            });

                            ImageButton dialogButtonClose = (ImageButton) dialog.findViewById(R.id.btnlogout);
                            dialogButtonClose.setOnTouchListener(new View.OnTouchListener()
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
                            dialogButtonClose.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            });

                            ImageButton dialogButtonopenprofile = (ImageButton) dialog.findViewById(R.id.btn_dialog_open_profile);
                            dialogButtonCall.setOnTouchListener(new View.OnTouchListener()
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

                            dialogButtonopenprofile.setOnClickListener(new Button.OnClickListener() {
                                public void onClick(View v) {

                                    Intent intent = new Intent(MathGame.this,
                                            ProfileActivity.class);

                                    startActivity(intent);
                                }
                            });

                            dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                                @Override
                                public boolean onKey(DialogInterface arg0, int keyCode,
                                                     KeyEvent event) {
                                    // TODO Auto-generated method stub
                                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                                    }
                                    return true;
                                }
                            });

                            dialog.show();

                            Log.v("status", "done");

                        } else {
                            Log.v("status", Long.toString(sec) + " seconds remain");
                            millisInFuture -= countDownInterval;
                            handler.postDelayed(this, countDownInterval);


                        }
                    } else {
                        Log.v("status", Long.toString(sec) + " seconds remain and timer has stopped!");
                        handler.postDelayed(this, countDownInterval);
                    }
                }
            };

            handler.postDelayed(counter, countDownInterval);

        }
    }

    private void customizeTypface(){
        Typeface tf = Typeface.createFromAsset(this.getAssets(),"GoodDog.otf");
        rightnumber.setTypeface(tf);
        leftnumber.setTypeface(tf);
        time.setTypeface(tf);
        seviye.setTypeface(tf);
        score.setTypeface(tf);
        soru.setTypeface(tf);
        txtstart.setTypeface(tf);
        txtbasla.setTypeface(tf);
        title.setTypeface(tf);
    }

    public void startAnimation(){
        CountDownAnimation countDownAnimation = new CountDownAnimation(txtstart, 3);
        countDownAnimation.start();
        Animation scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        countDownAnimation.setAnimation(scaleAnimation);
        countDownAnimation.setCountDownListener(new CountDownAnimation.CountDownListener() {
            @Override
            public void onCountDownEnd(CountDownAnimation animation) {
                txtbasla.setVisibility(View.VISIBLE);
                new CountDownTimer(1000, 1000) {

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        txtbasla.setVisibility(View.GONE);
                        viewDidLoad();
                        sorucount++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                firsttime=1;
                                mycounter = new MyCountDownTimer(10000, 1000);
                                mycounter.Start();
                                startgame=true;
                                RefreshTimer();
                            }
                        });

                    }

                }.start();
            }
        });
    }

    public void RefreshTimer()
    {
        final Handler handler = new Handler();
        final Runnable counter = new Runnable(){

            public void run(){
                time.setText("Time : "+Long.toString(mycounter.getCurrentTime()));
                handler.postDelayed(this, 100);

            }
        };

        handler.postDelayed(counter, 100);
    }

    private void settingVibration(int i){
        if(level>=1 & level<5 ){
            final Animation myAnim = AnimationUtils.loadAnimation(MathGame.this, R.anim.vibra);
            brain.startAnimation(myAnim);
        }else if(level>=5 & level<8 ){
            final Animation myAnim = AnimationUtils.loadAnimation(MathGame.this, R.anim.vibra1);
            brain.startAnimation(myAnim);
        }else if(level>=8 & level<11){
            final Animation myAnim = AnimationUtils.loadAnimation(MathGame.this, R.anim.vibra2);
            brain.startAnimation(myAnim);
        }else if(level>=11 & level<14){
            final Animation myAnim = AnimationUtils.loadAnimation(MathGame.this, R.anim.vibra3);
            brain.startAnimation(myAnim);
        }else if(level>=14){
            final Animation myAnim = AnimationUtils.loadAnimation(MathGame.this, R.anim.vibra4);
            brain.startAnimation(myAnim);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(startgame){
            mycounter.Stop();
        }

    }

    @Override
    public void onBackPressed() {

    }
}
