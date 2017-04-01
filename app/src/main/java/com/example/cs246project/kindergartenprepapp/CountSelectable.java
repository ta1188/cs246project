package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * @author  Trevor Adams
 * edits Michael Lucero
 * Handles finding how many objects are shown
 * */
public class CountSelectable extends SelectableActivity implements View.OnTouchListener, MediaButtonHandler {


    // Create a new Array list that will hold the filenames to reference
    private CountSelectableModel _model;

    // Find the horizontal scroll view
    private LinearLayout layout_top;
    private LinearLayout layout_bottom;
    private ProgressBar _progBar;
    private boolean wasTrue = false;
    private boolean isFirstTime = true;
    Context context = this;
    private ImageView imageView;

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up main activity view in 2 containers
        setContentView(R.layout.count_selectable);
        layout_top = (LinearLayout) findViewById(R.id.layout_count_top);
        layout_bottom = (LinearLayout) findViewById(R.id.layout_count_bottom);
        _progBar = (ProgressBar) findViewById(R.id.progressBarCount);
        _progBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor(AppConstants.progressBarColor)));


        // set up model for 4 question buttons and random choices
        _model = new CountSelectableModel(this, 4);

        // set main media button
        imageView = (ImageView) findViewById(R.id.countImage);

        viewSetUp();
        setMainImage();

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
            btn.setBackgroundColor(Color.parseColor(_model.getBtnColor()));

            if (count <= 2) {
                layout_top.addView(btn);
            } else {
                layout_bottom.addView(btn);
            }
            count++;
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
        imageView.setEnabled(state);
    }

    /**
     * Will disable or enable the layout buttons
     * */
    private void disableQuestionButtons(Boolean state){
        for (int i = 0; i < layout_top.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) layout_top.getChildAt(i);
            mediaButton.setIsDisabled(state);
        }
        for (int i = 0; i < layout_bottom.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) layout_bottom.getChildAt(i);
            mediaButton.setIsDisabled(state);
        }
    }

    private void setMainImage() {
        // Grab the image resource and set the image drawable
        Drawable res = getResources().getDrawable(_model.getAnswerResourceIndex(), getTheme());
        imageView.setImageDrawable(res);
    }

    private void resetActivity() {
        layout_top.removeAllViews();
        layout_bottom.removeAllViews();
        wasTrue = false;
        count = 1;
        viewSetUp();
        setMainImage();
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
     * Once instruction audio is over then re-enable the the question buttons
     */
    @Override
    public void onInstructionsAudioComplete() {

        // stop the instructions toast when done
        if (_toast != null) {
            _toast.cancel();
        }

        // Enable the buttons when sound is complete
        disableQuestionButtons(false);

        isFirstTime = false;

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

        for (int i = 0; i < layout_top.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) layout_top.getChildAt(i);
            mediaButton.stopAudio(); // media button stop audio does release
        }
        for (int i = 0; i < layout_bottom.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) layout_bottom.getChildAt(i);
            mediaButton.stopAudio();
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
