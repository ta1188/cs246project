package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
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
public class CountSelectable extends SkipTapActivity implements View.OnTouchListener, MediaButtonHandler {


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

    // Toast instructions should play the length of the audio instructions
    final private int toastTimeLength = 2000;

    int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set up main activity view in 2 containers
        setContentView(R.layout.count_selectable);
        layout_top = (LinearLayout) findViewById(R.id.layout_count_top);
        layout_bottom = (LinearLayout) findViewById(R.id.layout_count_bottom);
        _progBar = (ProgressBar) findViewById(R.id.progressBarCount);

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

        playInstructions(_model.getActivityInstructionsIndex());
        _model.displayInstructionToast(toastTimeLength);
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
                _model.displayToast(true);
            } else {
                // Show answer toast
                _model.displayToast(false);
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
        // Enable the buttons when sound is complete
        disableQuestionButtons(false);

        isFirstTime = false;
    }

    public void returnToMenu(View view) {
        if(_model._toast != null) {
            _model.cancelToast();
        }

        this.finish();
    }

    /**
     * Overrides from skiptap activity and calls functions to handle all closing activities like
     * stopping audio for media players and existing toasts
     */
    @Override
    public void stopEverything() {

        // stop all audio that is playing
        stopAudio();

        // cancel any toasts that are still showing; done in override
        // leaving app so
        _model.cancelToast();
    }

    /**
     * Handles stopping audio the buttons might be playing when transitioning to other activities
     * or moving to another app.
     */
    @Override
    public void stopAudio() {

        for (int i = 0; i < layout_top.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) layout_top.getChildAt(i);
            mediaButton.stopAudio();
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

        _model.displayInstructionToast(toastTimeLength);

        _backgroundAudioModel.startBackgroundAudio(this);
    }



//
//
//    // Create a new Array list that will hold the filenames to reference
//    private CountSelectableModel _model;
//
//    // Find the horizontal scroll view
//    private LinearLayout layout_top;
//    private LinearLayout layout_bottom;
//    private ProgressBar _progBar;
//    private boolean wasTrue = false;
//    private ImageView imageView;
//    private MediaPlayer mainImageMediaPlayer;
//
//    int count = 1;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.count_selectable);
//        layout_top = (LinearLayout) findViewById(R.id.layout_count_top);
//        layout_bottom = (LinearLayout) findViewById(R.id.layout_count_bottom);
//        _progBar = (ProgressBar) findViewById(R.id.progressBarCount);
//        _model = new CountSelectableModel(this, 4);
//        playInstructions(_model.getActivityInstructionsIndex());
//
//        viewSetUp();
//        imageView = (ImageView) findViewById(R.id.countImage);
//        imageView.setEnabled(false);
//        setMainImage();
//        // Disable the buttons
//        enableDisableButtons(true);
//    }
//
//    public void viewSetUp() {
//        /**
//         * This will loop through the generatedValueList based on the length of the array
//         * It will then get the index of each file in the drawable directory,
//         * then it will update the image for each button.
//         * */
//        for (MediaModel item : _model.generateValueList()) {
//            final MediaButton btn = new MediaButton(this, item, this);
//            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
//            btn.setScaleType(ImageView.ScaleType.CENTER);
//            btn.setAdjustViewBounds(true);
//            btn.setPadding(5, 5, 5, 5);
//            btn.setElevation(10);
//            ((ViewGroup.MarginLayoutParams) btn.getLayoutParams()).setMargins(10, 5, 30, 5);
//            btn.setBackgroundColor(Color.parseColor(_model.getBtnColor()));
//
//
//            if (count <= 2) {
//                layout_top.addView(btn);
//            } else {
//                layout_bottom.addView(btn);
//            }
//            count++;
//        }
//    }
//
//    @Override
//    public void onMediaButtonTouched(MediaButton mediaButton, MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if(_model.isCorrect(mediaButton.getValue())) {
//                wasTrue = true;
//                _progBar.incrementProgressBy(1);
//                // Show answer toast
//                _model.displayToast(true);
//            } else {
//                // Show answer toast
//                _model.displayToast(false);
//            }
//            // Disable the buttons
//            enableDisableButtons(true);
//        }
//    }
//
//    /**
//     * Will disable or enable the layout buttons
//     * */
//    private void enableDisableButtons(Boolean state){
//        for (int i = 0; i < layout_top.getChildCount(); i++) {
//            MediaButton mediaButton = (MediaButton) layout_top.getChildAt(i);
//            mediaButton.setIsDisabled(state);
//        }
//        for (int i = 0; i < layout_bottom.getChildCount(); i++) {
//            MediaButton mediaButton = (MediaButton) layout_bottom.getChildAt(i);
//            mediaButton.setIsDisabled(state);
//        }
//    }
//
//
//    private void setMainImage() {
//        // Grab the image resource and set the image drawable
//        Drawable res = getResources().getDrawable(_model.getAnswerResourceIndex(), getTheme());
//        imageView.setImageDrawable(res);
//        final Context context = this;
//
//        /**
//         * Setup event listener for main image
//         * */
//        imageView.setOnTouchListener(new View.OnTouchListener() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    MediaPlayer mp = new MediaPlayer();
//
//                    // Reset the media player
//                    mp.reset();
//
//                    int soundId = getResources().getIdentifier("instruct_how_many_objects_can_you_count", "raw", getPackageName());
//
//                    mp.create(context, soundId);
//                    // Load the media player with a new audio resource
//                    try {
//                        AssetFileDescriptor afd = context.getResources().openRawResourceFd(soundId);
//                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                        afd.close();
//                        mp.prepare();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    // Play the audio
//                    mp.start();
//                    imageView.setEnabled(false);
//
//                    Runnable disableImageClick = new Runnable() {
//                        @Override
//                        public void run() {
//                            imageView.setEnabled(true);
//                        }
//                    };
//                    Handler handler = new Handler();
//                    handler.postDelayed(disableImageClick, 1000);
//                }
//                return false;
//            }
//        });
//        // Enable the buttons
//        enableDisableButtons(false);
//    }
//
//
//    private void resetActivity() {
//        layout_top.removeAllViews();
//        layout_bottom.removeAllViews();
//        wasTrue = false;
//        count = 1;
//        viewSetUp();
//        setMainImage();
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        return false;
//    }
//
//    /**
//     * Called when audio has completed (for media buttons only)
//     * */
//    @Override
//    public void onAudioComplete() {
//        if (_model._isActivityDone) {
//            this.finish();
//        } else {
//            // Enable the buttons when sound is complete
//            enableDisableButtons(false);
//            // check for true
//            if (wasTrue)
//                resetActivity();
//        }
//    }
//
//    @Override
//    public void onInstructionsAudioComplete() {
//        imageView.setEnabled(true);
//        // Enable the buttons when sound is complete
//        enableDisableButtons(false);
//    }
//
//    public void returnToMenu(View view) {
//        this.finish();
//    }
//
//    /**
//     * Handles stopping audio the buttons might be playing when transitioning to other activities
//     * or moving to another app.
//     */
//    @Override
//    public void stopAudio() {
//        super.stopAudio();
//        for (int i = 0; i < layout_top.getChildCount(); i++) {
//            MediaButton mediaButton = (MediaButton) layout_top.getChildAt(i);
//            mediaButton.stopAudio();
//        }
//        for (int i = 0; i < layout_bottom.getChildCount(); i++) {
//            MediaButton mediaButton = (MediaButton) layout_bottom.getChildAt(i);
//            mediaButton.stopAudio();
//        }
//    }

}
