package com.example.cs246project.kindergartenprepapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

/**
 * Handles selecting numbers of the user's name
 * @author Trevor Adams
 * edits Michael Lucero
 * */
public class NameSelectable extends SkipTapActivity implements View.OnTouchListener, MediaButtonHandler {

    // Create a new Array list that will hold the filenames to reference
    private NameSelectableModel _model;

    // Find the horizontal scroll view
    private LinearLayout layout_name;
    private LinearLayout layout_top_name;

    private int position = 0;
    private int count = 0;
    private boolean completedFirstName = false;
    private Boolean _isCorrect;

    private MediaPlayer _soundsOfNameMediaPlayer;
    private MediaPlayer _answerMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_selectable);
        layout_name = (LinearLayout) findViewById(R.id.layout_name);
        layout_top_name = (LinearLayout) findViewById(R.id.layout_top_name);
        _model = new NameSelectableModel(this, "first");

        SharedPreferences settings = this.getSharedPreferences(AppConstants.sharePreferenceSettings, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();


        viewSetUp();
        // Disable the buttons
        disableQuestionButtons(true);

        // display toast and show for the duration of the instructions
        displayInstructionToast();
        playInstructions(_model.getActivityInstructionsIndex());
    }

    protected void resetToLastName() {
        _model = new NameSelectableModel(this, "last");
        layout_name.removeAllViews();
        layout_top_name.removeAllViews();

        position = 0;
        count = 0;
        viewSetUp();
    }

    public void viewSetUp() {
        /**
         * This will loop through the generatedValueList based on the length of the array
         * It will then get the index of each file in the drawable directory,
         * then it will update the image for each button.
         * */
        for (MediaModel item : _model.generateValueList()) {
            final MediaButton btn = new MediaButton(this, item, this);
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            btn.setScaleType(ImageView.ScaleType.CENTER);
            btn.setAdjustViewBounds(true);
            btn.setPadding(5, 5, 5, 5);
            btn.setElevation(10);
            ((ViewGroup.MarginLayoutParams) btn.getLayoutParams()).setMargins(10, 5, 10, 5);
            btn.setBackgroundColor(Color.parseColor(_model.getBtnColor()));

            layout_name.addView(btn);

            // Add letter options on Top
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setPadding(6, 0, 6, 0);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setAdjustViewBounds(true);
            imageView.setImageResource(R.drawable.underline);
            imageView.setId(count);

            if (count == 0) {
                imageView.setImageResource(R.drawable.underline);
                Drawable res = this.getResources().getDrawable(R.drawable.button_border, getTheme());
                imageView.setBackground(res);
            }
            count ++;
            layout_top_name.addView(imageView);
        }
    }

    @Override
    public void onMediaButtonTouched(MediaButton mediaButton, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Disable the buttons
            disableQuestionButtons(true);

            // checking for the correct value dynamically as button moves along
            _isCorrect = _model.isCorrectOrder((Character) mediaButton.getValue());
            if(_isCorrect) {
                // Show answer toast
                displayToast(true);

                // Remove correct button selection from view
                mediaButton.setVisibility(View.GONE);

                ImageView letterTopLine = (ImageView) findViewById(position);
                letterTopLine.setBackgroundResource(0);

                // Progress name position indicator
                if (position < (count - 1)) {
                    ImageView nextLetterSpot = (ImageView) findViewById(position + 1);
                    Drawable drawRes = getResources().getDrawable(R.drawable.button_border, getTheme());
                    nextLetterSpot.setImageDrawable(drawRes);

                    Animation fadeIn = AnimationUtils.loadAnimation(NameSelectable.this, R.anim.fade_in_animation);
                    nextLetterSpot.startAnimation(fadeIn);

                }
                // Get the letter index for the letter of the name selected
                int resourceIndex;
                if (position == 0) {
                    resourceIndex = getResources().getIdentifier("upper_" + mediaButton.getValue().toString().toLowerCase(), "drawable", getPackageName());
                } else {
                    resourceIndex = getResources().getIdentifier("lower_" + mediaButton.getValue().toString().toLowerCase(), "drawable", getPackageName());
                }

                letterTopLine.setImageResource(resourceIndex);

                position++;
            } else {
                // Show answer toast
                displayToast(false);
            }
        }
    }

    /**
     * Will disable or enable the layout buttons
     * */
    private void disableQuestionButtons(Boolean state){
        for (int i = 0; i < layout_name.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) layout_name.getChildAt(i);
            mediaButton.setIsDisabled(state);
        }
    }

    private void playSoundsOfName(boolean isFirstName, int transitionType) {
        SharedPreferences _sharedPreferences = this.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        String name = isFirstName ? _sharedPreferences.getString("FIRST_NAME", "") : _sharedPreferences.getString("LAST_NAME", "");

        for (int i = 0; i < name.length(); i++) {
            int audioAnswerIndex = this.getResources().getIdentifier(String.valueOf(Character.toLowerCase(name.charAt(i))), "raw", this.getPackageName());
            _soundsOfNameMediaPlayer = MediaPlayer.create(this, audioAnswerIndex);
            _soundsOfNameMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    mp = null;
                }
            });
            _soundsOfNameMediaPlayer.start();

            // Delay a little for audio to complete
            try {
                TimeUnit.MILLISECONDS.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Screen update logic for post sound completion...
        switch (transitionType) {
            case 0:
                resetToLastName();
                break;
            case 1:
                this.finish();
                break;
            case 2:
                this.finish();
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    /**
     * Called when audio has completed (for media buttons only)
     * */
    @Override
    public void onAudioComplete() {

        if (_model._isActivityDone && _model.hasLastName() && !completedFirstName) {
            // Checking for if it's the first name AND the activity is done W/ a last name
            playSoundsOfName(true, 0);
            completedFirstName = true;
        } else if (_model._isActivityDone && !_model.hasLastName()) {
            // Checking for if it's the first name AND the activity is done W/O a last name
            playSoundsOfName(true, 1);
            this.finish();
        } else if (_model._isActivityDone && _model.hasLastName() && completedFirstName) {
            // Checking for if it's the last name AND the activity is done
            playSoundsOfName(false, 1);

            this.finish();
        } else {
            // Enable the buttons when sound is complete
            int audioAnswerIndex = _model.getAnswerAudioIndex(_isCorrect);
             _answerMediaPlayer = MediaPlayer.create(this, audioAnswerIndex);
            _answerMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // cancel toast after audio
                    if (_toast != null) {
                        _toast.cancel();
                    }

                    if (mp != null) {
                        mp.release();
                        mp = null;
                    }
                    disableQuestionButtons(false);
                }
            });
            _answerMediaPlayer.start();
        }
    }

    @Override
    public void onInstructionsAudioComplete() {

        // stop the instructions toast when done
        if (_toast != null) {
            _toast.cancel();
        }

        // Enable the buttons when sound is complete
        disableQuestionButtons(false);
    }

    public void returnToMenu(View view) {
        if (_toast != null) {
            _toast.cancel();
        }

        this.finish();
    }

    /**
     * Overrides from skiptap activity and calls functions to handle all closing activities like
     * stopping audio for media players and existing toasts
     */
    @Override
    public void stopEverything() {

        // cancel any toasts that are still showing; done in override
        // leaving app
        if (_toast != null) {
            _toast.cancel();
        }
        // stop all audio that is playing
        stopAudio();
    }

    /**
     * Handles stopping audio the buttons might be playing when transitioning to other activities
     * or moving to another app.
     */
    @Override
    public void stopAudio() {

        // name button audio release
        for (int i = 0; i < layout_name.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) layout_name.getChildAt(i);
            mediaButton.stopAudio();
        }

        // if leaving activity release audio for saying name back to you
        if(_soundsOfNameMediaPlayer != null ) {
            _soundsOfNameMediaPlayer.release();
            _soundsOfNameMediaPlayer = null;
        }

        // release audio for answer
        if(_answerMediaPlayer != null ) {
            _answerMediaPlayer.release();
            _answerMediaPlayer = null;
        }

        super.stopAudio();
    }

    /**
     * When activity resumes make sure the the image button and question buttons are disabled.
     */
    @Override
    public void startAudio() {

        disableQuestionButtons(true);

        playInstructions(_instructionsAudioResourceIndex);

        displayInstructionToast();

        _backgroundAudioModel.startBackgroundAudio(this);
    }

}
