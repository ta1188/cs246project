package com.example.cs246project.kindergartenprepapp;

import android.support.v7.widget.AppCompatImageView;

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

public class LetterTraceableActivity extends CharacterTraceableActivity {

    protected void initializeModel() {
        _model = new LetterTraceableModel(this);
    }

    protected void initializeLayoutIndex() {
        setContentView(R.layout.letter_traceable_activity);
    }

    protected void initializeDrawView() {
        _drawView = (DrawView) findViewById(R.id.drawView);
    }

    protected void setTraceBackgroundFromValues(List<Integer> values) {
        // Get the views
        AppCompatImageView upperLetterImageView = (AppCompatImageView) findViewById(R.id.upperLetter);
        AppCompatImageView lowerLetterImageView = (AppCompatImageView) findViewById(R.id.lowerLetter);

        // Set the view images
        upperLetterImageView.setImageResource(_model.getCurrentValues().get(0));
        lowerLetterImageView.setImageResource(_model.getCurrentValues().get(1));
        // Set the view opacity
        upperLetterImageView.setAlpha(0.3f);
        lowerLetterImageView.setAlpha(0.3f);

        _drawView.randomizeCurrentColor();
    }
}
