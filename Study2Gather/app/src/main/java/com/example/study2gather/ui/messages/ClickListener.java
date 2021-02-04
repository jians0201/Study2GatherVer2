package com.example.study2gather.ui.messages;

import android.view.GestureDetector;
import android.view.MotionEvent;

class ClickListener extends GestureDetector.SimpleOnGestureListener {

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }
};