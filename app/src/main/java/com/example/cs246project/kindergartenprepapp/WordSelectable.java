package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Handles finding first letter of word image shown
 * @author Trevor Adams
 * */
public class WordSelectable extends SkipTapActivity implements View.OnTouchListener, AudioHandler {

    // Create a new Array list that will hold the filenames to reference
    private WordSelectableModel _model;

    // Find the horizontal scroll view
    private LinearLayout layout;
    private ProgressBar _progBar;
    private boolean wasTrue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_selectable);
        layout = (LinearLayout) findViewById(R.id.layout_word);
        _progBar = (ProgressBar) findViewById(R.id.progressBar2);
        _model = new WordSelectableModel(this, 4);
        playInstructions(_model.getActivityInstructionsIndex());

        viewSetUp();
        setMainImage();
    }

    public void viewSetUp() {
         /**
        * This will loop through the generatedValueList based on the length of the array
        * It will then get the index of each file in the drawable directory,
        * then it will update the image for each button.
        * */
        for (MediaModel item : _model.generateValueList()) {
            final MediaButton btn = new MediaButton(this, item, this);
            /**
             * Setup event listeners for media buttons
             * */
            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if(_model.isCorrect(((MediaButton) v).getValue())) {
                            wasTrue = true;
                            _progBar.incrementProgressBy(1);
                            Log.d("WordSelectable", "------- CORRECT --------" + v.getId());
                            CharSequence text = "Correct!";
                            int duration = Toast.LENGTH_SHORT;

                            final Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            ViewGroup group = (ViewGroup) toast.getView();
                            TextView messageTextView = (TextView) group.getChildAt(0);
                            messageTextView.setTextSize(25);
                            View view = toast.getView();
                            view.setBackgroundColor(Color.parseColor("#00e676"));
                            view.setPadding(20, 10, 20, 10);

                            // Set the countdown to display the toast
                            CountDownTimer toastCountDown = new CountDownTimer(800, 1000 /*Tick duration*/) {
                                public void onTick(long millisUntilFinished) {
                                    toast.show();
                                }
                                public void onFinish() {
                                    toast.cancel();
                                }
                            };

                            // Show the toast and starts the countdown
                            toast.show();
                            toastCountDown.start();


                        } else {
                            Log.d("WordSelectable", "------- WRONG --------" + v.getId());

                            CharSequence text = "Incorrect!";
                            int duration = Toast.LENGTH_SHORT;

                            final Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            ViewGroup group = (ViewGroup) toast.getView();
                            TextView messageTextView = (TextView) group.getChildAt(0);
                            messageTextView.setTextSize(25);
                            View view = toast.getView();
                            view.setBackgroundColor(Color.parseColor("#ff8a65"));
                            view.setPadding(20, 10, 20, 10);

                            // Set the countdown to display the toast
                            CountDownTimer toastCountDown = new CountDownTimer(800, 1000 /*Tick duration*/) {
                                public void onTick(long millisUntilFinished) {
                                    toast.show();
                                }
                                public void onFinish() {
                                    toast.cancel();
                                }
                            };

                            // Show the toast and starts the countdown
                            toast.show();
                            toastCountDown.start();

                        }
                        // Runnable for disabling buttons on new thread to not impede audio playing
                        Runnable enableDisable = new Runnable() {
                            @Override
                            public void run() {
                                enableDisableButtons(false);
                            }
                        };
                        Handler handler = new Handler();
                        handler.postDelayed(enableDisable, 200);
                    }
                    return false;
                }
            });
            layout.addView(btn);
        }
    }

    /**
     * Will disable or enable the layout buttons
     * */
    private void enableDisableButtons(Boolean state){
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setEnabled(state);
        }
    }

    private void setMainImage() {
        // Grab the image resource and set the image drawable
        Drawable res = getResources().getDrawable(_model.getAnswerResourceIndex(), getTheme());
        final ImageView imageView = (ImageView) findViewById(R.id.objectImage);
        imageView.setImageDrawable(res);
        final Context context = this;

        /**
         * Setup event listener for main image
         * */
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    String answer = "object_" + _model.getAnswer();
                    MediaPlayer mp = new MediaPlayer();

                    // Reset the media player
                    mp.reset();

                    int soundId = getResources().getIdentifier(answer, "raw", getPackageName());

                    mp.create(context, soundId);
                    // Load the media player with a new audio resource
                    try {
                        AssetFileDescriptor afd = context.getResources().openRawResourceFd(soundId);
                        mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        afd.close();
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Play the audio
                    mp.start();
                    imageView.setEnabled(false);

                    Runnable disableImageClick = new Runnable() {
                        @Override
                        public void run() {
                            imageView.setEnabled(true);
                        }
                    };
                    Handler handler = new Handler();
                    handler.postDelayed(disableImageClick, 1000);
                }
                return false;
            }
        });
        // Unlock buttons (for crazy-clicks between transitions)
        enableDisableButtons(true);
    }


    private void resetActivity() {
        layout.removeAllViews();
        wasTrue = false;
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
            // Unlock buttons when sound is complete
            enableDisableButtons(true);
            // check for true
            if (wasTrue)
                resetActivity();
        }
    }

    public void returnToMenu(View view) {
        this.finish();
    }

}
