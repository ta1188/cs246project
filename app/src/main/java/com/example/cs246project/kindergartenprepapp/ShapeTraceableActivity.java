package com.example.cs246project.kindergartenprepapp;

import android.support.v7.widget.AppCompatImageView;

import java.util.List;

/**
 * An Activity that allows the user to trace shapes.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class ShapeTraceableActivity extends CharacterTraceableActivity {

    protected void initializeModel() {
        _model = new ShapeTraceableModel(this);
    }

    protected void initializeLayoutIndex() {
        setContentView(R.layout.shape_traceable_activity);
    }

    protected void initializeDrawView() {
        _drawView = (DrawView) findViewById(R.id.drawView);
    }

    protected void setTraceBackgroundFromValues(List<Integer> values) {
        // Get the view
        AppCompatImageView imageView = (AppCompatImageView) findViewById(R.id.imageView);

        // Set the view image
        imageView.setImageResource(_model.getCurrentValues().get(0));

        // Set the view opacity
        imageView.setAlpha(0.5f);

        _drawView.randomizeCurrentColor();
    }

}
