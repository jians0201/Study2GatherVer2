package com.example.study2gather;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import pl.droidsonroids.gif.GifImageView;

public class Splash extends AppCompatActivity {

    private GifImageView splashAppName;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> splash.xml
        setContentView(R.layout.splash);

        anim = AnimationUtils.loadAnimation(this, R.anim.fade);
        splashAppName = findViewById(R.id.splashAppName);
        splashAppName.startAnimation(anim);

        Thread splashThread = new Thread() {

            public void run() {
                try {
                    // sleep time in milliseconds (3000 = 3sec)
                    sleep(3000);

                }  catch(InterruptedException e) {
                    // Trace the error
                    e.printStackTrace();
                } finally
                {
                    // Launch the MainActivity class
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                }

            }
        };
        // To Start the thread
        splashThread.start();
    }
}


