package com.example.cs246project.kindergartenprepapp;

import android.os.Bundle;
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

public class LetterTraceActivity extends CharacterTraceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playInstructions(getResources().getIdentifier("instruct_trace_letter_with_finger", "raw", getPackageName()));
    }

    protected void initializeModel() {
        _model = new LetterTraceModel(this);
    }

    protected void initializeLayoutIndex() {
        setContentView(R.layout.letter_activity_trace);
    }

    protected void initializeDrawView() {
        _drawView = (DrawView) findViewById(R.id.LetterTraceDrawView);
    }

    protected void setTraceBackgroundFromValues(List<String> values) {
        // Setup the Upper case letter image view
        int upperCaseResourceIndex = this.getResources().getIdentifier(values.get(0), "drawable", this.getPackageName());
        AppCompatImageView upperCaseImageView = (AppCompatImageView) findViewById(R.id.upperLetter);
        upperCaseImageView.setImageResource(upperCaseResourceIndex);
        upperCaseImageView.setAlpha(0.5f);

        // Setup the Lower case letter image view
        int lowerCaseResourceIndex = this.getResources().getIdentifier(values.get(1), "drawable", this.getPackageName());
        AppCompatImageView lowerCaseImageView = (AppCompatImageView) findViewById(R.id.lowerLetter);
        lowerCaseImageView.setImageResource(lowerCaseResourceIndex);
        lowerCaseImageView.setAlpha(0.5f);
    }
}
