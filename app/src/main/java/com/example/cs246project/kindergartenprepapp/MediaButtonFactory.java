package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.xml.transform.Templates;

/**
 * Button with sound and image
 * <p>
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-02-23
 */

public class MediaButtonFactory implements ButtonAudio {
    Uri _soundResourceFile;
    Drawable _imageResourceFile;
    ArrayList<Integer> randomizeList = new ArrayList<Integer>();
    int buttonCount;

    public MediaButtonFactory(int buttonCount) {
        this.buttonCount = buttonCount;
    }

    // constructor will create a button with passed audio on click and assign an image from raw
    //public MediaButtonFactory(String activityType, String buttonCount, Context context) {

        // User requests button type and how many buttons to make. Returns array of randomized buttons
        public MediaButtonFactory[] createMediaButton(String activityType, int buttonCount, Context context) {



            switch (activityType) {
                case "count" :


                    for (int i = 0 ; i < buttonCount; i++) {
                        ArrayList<NumberButton> countButtonArray = new ArrayList<NumberButton>();

                        if (i == 0);
                            // sets the answer to true
                            //countButtonArray.add(new NumberButton(context, true, name));
                        else
                            // set everything else to false
                            //countButtonArray.add(new CountButton(context, false));

                        // randomize position in the array
                        Collections.shuffle(countButtonArray);
                    }
            }


            return null;
        }

    @Override
    public void playAudio() {

    }

/*
        // adds
        _soundResourceFile = Uri.parse("android.resource://com.example.michaellucero." +
                "musicapp2/raw/" + audioFileName);

        // int a = R.drawable.raccoon;
        imageFileName = "android.resource://com.example.michaellucero.musicapp2/drawable/" + imageFileName;
        _imageResourceFile = Drawable.createFromPath(imageFileName);

        // try
        _mediaPlayer = MediaPlayer.create(context, _soundResourceFile);
        _imageButton = new ImageButton(context);
        _imageButton.setBackground(_imageResourceFile);

    }

    @Override
    public void playAudio() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                //_mediaPlayer.setLooping(false);
                _mediaPlayer.start();
            }
        };
    }*/
}



