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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_selectable);
        layout_name = (LinearLayout) findViewById(R.id.layout_name);
        layout_top_name = (LinearLayout) findViewById(R.id.layout_top_name);
        _model = new NameSelectableModel(this, "first");

        SharedPreferences settings = this.getSharedPreferences(AppConstants.sharePreferenceSettings, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        if (_model.hasFirstName()) {
            playInstructions(_model.getActivityInstructionsIndex());
            viewSetUp();
            // Update shared preferences
            editor.putString(AppConstants.sharePreferenceUpdatingName, "false");
        } else {
            // Update shared preferences
            editor.putString(AppConstants.sharePreferenceUpdatingName, "true");
            editor.commit();

            // Play toast of missing your name
            CharSequence text = "Missing your name";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(25);
            View view = toast.getView();
            view.setBackgroundColor(Color.parseColor("#9575cd"));
            view.setPadding(20, 10, 20, 10);
            toast.show();

            // Redirect to main activity for entry of name
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        }

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
            enableDisableButtons(true);

            // checking for the correct value dynamically as button moves along
            _isCorrect = _model.isCorrectOrder((Character) mediaButton.getValue());
            if(_isCorrect) {
                // Show answer toast
                _model.displayToast(true);

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
                _model.displayToast(false);
            }
        }
    }

    /**
     * Will disable or enable the layout buttons
     * */
    private void enableDisableButtons(Boolean state){
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
            MediaPlayer mediaPlayer = MediaPlayer.create(this, audioAnswerIndex);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
            mediaPlayer.start();

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
            MediaPlayer mediaPlayer = MediaPlayer.create(this, audioAnswerIndex);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mp != null) {
                        mp.release();
                    }
                    enableDisableButtons(false);
                }
            });
            mediaPlayer.start();
        }
    }

    public void returnToMenu(View view) {
        this.finish();
    }

}
