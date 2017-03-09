package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ImageButton;

/**
 * Created by Michael Lucero on 3/3/17.
 */


public class MediaButton extends ImageButton implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mediaPlayer;
    private Integer _buttonName;
    private Boolean isAnswer;
    private String _imageFile;
    private String[] array;

    public MediaButton(String activityType, Context context, Integer buttonName, Boolean isAnswer) {
        super(context);



    }

    /*int assciiValue;
    char myChar;

    assciiValue = questionBank.get(_buttonName);
    myChar = (char) letter;

    // adds
    _soundResourceFile = Uri.parse("android.resource://com.example.cs246project." +
            "kindergartenprepapp/raw/" + audioFileName);

    // int a = R.drawable.raccoon;
    imageFileName = "android.resource://com.example.cs246project.kindergartenprepapp/drawable/" + imageFileName;
    _imageResourceFile = Drawable.createFromPath(imageFileName);

    // try
    _mediaPlayer = MediaPlayer.create(context, _soundResourceFile);
    _imageButton = new ImageButton(context);
    */

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    //_imageButton.setBackground(_imageResourceFile);



}
