package com.example.cs246project.kindergartenprepapp;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * An Activity that allows the user to trace letters in the alphabet, numbers, or words (including
 * their name). This is meant to help them improve their fine motor, writing, and recognition
 * skills.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

abstract public class TraceActivity extends AppCompatActivity {

    // The view control that allows the user to trace on a transparent canvas
    private DrawView _drawView;

    // The layout that has the transparent DrawView and sits on top of the background (bottom).
    private RelativeLayout _topLayout;

    // The layout that holds the background trace character images that sits underneath the top layout.
    private RelativeLayout _bottomLayout;

    // The buttons used for scrolling left and right.
    private Button _buttonLeft;
    private Button _buttonRight;

    // The characters that the user will be tracing (in the background)
    protected String _traceCharacters;

    // The layout index for the .xml file to use.
    protected int _layoutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the layoutIndex
        initializeLayoutIndex();

        // Set the content view using the _layoutIndex.
        setContentView(_layoutIndex);

        // Initialize the controls/layouts
        _buttonLeft = (Button) findViewById(R.id.btnScrollLeft);
        _buttonRight = (Button) findViewById(R.id.btnScrollRight);
        _topLayout = (RelativeLayout) findViewById(R.id.RelativeLayoutTop);
        _bottomLayout = (RelativeLayout) findViewById(R.id.RelativeLayoutBottom );

        // Get the traceCharacters
        initializeTraceCharacters();

        // Build the background images from the traceCharacters
        for (int i = 0; i < _traceCharacters.length(); ++i) {
            ImageView imageView = new ImageView(this);
            switch (_traceCharacters.charAt(i)) {
                case 'a' : {
                    imageView.setImageResource(R.drawable.upper_a_and_lower_a_300dpi);
                    break;
                }
                // TODO: Add more cases for b-z, A-Z, and 1-10.
                default: {
                    imageView.setImageResource(R.drawable.upper_a_and_lower_a_300dpi);
                    break;
                }
            }
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(500, 500);
            layoutParams.setMarginStart(i * 300);
            _bottomLayout.addView(imageView, layoutParams);
        }

        // Build the transparent draw view that sits on top of the background layer
        int widthOfCombinedCharacters = 5000;
        int heightOfCharacters = 500;
        RelativeLayout.LayoutParams drawViewLayoutParams = new RelativeLayout.LayoutParams(widthOfCombinedCharacters, heightOfCharacters);
        drawViewLayoutParams.setMarginStart(0);
        _drawView = new DrawView(this);
        _topLayout.addView(_drawView, drawViewLayoutParams);

        // Setup the left/right scroll button touch actions
        setOnTouchListenerForScrollButton(_buttonLeft, -10);
        setOnTouchListenerForScrollButton(_buttonRight, 10);
    }

    /**
     * Initialize Trace Characters
     * Initializes or sets the _traceCharacters.
     */
    abstract public void initializeTraceCharacters();

    /**
     * Initialize Layout Index
     * Initializes or sets the _layoutIndex.
     */
    abstract public void initializeLayoutIndex();

    /**
     * Set On Touch Listener For Button
     * Sets up the on touch scroll actions for a button and scrolls that button using the
     * directionValue (if negative it scrolls to the left, if positive to the right).
     * @param button the scroll button to setup on touch actions for
     * @param directionValue the direction to scroll (left/right)
     */
    public void setOnTouchListenerForScrollButton(Button button, final int directionValue) {
        // Setup a new touch listener
        button.setOnTouchListener(new Button.OnTouchListener() {

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
}
