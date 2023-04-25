package my.edu.utar.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import my.edu.utar.moodtracker.utils.Shared;

public class SplashScreen extends AppCompatActivity {

    Animation anim;
    ImageView logo;
    TextView slogan;

    private static int SPLASH_SCREEN = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Shared.initialize(getBaseContext());

        anim = AnimationUtils.loadAnimation(this,R.anim.splash_anim);

        logo= findViewById(R.id.splashImage);
        slogan = findViewById(R.id.splashText);

        logo.setAnimation(anim);
        slogan.setAnimation(anim);

        slogan.setTypeface(Shared.fontLight);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

        },SPLASH_SCREEN);

    }
}