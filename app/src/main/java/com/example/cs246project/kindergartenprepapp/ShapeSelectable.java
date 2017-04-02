package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Handles finding the shape that matches the word referenced in the button shown
 * @author Trevor Adams
 * edits Michael Lucero
 * */
public class ShapeSelectable extends SelectableActivity implements View.OnTouchListener, MediaButtonHandler {

    // Create a new Array list that will hold the filenames to reference
    private ShapeSelectableModel _model;

    // Find the horizontal scroll view
    private LinearLayout _shape_layout;
    private ProgressBar _progBar;
    private boolean wasTrue = false;
    private boolean isFirstTime = true;
    Context context = this;
    private Button _shapeButton;
    private MediaPlayer _mainButtonMediaPlayer;

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up main activity view in 2 containers
        setContentView(R.layout.shape_selectable);
        _shape_layout = (LinearLayout) findViewById(R.id.shape_layout);
        _progBar = (ProgressBar) findViewById(R.id.progressBar2);
        _progBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(AppConstants.progressBarColor)));


        // set up model for 4 question buttons and random choices
        _model = new ShapeSelectableModel(this, 4);

        // set main media button
        _shapeButton = (Button) findViewById(R.id.shapeButton);

        viewSetUp();
        setAnswerButtonValue();

        // Instruction audio is now playing so disable main image button and the question buttons
        //    The buttons will be re-enabled after playinstructions is complete, then the override
        //    of onInstruction complete will re-enable the buttons.
        enableMainImageButton(false);
        disableQuestionButtons(true);

        // During play instructions will show toast for the duration of the instructions
        //    and then stop when audio is over
        playInstructions(_model.getActivityInstructionsIndex());
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
            ((ViewGroup.MarginLayoutParams) btn.getLayoutParams()).setMargins(10, 5, 30, 5);
            btn.setBackgroundColor(Color.TRANSPARENT);

            _shape_layout.addView(btn);
        }
    }

    @Override
    public void onMediaButtonTouched(MediaButton mediaButton, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            // Disable the buttons first thing after touch
            disableQuestionButtons(true);
            enableMainImageButton(false);

            if(_model.isCorrect(mediaButton.getValue())) {
                wasTrue = true;
                _progBar.incrementProgressBy(1);
                // Show answer toast
                displayToast(true);
            } else {
                // Show answer toast
                displayToast(false);
            }
        }
    }

    private void enableMainImageButton(Boolean state){
        _shapeButton.setEnabled(state);
    }

    /**
     * Will disable or enable the layout buttons
     * */
    private void disableQuestionButtons(Boolean state){
        for (int i = 0; i < _shape_layout.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) _shape_layout.getChildAt(i);
            mediaButton.setIsDisabled(state);
        }
    }

    private void playMainImageSound() {
        String answer = "shape_" + _model.getAnswer();
        int soundId = getResources().getIdentifier(answer, "raw", getPackageName()); // todo need to get correct audio resource
        _mainButtonMediaPlayer = MediaPlayer.create(this, soundId);

        // Release main image audio after it is no longer playing
        _mainButtonMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
                mp = null;
                enableMainImageButton(true);
                disableQuestionButtons(false);
                _mainButtonMediaPlayer = null;
            }
        });

        disableQuestionButtons(true);


        // Play the audio
        _mainButtonMediaPlayer.start();
    }

    private void setAnswerButtonValue() {
        // Grab the text of answer
        String btnName = _model.getAnswer();
        // Capitalize first character
        btnName = Character.toUpperCase(btnName.charAt(0)) + btnName.substring(1);
        // Set button
        _shapeButton.setText(btnName);

        if (!isFirstTime) {
            playMainImageSound();
        }

        /**
         * Setup event listener for main image
         * */
        _shapeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    enableMainImageButton(false);
                    playMainImageSound();
                }
                return false;
            }
        });
        // Enable the buttons
        disableQuestionButtons(false);
    }

    /**
     * Called when a round is over, to prepare views for next round
     * */
    private void resetActivity() {
        _shape_layout.removeAllViews();
        wasTrue = false;
        count = 1;
        viewSetUp();
        setAnswerButtonValue();

        // disable audio
        enableMainImageButton(false);
        disableQuestionButtons(true);
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
        if (_model._isActivityDone) {
            this.finish();
        } else {

            // stop the correct/incorrect toast when done
            if (_toast != null) {
                _toast.cancel();
            }

            // Enable the buttons when sound is complete
            disableQuestionButtons(false);
            enableMainImageButton(true);
            // check for true
            if (wasTrue)
                resetActivity();
        }
    }

    /**
     * Once instruction audio is over then re-enable the the question buttons. Note the re-enable
     * will happen in the playMainImageSound() on complete listener as this is the last sound
     * that will be played before activity is ready to work.
     */
    @Override
    public void onInstructionsAudioComplete() {

        // stop the instructions toast when done
        if (_toast != null) {
            _toast.cancel();
        }

        playMainImageSound();
        isFirstTime = false;
    }

    public void returnToMenu(View view) {
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

        for (int i = 0; i < _shape_layout.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) _shape_layout.getChildAt(i);
            mediaButton.stopAudio();
        }

        if (_mainButtonMediaPlayer != null && _mainButtonMediaPlayer.isPlaying()) {
            _mainButtonMediaPlayer.stop();
            _mainButtonMediaPlayer.release();
            _mainButtonMediaPlayer = null;
        }
        super.stopAudio();
    }

    /**
     * When activity resumes make sure the the image button and question buttons are disabled.
     */
    @Override
    public void startAudio() {

        enableMainImageButton(false);
        disableQuestionButtons(true);
        playInstructions(_instructionsAudioResourceIndex);

        _backgroundAudioModel.startBackgroundAudio(this);
    }
}
