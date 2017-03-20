package com.example.cs246project.kindergartenprepapp;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.cs246project.kindergartenprepapp.R.raw.music_melody;

/**
 * Created by Michael Lucero on 3/15/17.
 */

public class BackgroundSoundService extends Service {

    private MediaPlayer themeMediaPlayer;
    ArrayList<Integer> musicArray = new ArrayList<>(Arrays.asList(music_melody,
            R.raw.music_melody_and_drums, R.raw.music_verse, R.raw.music_verse_and_drums,
            R.raw.music_verse_slow, R.raw.music_verse_slow_and_drums,
            R.raw.music_verse_slow_violin, R.raw.music_verse_slower, R.raw.music_verse_upbeat,
            R.raw.music_verse_upbeat_and_drums));

    private Integer trackIndex = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Collections.shuffle(musicArray.subList(1, 9));


        themeMediaPlayer = MediaPlayer.create(this , R.raw.music_melody);

        themeMediaPlayer.setVolume(0.2f, 0.2f);
        themeMediaPlayer.start();

        themeMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                themeMediaPlayer.release();
                themeMediaPlayer = MediaPlayer.create(getApplicationContext() , musicArray.get(trackIndex));
                trackIndex++;
                themeMediaPlayer.setVolume(0.2f, 0.2f);
                themeMediaPlayer.start();
                themeMediaPlayer.setOnCompletionListener(this);

                if (trackIndex == 8) {
                    Collections.shuffle(musicArray.subList(0, 9));
                    trackIndex = 0;
                }

            }
        });
        return START_STICKY;



    }


//    @Override
//    public void onCompletion(MediaPlayer mp) {
//
//        themeMediaPlayer = MediaPlayer.create(getApplicationContext() , musicArray.get(trackIndex));
//        trackIndex++;
//        themeMediaPlayer.start();
//
//    }

    public void startService1() {
        startService(new Intent(this, BackgroundSoundService.class));
    }

    public void stopService() {
        startService(new Intent(this, BackgroundSoundService.class));
    }

    public void stopPlayingBackgroundMusic() {

    }

    @Override
    public void onDestroy() {

        trackIndex = 10;
        super.onDestroy();
        themeMediaPlayer.stop();
        themeMediaPlayer.release();
        stopSelf();
    }


//
//     MediaPlayer.create(getApplicationContext() , musicArray.get(trackIndex));
//        trackIndex++;
//        themeMediaPlayer.start();
//
//
//        themeMediaPlayer.setOnCompletionListener(new OnCompletionListener(){
//
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//
//
//            }
//
//        });
//
//    }
//
//

//    @Override
//    public void onCompletion(MediaPlayer mp) {
//
//    }

    //    protected randomValuesGenerator() {
//
//        Collections.shuffle(musicArray.subList());
//        List<T> valueList = new ArrayList<>();
//        Random randomValueRetriever = new Random();
//
//        // get random first value with conditions based on available answer bank questions
//        _answer = _answerBank.get(randomValueRetriever.nextInt(_answerBank.size()));
//
//        // check if random number already exists where -1 means it doesn't exists
//        valueList.add(_answer);
//
//        // generate the rest of the buttons with random values that don't match button 1st
//        //    button made
//        while (valueList.size() < _optionCount) {
//
//            // get random value
//            T randomNum = _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));
//
//            // check if number already added, not added if -1 is returned
//            if (valueList.indexOf(randomNum) == -1)
//                valueList.add(randomNum);
//        }
//
//        return valueList;
//    }


//    @Override
//    protected void onStop() {
//        super.onStop();
//        stopService(new Intent(this, BackgroundSoundService.class));
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        startService(new Intent(this, BackgroundSoundService.class));
//
//
//    }


    //// Change from 200 to 150
     //btn.removeCallbacks(enableDisable);    ////// added to handle extra presses

    /////// changed from down to up
}
