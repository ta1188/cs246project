package com.example.cs246project.kindergartenprepapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import java.util.List;

/**
 * Created by Dan on 3/31/2017.
 */

public abstract class TraceableActivity extends SkipTapActivity {

    // Model object associated with the activity
    protected TraceableModel _model;

    // DrawView object associated with the activity
    protected DrawView _drawView;

    private FloatingActionButton _buttonMenu;
    private FloatingActionButton _buttonPrevious;
    private FloatingActionButton _buttonClear;
    private FloatingActionButton _buttonNext;
    private FloatingActionButton _buttonDone;

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

        playInstructions(getResources().getIdentifier(_model.getInstructionsFileName(), "raw", getPackageName()));

        _buttonMenu = (FloatingActionButton) findViewById(R.id.btnMenuReturn);
        _buttonPrevious = (FloatingActionButton) findViewById(R.id.btnPrevious);
        _buttonClear = (FloatingActionButton) findViewById(R.id.btnClear);
        _buttonNext = (FloatingActionButton) findViewById(R.id.btnNext);
        _buttonDone = (FloatingActionButton) findViewById(R.id.btnDone);

        enableAllButtons(false);
    }

    public void enableAllButtons(boolean isEnable) {
        _buttonMenu.setEnabled(isEnable);
        _buttonPrevious.setEnabled(isEnable);
        _buttonClear.setEnabled(isEnable);
        _buttonNext.setEnabled(isEnable);
        _buttonDone.setEnabled(isEnable);
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
    abstract protected void setTraceBackgroundFromValues(List<Integer> values);

    /**
     * Clear Last Tracing
     * Clears the last tracing by the user from the _drawView.
     * @param view the button that caused this action to be called
     */
    public void clearLastTracing(View view) {
        _drawView.clearPreviousPath();
    }

    /**
     * Clear All Tracings
     * Clears all the tracings by the user from the _drawView.
     * @param view the button that caused this action to be called
     */
    public void clearAllTracings(View view) {
        _drawView.clearAllPaths();
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
    abstract public void goToNextValue(View view);

    /**
     * Go Back To Previous Value
     * Displays the previous value (un-traced), if any.
     */
    abstract public void goToPreviousValue(View view);

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
                    enableAllButtons(true);
                }
            }
        });

        mediaPlayer.start();
    }

    @Override
    protected String getInstructionToastText() {
        return "Trace with your Finger";
    }

}
