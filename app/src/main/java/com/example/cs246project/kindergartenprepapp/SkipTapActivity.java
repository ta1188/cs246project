package com.example.cs246project.kindergartenprepapp;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The super Activity of all SkipTap activities which has common member variables and member
 * functions.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-15
 */

abstract class SkipTapActivity extends AppCompatActivity {

    protected BackgroundAudioModel _backgroundAudioModel;
    protected MediaPlayer _instructionsMediaPlayer;
    protected int _instructionsAudioResourceIndex;
    protected Toast _toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        _backgroundAudioModel = new BackgroundAudioModel(this);
    }

    /**
     * Plays the instructions audio for a given index.
     * @param instructionsAudioResourceIndex Audio resource index of the instruction to be played.
     */
    protected void playInstructions(int instructionsAudioResourceIndex) {

        displayInstructionToast();

        // keep track for onResume
        _instructionsAudioResourceIndex = instructionsAudioResourceIndex;

        if (_instructionsMediaPlayer != null) {
            _instructionsMediaPlayer.release();
            _instructionsMediaPlayer = null;
        }

        // Initialize a media player with the audio resource
        if (_instructionsAudioResourceIndex > 0) {
            _instructionsMediaPlayer = MediaPlayer.create(this, instructionsAudioResourceIndex);
        }

        _instructionsMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                _instructionsMediaPlayer.release();
                _instructionsMediaPlayer = null;

                // got to specific things that need to be stopped in the child. Note the
                //    toast is canceled in this function found in the child to show the
                //    logical flow where toast is removed and buttons are re-enabled
                onInstructionsAudioComplete();
            }
        });

        // Play the first audio track
        _instructionsMediaPlayer.start();
    }

    /**
     * Handle standard toast message where it will display for the length of the correct/incorrect
     * audio.
     * */
    public void displayToast(boolean correctAnswer) {
        CharSequence text;
        String toastColor;

        if (correctAnswer) {
            text = "Correct!";
            toastColor = "#00e676";
        } else {
            text = "Incorrect!";
            toastColor = "#ff8a65";
        }

        int duration = Toast.LENGTH_LONG;

        _toast = Toast.makeText(this, text, duration);
        _toast.setGravity(Gravity.CENTER, 0, 0);
        ViewGroup group = (ViewGroup) _toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        View view = _toast.getView();
        view.setBackgroundColor(Color.parseColor(toastColor));
        view.setPadding(20, 10, 20, 10);

        // Show the toast
        _toast.show();
    }

    /**
     * Handle instruction toast messages that will play for the length of the beginning instructions
     * */
    public void displayInstructionToast() {
        String color = "#ffbb33";

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);


        final AppCompatImageView imageView = new AppCompatImageView(this);
        imageView.setImageResource(R.drawable.touch_icon);
        imageView.setBackgroundColor(Color.parseColor(color));
        imageView.setPadding(50, 50, 50, 50);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(250, 250);
        imageView.setLayoutParams(layoutParams);
        layout.addView(imageView);
        final ScaleAnimation growAnim = new ScaleAnimation(1.0f, 1.15f, 1.0f, 1.15f);
        final ScaleAnimation shrinkAnim = new ScaleAnimation(1.15f, 1.0f, 1.15f, 1.0f);

        growAnim.setDuration(600);
        shrinkAnim.setDuration(600);

        imageView.setAnimation(growAnim);
        growAnim.start();

        growAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imageView.setAnimation(shrinkAnim);
                shrinkAnim.start();
            }
        });
        shrinkAnim.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation){}

            @Override
            public void onAnimationRepeat(Animation animation){}

            @Override
            public void onAnimationEnd(Animation animation)
            {
                imageView.setAnimation(growAnim);
                growAnim.start();
            }
        });

        if (_toast != null) {
            _toast.cancel();
        }
        _toast = new Toast(this);
        _toast.setView(layout);
        _toast.setDuration(Toast.LENGTH_SHORT);
        _toast.setGravity(Gravity.CENTER, 0, 0);
        _toast.show();
    }

    /**
     * Gets the text to put into the instructions toast.
     */
    abstract protected String getInstructionToastText();

    /**
     * Handle instruction toast messages that will play for the length of the beginning instructions
     * */
    public void displayMissingNameToast() {
        CharSequence text = "Missing your name";
        String toastColor = "#9575cd";

        int duration = Toast.LENGTH_SHORT;

        Toast missingNameToast = Toast.makeText(this, text, duration);
        missingNameToast.setGravity(Gravity.CENTER, 0, 0);
        ViewGroup group = (ViewGroup) missingNameToast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        View view = missingNameToast.getView();
        view.setBackgroundColor(Color.parseColor(toastColor));
        view.setPadding(20, 10, 20, 10);
        missingNameToast.show();
    }

    public void onInstructionsAudioComplete() {
        // Do nothing here.
        // Descendants may Override this if they want (not abstract == not required)
    }

    public void stopEverything() {
        if (_toast != null) {
            _toast.cancel();
        }
        // Do nothing here.
        // Descendants Override
        stopAudio();
    }

    /**
     * Will kill the media player
     */
    public void stopAudio() {
        if (_instructionsMediaPlayer != null) {
            _instructionsMediaPlayer.release();
            _instructionsMediaPlayer = null;
        }

        _backgroundAudioModel.stopBackgroundAudio();
    }

    /**
     * Will start audio for background music and instructions
     */
    public void startAudio() {

        playInstructions(_instructionsAudioResourceIndex);

        _backgroundAudioModel.startBackgroundAudio(this);
    }

    @Override
    protected void onStop() {
       stopEverything();

        super.onStop();
    }

    @Override
    protected void onPause() {
        stopEverything();

        super.onPause();
    }

    @Override
    protected void onResume() {

        // For hiding status the bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        if(!_backgroundAudioModel.isPlaying()) {
            startAudio();
        }

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

    @Override
    protected void onDestroy() {
        stopEverything();
        super.onDestroy();
    }
}


