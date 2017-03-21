package com.example.cs246project.kindergartenprepapp;

import android.view.MotionEvent;

/**
 * Created by Dan on 3/13/2017.
 */

public interface MediaButtonHandler {

    /**
     *
     */
    public void onAudioComplete();

    /**
     *
     */
    public void onMediaButtonTouched(MediaButton mediaButton, MotionEvent event);
}
