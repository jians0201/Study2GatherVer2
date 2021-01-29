package com.example.study2gather;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import pl.droidsonroids.gif.GifImageView;

public class Splash extends AppCompatActivity {

    private GifImageView splashAppName;
    private GifImageView splashBg;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> splash.xml
        setContentView(R.layout.splash);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade);
        splashAppName = findViewById(R.id.splashAppName);
        splashBg = findViewById(R.id.splashBackground);
        splashAppName.startAnimation(anim);
        Animation flashAnimation=new AlphaAnimation(0.8f,0.0f);
        flashAnimation.setDuration(210);


        Thread splashThread = new Thread() {

            public void run() {
                try {
                    // sleep time in milliseconds (3000 = 3sec)
                    sleep(3000);



                    flashAnimation.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            splashBg.animate().translationY(-3000);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    splashBg.startAnimation(flashAnimation);
                }  catch(InterruptedException e) {
                    // Trace the error
                    e.printStackTrace();
                }

            }
        };
        // To Start the thread
        splashThread.start();
    }
}


