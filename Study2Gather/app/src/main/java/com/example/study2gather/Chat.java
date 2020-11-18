package com.example.study2gather;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Chat extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> splash.xml
        setContentView(R.layout.chat);
    }

}
