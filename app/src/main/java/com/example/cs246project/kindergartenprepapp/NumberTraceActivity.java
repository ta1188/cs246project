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

public class NumberTraceActivity extends CharacterTraceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playInstructions(getResources().getIdentifier(_model.getInstructionsFileName(), "raw", getPackageName()));
    }

    protected void initializeModel() {
        _model = new NumberTraceModel(this);
    }

    protected void initializeLayoutIndex() {
        setContentView(R.layout.number_activity_trace);
    }

    protected void initializeDrawView() {
        _drawView = (DrawView) findViewById(R.id.numberTraceDrawView);
    }

    protected void setTraceBackgroundFromValues(List<String> values) {
        int resourceIndex = this.getResources().getIdentifier(values.get(0), "drawable", this.getPackageName());
        AppCompatImageView imageView = (AppCompatImageView) findViewById(R.id.number);
        imageView.setImageResource(resourceIndex);
        imageView.setAlpha(0.5f);
    }
}
