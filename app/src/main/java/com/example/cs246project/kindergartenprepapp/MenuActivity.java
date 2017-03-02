package com.example.cs246project.kindergartenprepapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The Menu screen/activity where the user can choose a tracing or selection "activity" to help
 * them prepare for Kindergarten by enhancing their reading, writing, and comprehension skills.
 * <p>
 * @author  Micheal Lucero, Trevor Adams, & Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    /**
     * Send To Word Selectable
     * Sends the user to the Word/Letter selection activity.
     * @param view the button that called this action.
     */
    public void sendToWordSelectable(View view) {
        Intent intent = new Intent(getBaseContext(), WordSelectable.class);
        startActivity(intent);
    }

    /**
     * Send To Name Selectable
     * Sends the user to the Name selection activity.
     * @param view the button that called this action.
     */
    public void sendToNameSelectable(View view) {
        Intent intent = new Intent(getBaseContext(), NameSelectable.class);
        startActivity(intent);
    }

    /**
     * Send To Count Selectable
     * Sends the user to the Count selection activity.
     * @param view the button that called this action.
     */
    public void sendToCountSelectable(View view) {
        Intent intent = new Intent(getBaseContext(), CountSelectable.class);
        startActivity(intent);
    }

    /**
     * Send to Trace Activity
     * Sends the user to the Name tracing activity.
     * @param view the button that called this action.
     */
    public void sendToNameTraceActivity(View view) {
        Intent intent = new Intent(getBaseContext(), NameTraceActivity.class);
        startActivity(intent);
    }

    /**
     * Send to Trace Activity
     * Sends the user to the Name tracing activity.
     * @param view the button that called this action.
     */
    public void sendToLetterTraceActivity(View view) {
        Intent intent = new Intent(getBaseContext(), LetterTraceActivity.class);
        startActivity(intent);
    }

    /**
     * Send to Trace Activity
     * Sends the user to the Name tracing activity.
     * @param view the button that called this action.
     */
    public void sendToNumberTraceActivity(View view) {
        Intent intent = new Intent(getBaseContext(), LetterTraceActivity.class);
        startActivity(intent);
    }
}
