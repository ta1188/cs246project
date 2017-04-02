package com.example.cs246project.kindergartenprepapp;

import android.view.MotionEvent;

/**
 * An interface for class that handle or use {@link MediaButton}.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public interface MediaButtonHandler {

    /**
     * Actions for when the MediaButton's audio is complete.
     */
    public void onAudioComplete();

    /**
     * Actions for when the MediaButton is touched.
     * @param mediaButton the button being touched.
     * @param event the touch event.
     */
    public void onMediaButtonTouched(MediaButton mediaButton, MotionEvent event);
}
