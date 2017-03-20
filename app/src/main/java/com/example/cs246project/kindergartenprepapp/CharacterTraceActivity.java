package com.example.cs246project.kindergartenprepapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.List;

/**
 * An Activity that allows the user to trace letters in the alphabet, numbers, or words (including
 * their name). This is meant to help them improve their fine motor, writing, and recognition
 * skills.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

abstract public class CharacterTraceActivity extends SkipTapActivity {

    // Model object associated with the activity
    protected CharacterTraceModel _model;

    // DrawView object associated with the activity
    protected DrawView _drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the model object
        initializeModel();
        // Set the layout
        initializeLayoutIndex();
        // Set the draw view
        initializeDrawView();
        // Set the background
        setTraceBackgroundFromValues(_model.getCurrentValues());

        // Hide the previous button since the activity is at the start
        if (_model.isAtBeginning()) {
            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
            previousButton.setVisibility(View.INVISIBLE);
        }

        // Hide button if the activity isn't already complete
        if (!_model.isComplete()) {
            FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
            doneButton.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected void playInstructions(int instructionsAudioResourceIndex){
        // Initialize a media player with the audio resource
        _instructionsMediaPlayer = MediaPlayer.create(this, instructionsAudioResourceIndex);

        _instructionsMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playCurrentValueAudio();
            }
        });

        _instructionsMediaPlayer.start();
    }

    /**
     * Initialize Model
     * Initializes or sets the _model.
     */
    abstract protected void initializeModel();

    /**
     * Initialize DrawView
     * Initializes or sets the _drawView.
     */
    abstract protected void initializeDrawView();

    /**
     * Initialize Layout Index
     * Sets Content View with a layout resource index(.xml layout);.
     */
    abstract protected void initializeLayoutIndex();

    /**
     * Set Trace Background From Values
     * Sets the background trace images using a list of string values (file names).
     * @param values used as the background to trace over
     */
    abstract protected void setTraceBackgroundFromValues(List<String> values);

    /**
     * Clear Draw View
     * Clears all the tracing from the _drawView.
     * @param view the button that caused this action to be called
     */
    public void clearDrawView(View view) {
        _drawView.clearView();
    }

    /**
     * Return To Menu
     * Returns to the previous activity that called this activity.
     * @param view the button that caused this action to be called
     */
    public void returnToMenu(View view) {
        this.finish();
    }

    /**
     * On Done Button Click
     * Checks if the user has completed the activity or not and handles new values if not complete.
     * @param view the button that caused this action to be called
     */
    public void onDoneButtonClick(View view) {
        if (_model.isComplete()) {

            // Play a completion sound
            MediaPlayer mediaPlayer = MediaPlayer.create(this, _model.getCompletionAudioIndex());
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();

                    // Go back to the main menu
                    finish();
                }
            });
            mediaPlayer.start();
        };
    }

    /**
     * Go Back To Next Value
     * Displays the next value (un-traced), if any.
     */
    public void goToNextValue(View view) {
        if (!_model.isComplete()) {
            _model.goToNextValue();
            clearDrawView(view);
            setTraceBackgroundFromValues(_model.getCurrentValues());
            playCurrentValueAudio();
            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
            if (previousButton.getVisibility() != View.VISIBLE) {
                previousButton.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
                previousButton.startAnimation(fadeIn);
            }
        }

        if (_model.isComplete()) {
            // Hide the next button since its at the end
            FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
            nextButton.setVisibility(View.INVISIBLE);

            // Show the done button
            FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
            doneButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Go Back To Previous Value
     * Displays the previous value (un-traced), if any.
     */
    public void goToPreviousValue(View view) {
        if (!_model.isAtBeginning()) {
            _model.goToPreviousValue();
            clearDrawView(view);
            setTraceBackgroundFromValues(_model.getCurrentValues());
            playCurrentValueAudio();
            FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
            if (nextButton.getVisibility() != View.VISIBLE) {
                nextButton.setVisibility(View.VISIBLE);
                Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
                nextButton.startAnimation(fadeIn);
            }
        }

        if (_model.isAtBeginning()){
            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
            previousButton.setVisibility(View.INVISIBLE);
        }

        // Hide the done button if not already hidden
        FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
        if (doneButton.getVisibility() == View.VISIBLE && !_model.isComplete()) {
            doneButton.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Plays the audio resource that applies to the currently viewed value.
     */
    protected void playCurrentValueAudio() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, _model.getCurrentValueAudioResourceIndex());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mp != null) {
                    mp.release();
                }
            }
        });

        mediaPlayer.start();
    }
}
