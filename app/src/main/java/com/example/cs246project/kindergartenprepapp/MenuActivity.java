package com.example.cs246project.kindergartenprepapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

/**
 * The Menu screen/activity where the user can choose a tracing or selection "activity" to help
 * them prepare for Kindergarten by enhancing their reading, writing, and comprehension skills.
 * <p>
 * @author  Micheal Lucero, Trevor Adams, & Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class MenuActivity extends AppCompatActivity {

    protected ViewFlipper flipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_menu);
        flipper = (ViewFlipper) findViewById(R.id.flipper);
        final FloatingActionButton nextMenuBtn = (FloatingActionButton) findViewById(R.id.menuNext);
        final FloatingActionButton prevMenuBtn = (FloatingActionButton) findViewById(R.id.menuPrev);
        prevMenuBtn.setVisibility(View.GONE);

        nextMenuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flipper.setInAnimation(inFromRightAnimation());
                flipper.setOutAnimation(outToLeftAnimation());
                flipper.showNext();
                prevMenuBtn.setVisibility(View.VISIBLE);
                nextMenuBtn.setVisibility(View.GONE);
                Animation fadeIn = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.fade_in_animation);
                prevMenuBtn.startAnimation(fadeIn);
            }
        });

        prevMenuBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                flipper.setInAnimation(inFromLeftAnimation());
                flipper.setOutAnimation(outToRightAnimation());
                flipper.showPrevious();
                nextMenuBtn.setVisibility(View.VISIBLE);
                prevMenuBtn.setVisibility(View.GONE);
                Animation fadeIn = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.fade_in_animation);
                nextMenuBtn.startAnimation(fadeIn);
            }
        });

        // retrieve from shared preferences
        final SharedPreferences settings = this.getSharedPreferences(AppConstants.sharePreferenceSettings, MODE_PRIVATE);
        Boolean musicPlayable = settings.getBoolean("IS_MUSIC_PLAYABLE", true);

        // apply current shared setting for music playable
        ToggleButton musicPlayableButton = (ToggleButton) findViewById(R.id.musicOnOff);
        musicPlayableButton.setChecked(musicPlayable);

        // set listen to detect change and updated preferences
        ToggleButton toggle = (ToggleButton) findViewById(R.id.musicOnOff);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = settings.edit();
                if (isChecked) {
                    // The music is playable with toggle checked
                    editor.putBoolean("IS_MUSIC_PLAYABLE", true);

                } else {
                    // The music is playable with toggle not checked
                    editor.putBoolean("IS_MUSIC_PLAYABLE", false);
                }
                editor.commit();
            }
        });
    }

    /**
     * Animation handling
     * */
    private Animation inFromRightAnimation() {
        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
        inFromRight.setDuration(500);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }
    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
        outtoLeft.setDuration(500);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
        inFromLeft.setDuration(500);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }
    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  +1.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
        outtoRight.setDuration(500);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
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
        Intent intent = new Intent(getBaseContext(), NumberTraceActivity.class);
        startActivity(intent);
    }

    public void sendToAboutActivity (View view) {
        Intent intent = new Intent(getBaseContext(), AboutActivity.class);
        startActivity(intent);
    }

    public void progressMenu (View view) {
        Log.d("MenuActivity", "This will progress the menu");
    }

    @Override
    protected void onResume() {

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onResume();
    }

    @Override
    protected void onRestart() {

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        super.onRestart();
    }
}
