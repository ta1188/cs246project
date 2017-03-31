package com.example.cs246project.kindergartenprepapp;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
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

public class NameTraceableActivity extends SkipTapActivity implements Runnable {

    // Model object for managing the name character values.
    private NameTraceableModel _model;

    // The view control that allows the user to trace on a transparent canvas.
    private DrawView _drawView;

    // The layout that contains the letters
    private LinearLayout _letterLayout;

    // The list of all of the widths of each character image view
    private List<Integer> _characterWidths;

    // The index of the character showing farthest left on the screen
    private int _currentCharacterIndex;

    // Total width of the traceable area (length of the character * _characterWidth).
    private int _totalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _model = new NameTraceableModel(this);

        // Set the content view layout
        setContentView(R.layout.name_traceable_activity);

        // Set the _drawView
        _drawView = (DrawView) findViewById(R.id.drawView);

        // Initialize variables dealing with width/scrolling
        _characterWidths = new ArrayList<>();
        _totalWidth = 0;
        _currentCharacterIndex = 0;

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
        _letterLayout = (LinearLayout) findViewById(R.id.letterLayout);
        LinearLayout previewLetterLayout = (LinearLayout) findViewById(R.id.previewLetterLayout);

        for (int i = 0; i < _model.getValues().size(); i++) {
            // Actual image view
            AppCompatImageView imageView = new AppCompatImageView(this);
            imageView.setImageResource(_model.getValues().get(i));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 75, 0);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setAlpha(0.3f);
//            imageView.setBackgroundColor(Color.BLUE);
            imageView.setAdjustViewBounds(true);
            _letterLayout.addView(imageView);

            // Preview
            AppCompatImageView previewImageView = new AppCompatImageView(this);
            previewImageView.setImageResource(_model.getValues().get(i));
            LinearLayout.LayoutParams previewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            previewLayoutParams.setMargins(0, 0, 10, 0);
            previewImageView.setLayoutParams(previewLayoutParams);
            previewImageView.setAlpha(0.3f);
            previewImageView.setAdjustViewBounds(true);
            previewLetterLayout.addView(previewImageView);

            _letterLayout.getLayoutParams().width = 5000;
            _drawView.getLayoutParams().width = 5000;
            // Post only on the last view added
            if ((i + 1) == _model.getValues().size()) {
                imageView.post(this);
            }
        }
    }

    /**
     * Run
     * Used after dynamically getting the runtime height of the _framelayout so the background images
     * and _drawView can be sized appropriately.
     */
    @Override
    public void run() {
        for (int i = 0; i < _letterLayout.getChildCount(); i++) {
            AppCompatImageView imageView = (AppCompatImageView) _letterLayout.getChildAt(i);
            int characterWidth = imageView.getWidth() + 75;
            _characterWidths.add(characterWidth);
            _totalWidth += characterWidth;
        }
    }

    private boolean canScrollRight() {
        int remainingScrollWidth = 0;
        for (int i = _currentCharacterIndex; i < _characterWidths.size(); i++) {
            remainingScrollWidth += _characterWidths.get(i);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        return remainingScrollWidth <= width;
    }

    /**
     * Scrolls/Skips to the next value in the name.
     * @param view The view that activated the function (e.g. button)
     */
    public void goToNextValue(View view) {
        int characterWidth = _characterWidths.get(_currentCharacterIndex);
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.drawViewContainer);
        LinearLayout letterLayout = (LinearLayout) findViewById(R.id.letterLayout);
        int x = layout.getScrollX() + characterWidth;
        if (x < (_totalWidth)) {
            layout.scrollTo(x, 0);
            letterLayout.scrollTo(x, 0);
            ++_currentCharacterIndex;
        }

        FloatingActionButton previousButton = (FloatingActionButton) findViewById(R.id.btnPrevious);
        if (previousButton.getVisibility() != View.VISIBLE) {
            previousButton.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);
            previousButton.startAnimation(fadeIn);
        }

        int nextCharacterWidth = 0;
        if (_currentCharacterIndex != _characterWidths.size()) {
            nextCharacterWidth = _characterWidths.get(_currentCharacterIndex);
        }

        if (canScrollRight()) {
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
        int characterWidth = 0;
        if (_currentCharacterIndex >= 1) {
            characterWidth = _characterWidths.get(_currentCharacterIndex - 1);
        }
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.drawViewContainer);
        LinearLayout letterLayout = (LinearLayout) findViewById(R.id.letterLayout);
        int x = layout.getScrollX() - characterWidth;
        if (x >= 0) {
            layout.scrollTo(x, 0);
            letterLayout.scrollTo(x, 0);
            --_currentCharacterIndex;
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

