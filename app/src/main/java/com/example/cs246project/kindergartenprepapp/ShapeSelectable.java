package com.example.cs246project.kindergartenprepapp;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ShapeSelectable extends SkipTapActivity implements View.OnTouchListener, MediaButtonHandler {

    // Create a new Array list that will hold the filenames to reference
    private ShapeSelectableModel _model;

    // Find the horizontal scroll view
    private LinearLayout _shape_layout;
    private boolean isFirstTime = true;
    private Button _shapeButton;
    private ProgressBar _progBar;
    private boolean wasTrue = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_selectable);
        _shape_layout = (LinearLayout) findViewById(R.id.shape_layout);
        _progBar = (ProgressBar) findViewById(R.id.progressBar2);
        _model = new ShapeSelectableModel(this, 3);
        _shapeButton = (Button) findViewById(R.id.shapeButton);

        viewSetUp();
        setAnswerButtonVal();
        // Disable the buttons
        enableDisableButtons(true);
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

            _shape_layout.addView(btn);
        }
    }

    /**
     * Will disable or enable the layout buttons
     * */
    private void enableDisableButtons(Boolean state){
        for (int i = 0; i < _shape_layout.getChildCount(); i++) {
            MediaButton mediaButton = (MediaButton) _shape_layout.getChildAt(i);
            mediaButton.setIsDisabled(state);
        }
    }

    private void playMainImageSound() {
        String answer = "shape_" + _model.getAnswer();
        MediaPlayer mp = new MediaPlayer();

        // Reset the media player
        mp.reset();

        int soundId = getResources().getIdentifier(answer, "raw", getPackageName());

        mp.create(this, soundId);
        // Load the media player with a new audio resource
        try {
            AssetFileDescriptor afd = this.getResources().openRawResourceFd(soundId);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Play the audio
        mp.start();
    }

    private void setAnswerButtonVal() {
        // Grab the image resource and set the image drawable
        Drawable res = getResources().getDrawable(_model.getAnswerResourceIndex(), getTheme());

        // Need to get string value of button from model...
//        _shapeButton.setImageDrawable(res);
        if (!isFirstTime) {
            //
            playMainImageSound();
        }


        /**
         * Setup event listener for main image
         * */
        _shapeButton.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    playMainImageSound();

                    _shapeButton.setEnabled(false);

                    Runnable disableImageClick = new Runnable() {
                        @Override
                        public void run() {
                            _shapeButton.setEnabled(true);
                        }
                    };
                    Handler handler = new Handler();
                    handler.postDelayed(disableImageClick, 1000);
                }
                return false;
            }
        });
        // Enable the buttons
        enableDisableButtons(false);
    }

    private void resetActivity() {
        _shape_layout.removeAllViews();
        wasTrue = false;
        viewSetUp();
        setAnswerButtonVal();
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
            enableDisableButtons(false);
            // check for true
            if (wasTrue)
                resetActivity();
        }
    }

    @Override
    public void onMediaButtonTouched(MediaButton mediaButton, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if(_model.isCorrect(mediaButton.getValue())) {
                wasTrue = true;
                _progBar.incrementProgressBy(1);
                // Show answer toast
                _model.displayToast(true);
            } else {
                // Show answer toast
                _model.displayToast(false);
            }

            // Disable the buttons
            enableDisableButtons(true);
        }
    }

    @Override
    public void onInstructionsAudioComplete() {
        playMainImageSound();
        isFirstTime = false;
        // Delay a little for object audio to complete
        try {
            TimeUnit.MILLISECONDS.sleep(800);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        _shapeButton.setEnabled(true);
        // Enable the buttons when sound is complete
        enableDisableButtons(false);
    }


    public void returnToMenu(View view) {
        this.finish();
    }
}
