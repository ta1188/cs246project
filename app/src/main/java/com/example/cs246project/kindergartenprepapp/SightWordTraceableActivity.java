package com.example.cs246project.kindergartenprepapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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

public class SightWordTraceableActivity extends TraceableActivity {

    @Override
    protected void initializeModel() {
        _model = new SightWordTraceableModel(this);
    }

    @Override
    protected void initializeDrawView() {
        _drawView = (DrawView) findViewById(R.id.drawView);
    }

    @Override
    protected void initializeLayoutIndex() {
        setContentView(R.layout.sight_word_traceable_activity);
    }

    /**
     * Set Trace Background From Values
     * Sets the background trace images using a list of string values (file names).
     */
    @Override
    protected void setTraceBackgroundFromValues(List<Integer> values) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.letterLayout);
        layout.removeAllViews();

        for (int i = 0; i < values.size(); i++) {
            AppCompatImageView imageView = new AppCompatImageView(this);
            imageView.setImageResource(values.get(i));
            LinearLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 75, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setAlpha(0.5f);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setAdjustViewBounds(true);

            layout.addView(imageView);
        }
        _drawView.randomizeCurrentColor();
    }

    /**
     *
     */
    @Override
    public void onInstructionsAudioComplete() {
        super.onInstructionsAudioComplete();
        playCurrentValueAudio();
    }

    /**
     * Go Back To Next Value
     * Displays the next value (un-traced), if any.
     */
    public void goToNextValue(View view) {
        if (!_model.isComplete()) {
            enableAllButtons(false);
            _model.goToNextValue();
            clearAllTracings(view);
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
            _model.goToPreviousValue();
            clearAllTracings(view);
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

