package com.example.cs246project.kindergartenprepapp;

import android.support.v7.widget.AppCompatImageView;

import java.util.List;

/**
 * An Activity that allows the user to trace numbers.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class NumberTraceableActivity extends CharacterTraceableActivity {

    protected void initializeModel() {
        _model = new NumberTraceableModel(this);
    }

    protected void initializeLayoutIndex() {
        setContentView(R.layout.number_traceable_activity);
    }

    protected void initializeDrawView() {
        _drawView = (DrawView) findViewById(R.id.drawView);
    }

    protected void setTraceBackgroundFromValues(List<String> values) {
        // Get the view
        AppCompatImageView imageView = (AppCompatImageView) findViewById(R.id.imageView);

        // Set the view image
        imageView.setImageResource(getResources().getIdentifier(_model.getCurrentValues().get(0), "drawable", getPackageName()));
    }
}
