package com.example.cs246project.kindergartenprepapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * An Activity that allows the user to trace letters in the alphabet, numbers, or words (including
 * their name). This is meant to help them improve their fine motor, writing, and recognition
 * skills.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class TraceActivity extends AppCompatActivity implements MenuReturnable {

    protected DrawView _drawView;
    protected Button _clearButton;
    protected Button _scrollLeftButton;
    protected Button _scrollRightButton;
    protected ArrayList<String> _value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace);
    }

    /**
     * Clear Draw View
     * Clears all the tracing marks on the _drawView
     * @param view (button) that activated this method.
     */
    public void clearDrawView(View view) {

    }

    /**
     * Scroll Left
     * Scroll the _drawView to the left to show more letters that are hiding off the right-hand
     * side of the screen.
     * @param view (button) that activated this method.
     */
    public void scrollLeft(View view) {

    }

    /**
     * Scroll Right
     * Scroll the _drawView to the right to show more letters that are hiding off the left-hand
     * side of the screen.
     * @param view (button) that activated this method.
     */
    public void scrollRight(View view) {

    }

    /**
     * Setup Background
     * Sets the background of the _drawView with the _values
     * Example: If tracing your name, the _values will be the list of characters in your name, and
     * will act as the background to trace against.
     */
    private void setupBackground() {

    }

    /**
     * Return To Main Menu
     * Returns to the MainActivity
     * @param view (button) that activated this method.
     */
    public void returnToMainMenu(View view) {

    }
}
