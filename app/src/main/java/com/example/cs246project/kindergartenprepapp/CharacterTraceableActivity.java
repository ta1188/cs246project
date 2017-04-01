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

abstract public class CharacterTraceableActivity extends TraceableActivity {

    @Override
    public void onInstructionsAudioComplete() {
        super.onInstructionsAudioComplete();
        playCurrentValueAudio();
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
     * Go Back To Next Value
     * Displays the next value (un-traced), if any.
     */
    public void goToNextValue(View view) {
        if (!_model.isComplete()) {
            enableAllButtons(false);
            // Save the paths of the letter to be used again
//            _model.setCurrentValuePaths(_drawView.getPaths());

            // Go to the next character
            _model.goToNextValue();

            // Setup the tracing background with the next letter
            clearAllTracings(view);
//            _drawView.setPaths(_model.getCurrentValuePath());
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
            enableAllButtons(false);
            // Save the paths of the letter to be used again
//            _model.setCurrentValuePaths(_drawView.getPaths());

            // Go to the previous character
            _model.goToPreviousValue();


            // Setup the tracing background with the next letter
            clearAllTracings(view);
//            _drawView.setPaths(_model.getCurrentValuePath());
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
}
