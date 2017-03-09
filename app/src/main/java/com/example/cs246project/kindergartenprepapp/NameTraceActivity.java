package com.example.cs246project.kindergartenprepapp;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
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

public class NameTraceActivity extends AppCompatActivity {

    // Model object for managing the name character values
    private NameTraceModel _model;

    // The view control that allows the user to trace on a transparent canvas
    private DrawView _drawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(AppConstants.sharePreferenceSettings, MODE_PRIVATE);
        _model = new NameTraceModel(settings.getString(AppConstants.sharePreferenceName, ""));

        // Set the content view using the _layoutIndex.
        setContentView(R.layout.name_activity_trace);

        // Initialize the controls/layouts
        int widthOfCombinedCharacters = _model.getNumberOfCharacters() * 1000;

        ((FrameLayout) findViewById(R.id.LayoutTop)).setLayoutParams(new FrameLayout.LayoutParams(widthOfCombinedCharacters, ViewGroup.LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.LayoutBottom)).setLayoutParams(new FrameLayout.LayoutParams(widthOfCombinedCharacters, ViewGroup.LayoutParams.MATCH_PARENT));

        // Build the background images from the traceCharacters
        setTraceBackgroundFromValues(_model.getValues());

        // Build the transparent draw view that sits on top of the background layer
        FrameLayout.LayoutParams drawViewLayoutParams = new FrameLayout.LayoutParams(widthOfCombinedCharacters, ViewGroup.LayoutParams.MATCH_PARENT);
        drawViewLayoutParams.setMarginStart(0);
        drawViewLayoutParams.gravity = Gravity.CENTER;
        _drawView = new DrawView(this);
        ((FrameLayout) findViewById(R.id.LayoutTop)).addView(_drawView, drawViewLayoutParams);

        // Setup the left/right scroll button touch actions
        setOnTouchListenerForScrollButton((FloatingActionButton) findViewById(R.id.btnScrollLeft), -15);
        setOnTouchListenerForScrollButton((FloatingActionButton) findViewById(R.id.btnScrollRight), 15);
    }

    /**
     * Set Trace Background From Values
     * Sets the background trace images using a list of string values (file names).
     * @param values used as the background to trace over
     */
    private void setTraceBackgroundFromValues(List<String> values) {
        FrameLayout bottomLayout = (FrameLayout) findViewById(R.id.LayoutBottom);
        for (int i = 0; i < values.size(); i++) {
            // Set the imageView's image resource using value
            int resourceIndex = this.getResources().getIdentifier(values.get(i), "drawable", this.getPackageName());
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(resourceIndex);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(800, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMarginStart(i * 200);
            bottomLayout.addView(imageView, layoutParams);
        }
    }

    /**
     * Set On Touch Listener For Button
     * Sets up the on touch scroll actions for a button and scrolls that button using the
     * directionValue (if negative it scrolls to the left, if positive to the right).
     * @param button the scroll button to setup on touch actions for
     * @param directionValue the direction to scroll (left/right)
     */
    public void setOnTouchListenerForScrollButton(FloatingActionButton button, final int directionValue) {
        // Setup a new touch listener
        button.setOnTouchListener(new FloatingActionButton.OnTouchListener() {

            // The handler will use callbacks to mimic "holding" the button down
            private Handler _handler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // If the user presses the button and doesn't release, then add the
                        // _scrollAction to the _handler which will call run() over and over to
                        // mimic "holding" the button down.
                        if (_handler != null) return true;
                        _handler = new Handler();
                        _handler.postDelayed(_scrollAction, 50);
                        break;
                    case MotionEvent.ACTION_UP:
                        // If the user releases the button, then remove the _scrollAction from the
                        // _handler callbacks which will stop it from scrolling.
                        if (_handler == null) return true;
                        _handler.removeCallbacks(_scrollAction);
                        _handler = null;
                        break;
                }
                return false;
            }

            // The _scrollAction that scrolls to the left or right depending on the directionValue.
            Runnable _scrollAction = new Runnable() {
                @Override public void run() {
                    FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
                    frameLayout.scrollTo(frameLayout.getScrollX() + directionValue, 0);
                    _handler.postDelayed(this, 50);
                }
            };
        });
    }

    /**
     * Clear Draw View
     * Clears all the tracing from the _drawView.
     * @param view the button that caused this action to be called
     */
    public void clearDrawView(View view) {
        _drawView.clearView();
    }

    public void onDoneButtonClick(View view) {
        // Display the snapshot popup
        finish();
    }
}

