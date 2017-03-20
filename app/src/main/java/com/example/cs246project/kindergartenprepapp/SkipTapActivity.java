package com.example.cs246project.kindergartenprepapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * The super Activity of all SkipTap activities which has common member variables and member
 * functions.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-15
 */

abstract class SkipTapActivity extends AppCompatActivity {

    protected MediaPlayer _instructionsMediaPlayer;

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
        _instructionsMediaPlayer = MediaPlayer.create(this, instructionsAudioResourceIndex);

        _instructionsMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                _instructionsMediaPlayer.release();
                onInstructionsAudioComplete();

            }
        });

        // Play the first audio track
        _instructionsMediaPlayer.start();
    }

    public void onInstructionsAudioComplete() {
        // Do nothing here.
        // Descendants may Override this if they want (not abstract == not required)
    }
}
