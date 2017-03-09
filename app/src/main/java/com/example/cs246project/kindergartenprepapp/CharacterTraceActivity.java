package com.example.cs246project.kindergartenprepapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

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

abstract public class CharacterTraceActivity extends AppCompatActivity {

    //
    protected CharacterTraceModel _model;

    // The view control that allows the user to trace on a transparent canvas
    private DrawView _drawView;

    private ImageView _imageViewUpper;
    private ImageView _imageViewLower;

    // The layout that has the transparent DrawView and sits on top of the background (bottom).
    private FrameLayout _topLayout;

    // The layout that holds the background trace character images that sits underneath the top layout.
    private FrameLayout _bottomLayout;

    // The layout index for the .xml file to use.
    protected int _layoutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instantiateModel();

        // Get the layoutIndex
        initializeLayoutIndex();

        // Set the content view using the _layoutIndex.
        setContentView(_layoutIndex);

        // Initialize the controls/layouts
        _topLayout = (FrameLayout) findViewById(R.id.LayoutTop);
        _bottomLayout = (FrameLayout) findViewById(R.id.LayoutBottom);


        // Build the transparent draw view that sits on top of the background layer
        int widthOfCombinedCharacters = 1000;
        int heightOfCharacters = 500;
        FrameLayout.LayoutParams drawViewLayoutParams = new FrameLayout.LayoutParams(widthOfCombinedCharacters, ViewGroup.LayoutParams.MATCH_PARENT);
        drawViewLayoutParams.setMarginStart(0);
        drawViewLayoutParams.gravity = Gravity.CENTER;
        _drawView = new DrawView(this);
        _topLayout.addView(_drawView, drawViewLayoutParams);

        setTraceBackgroundFromValues(_model.getCurrentValues());

    }

    /**
     * Initialize Layout Index
     * Initializes or sets the _layoutIndex.
     */
    public void initializeLayoutIndex() {
        _layoutIndex = R.layout.character_activity_trace;
    }

    /**
     * Clear Draw View
     * Clears all the tracing from the _drawView.
     * @param view the button that caused this action to be called
     */
    public void clearDrawView(View view) {
        _drawView.clearView();
    }

    /**
     * On Done Button Click
     * Checks if the user has completed the activity or not and handles new values if not complete.
     * @param view the button that caused this action to be called
     */
    public void onDoneButtonClick(View view) {
        if (_model.isComplete()) {
            finish();
        } else {
            _model.goToNextValue();
            _bottomLayout.removeAllViews();
            clearDrawView(view);
            setTraceBackgroundFromValues(_model.getCurrentValues());
        }
    }

    /**
     * Set Trace Background From Values
     * Sets the background trace images using a list of string values (file names).
     * @param values used as the background to trace over
     */
    private void setTraceBackgroundFromValues(List<String> values) {
        for (int i = 0; i < values.size(); i++) {
            // Set the imageView's image resource using value
            int resourceIndex = this.getResources().getIdentifier(values.get(i), "drawable", this.getPackageName());
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(resourceIndex);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(800, ViewGroup.LayoutParams.MATCH_PARENT);
            setLayoutParamStartPoint(layoutParams, i * 400);
            _bottomLayout.addView(imageView, layoutParams);
        }
    }

    abstract protected void instantiateModel();

    abstract protected void setLayoutParamStartPoint(FrameLayout.LayoutParams layoutParams, int startPoint);

}
