package com.example.cs246project.kindergartenprepapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
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

//        if (_model.isAtBeginning()) {
//            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
//            previousButton.hide();
//        }
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
            finish();
        };
    }

    /**
     * Go Back To Previous Value
     * Displays the previous value (un-traced), if any.
     */
    public void goToNextValue(View view) {
        if (!_model.isComplete()) {
            _model.goToNextValue();
            clearDrawView(view);
            setTraceBackgroundFromValues(_model.getCurrentValues());
            playCurrentValueAudio();
//            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnNext);
//            if (!_model.isAtBeginning() && previousButton.isShown()) {
//                previousButton.setVisibility(View.INVISIBLE);
//                previousButton.show();
//            }
//        } else {
//            FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
//            nextButton.hide();
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
//            FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
//            if (!_model.isComplete() && nextButton.isShown()) {
//                nextButton.setVisibility(View.INVISIBLE);
//                nextButton.show();
//            }
//        } else {
//            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
//            previousButton.hide();
        }
    }

    /**
     * Plays the audio resource that applies to the currently viewed value.
     */
    protected void playCurrentValueAudio() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, _model.getCurrentValueAudioResourceIndex());
        mediaPlayer.start();
    }
}
