package com.example.cs246project.kindergartenprepapp;

import android.os.Bundle;

/**
 * An Activity that allows the user to trace letters in the alphabet, numbers, or words (including
 * their name). This is meant to help them improve their fine motor, writing, and recognition
 * skills.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class LetterTraceActivity extends TraceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Initialize Trace Characters
     * Initializes or sets the traceCharacters
     */
    @Override
    public void initializeTraceCharacters() {
        _traceCharacters = "Aa";
    }

    /**
     * Initialize Layout Index
     * Initializes or sets the _layoutIndex.
     */
    @Override
    public void initializeLayoutIndex() {
        _layoutIndex = R.layout.character_activity_trace;
    }
}
