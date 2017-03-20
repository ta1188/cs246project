package com.example.cs246project.kindergartenprepapp;

import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
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

        _model = new NameTraceModel(this);

        // Set the content view layout
        setContentView(R.layout.name_activity_trace);

        _frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        // Set the _drawView
        _drawView = (DrawView) findViewById(R.id.nameTraceDrawView);

        // Build the background images from the traceCharacters
        setTraceBackgroundFromValues();

        playInstructions(getResources().getIdentifier(_model.getInstructionsFileName(), "raw", getPackageName()));

        // Hide the previous button since the activity is at the start
        FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
        previousButton.setVisibility(View.INVISIBLE);

        // Hide button if the activity isn't already complete
        FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
        doneButton.setVisibility(View.INVISIBLE);
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

        // Setup layout parameters of the bottomLayout
        LinearLayout linesLayout = (LinearLayout) findViewById(R.id.LayoutLines);
        FrameLayout.LayoutParams linesLayoutParams = new FrameLayout.LayoutParams(_layoutWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        linesLayout.setLayoutParams(linesLayoutParams);

        // Add each letter as an image view to the bottomLayout
        int linesResourceIndex = this.getResources().getIdentifier("lines", "drawable", this.getPackageName());
        List<String> values = _model.getValues();
        for (int i = 0; i < values.size(); i++) {
            // Set the imageView's image resource using value
            int resourceIndex = this.getResources().getIdentifier(values.get(i), "drawable", this.getPackageName());
            AppCompatImageView imageView = new AppCompatImageView(this);
            imageView.setImageResource(resourceIndex);
            imageView.setAlpha(0.5f);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(_imageViewWidthHeight, _imageViewWidthHeight);
            bottomLayout.addView(imageView, layoutParams);

            AppCompatImageView linesImageView = new AppCompatImageView(this);
            linesImageView.setImageResource(linesResourceIndex);
            LinearLayout.LayoutParams backgroundlayoutParams = new LinearLayout.LayoutParams(_imageViewWidthHeight, _imageViewWidthHeight);
            linesLayout.addView(linesImageView, backgroundlayoutParams);
        }
    }

    /**
     * Scrolls/Skips to the next value in the name.
     * @param view The view that activated the function (e.g. button)
     */
    public void goToNextValue(View view) {
        int x = _frameLayout.getScrollX() + _imageViewWidthHeight;
        if (x < (_layoutWidth - _imageViewWidthHeight)) {
            _frameLayout.scrollTo(x, 0);
        }

        FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
        if (previousButton.getVisibility() != View.VISIBLE) {
            previousButton.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
            previousButton.startAnimation(fadeIn);
        }

        if (x >= (_layoutWidth - (_imageViewWidthHeight * 2))) {
            FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
            nextButton.setVisibility(View.INVISIBLE);

            FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
            doneButton.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
            doneButton.startAnimation(fadeIn);
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

        FloatingActionButton nextButton = (FloatingActionButton) findViewById(R.id.btnNext);
        if (nextButton.getVisibility() != View.VISIBLE) {
            nextButton.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
            nextButton.startAnimation(fadeIn);
        }

        if (x <= 0) {
            FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
            previousButton.setVisibility(View.INVISIBLE);
        }

        FloatingActionButton doneButton = (FloatingActionButton) findViewById(R.id.btnDone);
        doneButton.setVisibility(View.INVISIBLE);
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

