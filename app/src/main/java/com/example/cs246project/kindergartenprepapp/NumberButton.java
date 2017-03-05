package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ImageButton;

/**
 * Created by lancerguy on 3/3/17.
 */

public class NumberButton extends ImageButton  {

    private MediaPlayer _mediaPlayer;
    private boolean answer;

    public NumberButton(Context context, Boolean isAnswer) {
        super(context);

        // set if correct answer
        answer = isAnswer;
    }
}
