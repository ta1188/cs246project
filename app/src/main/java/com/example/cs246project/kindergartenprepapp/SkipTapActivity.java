package com.example.cs246project.kindergartenprepapp;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

/**
 * Created by Dan on 3/15/2017.
 */

abstract class SkipTapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Plays the instructions audio for a given index.
     * @param instructionsAudioResourceIndex Audio resource index of the instruction to be played.
     */
    protected void playInstructions(int instructionsAudioResourceIndex) {
        // Initialize a media player with the audio resource
        MediaPlayer instructionsMediaPlayer = MediaPlayer.create(this, instructionsAudioResourceIndex);

        // Play the first audio track
        instructionsMediaPlayer.start();
    }
}
