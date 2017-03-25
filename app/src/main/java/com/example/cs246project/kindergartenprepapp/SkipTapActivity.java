package com.example.cs246project.kindergartenprepapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * The super Activity of all SkipTap activities which has common member variables and member
 * functions.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-15
 */

abstract class SkipTapActivity extends AppCompatActivity {

    protected BackgroundAudioModel _backgroundAudioModel;
    protected MediaPlayer _instructionsMediaPlayer;
    protected int _instructionsAudioResourceIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        _backgroundAudioModel = new BackgroundAudioModel(this);
    }

    /**
     * Plays the instructions audio for a given index.
     * @param instructionsAudioResourceIndex Audio resource index of the instruction to be played.
     */
    protected void playInstructions(int instructionsAudioResourceIndex) {

        // keep track for onResume
        _instructionsAudioResourceIndex = instructionsAudioResourceIndex;

        if (_instructionsMediaPlayer != null) {
            _instructionsMediaPlayer.release();
            _instructionsMediaPlayer = null;
        }

        // Initialize a media player with the audio resource
        _instructionsMediaPlayer = MediaPlayer.create(this, instructionsAudioResourceIndex);

        _instructionsMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                _instructionsMediaPlayer.release();
                _instructionsMediaPlayer = null;
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

    public void stopEverything() {
        // Do nothing here.
        // Descendants Override
    }

    /**
     * Will kill the media player
     */
    public void stopAudio() {
        if (_instructionsMediaPlayer != null) {
            _instructionsMediaPlayer.release();
            _instructionsMediaPlayer = null;
        }

        _backgroundAudioModel.stopBackgroundAudio();
    }

    /**
     * Will start audio for background music and instructions
     */
    public void startAudio() {

        playInstructions(_instructionsAudioResourceIndex);

        _backgroundAudioModel.startBackgroundAudio(this);
    }

    @Override
    protected void onStop() {
       stopEverything();

        super.onStop();
    }

    @Override
    protected void onPause() {
        stopEverything();

        super.onPause();
    }

    @Override
    protected void onResume() {

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if(!_backgroundAudioModel.isPlaying()) {
            startAudio();
        }

        super.onResume();
    }

    @Override
    protected void onRestart() {

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onRestart();
    }
}


