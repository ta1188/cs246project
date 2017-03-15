package com.example.cs246project.kindergartenprepapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

public class NameTraceActivity extends SkipTapActivity implements Runnable {

    // FrameLayout that holds the drawView and trace background.
    FrameLayout _frameLayout;

    // Model object for managing the name character values.
    private NameTraceModel _model;

    // The view control that allows the user to trace on a transparent canvas.
    private DrawView _drawView;

    // The pixel height/width of each image view (set dynamically).
    int _imageViewWidthHeight;

    // The pixel height of the views/layouts for tracing (set dynamically).
    int _layoutHeight;

    // The pixel width of the views/layouts for tracing (set dynamically).
    int _layoutWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the user's name and set the model with that name
        SharedPreferences settings = getSharedPreferences(AppConstants.sharePreferenceSettings, MODE_PRIVATE);
        _model = new NameTraceModel(settings.getString(AppConstants.sharePreferenceName, ""));

        // Set the content view layout
        setContentView(R.layout.name_activity_trace);

        _frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        // Set the _drawView
        _drawView = (DrawView) findViewById(R.id.nameTraceDrawView);

        // Build the background images from the traceCharacters
        setTraceBackgroundFromValues();

        playInstructions(getResources().getIdentifier("instruct_trace_letters_in_name_using_finger", "raw", getPackageName()));
    }

    /**
     * Set Trace Background From Values
     * Sets the background trace images using a list of string values (file names).
     */
    private void setTraceBackgroundFromValues() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout.post(this);
    }

    /**
     * Run
     * Used after dynamically getting the runtime height of the _framelayout so the background images
     * and _drawView can be sized appropriately.
     */
    @Override
    public void run() {
        // Get the dimensions of all the child views (drawView, bottomLayout, and imageViews).
        _imageViewWidthHeight = _frameLayout.getHeight();
        _layoutHeight = _imageViewWidthHeight;
        _layoutWidth = _model.getNumberOfCharacters() * _frameLayout.getHeight();

        // Setup layout parameters of the drawView
        if (_drawView != null) {
            FrameLayout.LayoutParams drawViewLayoutParams = new FrameLayout.LayoutParams(_layoutWidth, FrameLayout.LayoutParams.MATCH_PARENT);
            _drawView.setLayoutParams(drawViewLayoutParams);
        }

        // Setup layout parameters of the bottomLayout
        LinearLayout bottomLayout = (LinearLayout) findViewById(R.id.layoutBottom);
        FrameLayout.LayoutParams bottomLayoutParams = new FrameLayout.LayoutParams(_layoutWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        bottomLayout.setLayoutParams(bottomLayoutParams);

        // Add each letter as an image view to the bottomLayout
        List<String> values = _model.getValues();
        for (int i = 0; i < values.size(); i++) {
            // Set the imageView's image resource using value
            int resourceIndex = this.getResources().getIdentifier(values.get(i), "drawable", this.getPackageName());
            AppCompatImageView imageView = new AppCompatImageView(this);
            imageView.setImageResource(resourceIndex);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(_imageViewWidthHeight, _imageViewWidthHeight);
            bottomLayout.addView(imageView, layoutParams);
        }
    }

    /**
     * Scrolls/Skips to the next value in the name.
     * @param view The view that activated the function (e.g. button)
     */
    public void goToNextValue(View view) {
        int x = _frameLayout.getScrollX() + _imageViewWidthHeight;
        if (x < _layoutWidth) {
            _frameLayout.scrollTo(x, 0);
        }
    }

    /**
     * Scrolls/Skips to the previous value in the name.
     * @param view The view that activated the function (e.g. button)
     */
    public void goToPreviousValue(View view) {
        int x = _frameLayout.getScrollX() - _imageViewWidthHeight;
        if (x >= 0) {
            _frameLayout.scrollTo(x, 0);
        }
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
     * Runs the the actionst that should take place when a user is done with tracing their name.
     * @param view that activated this action.
     */
    public void onDoneButtonClick(View view) {
        // TODO: Display the snapshot popup
        finish();
    }

    /**
     * Return To Menu
     * Retuns the user to the main menu activity
     * @param view that activated this action.
     */
    public void returnToMenu(View view) {
        finish();
    }
}

