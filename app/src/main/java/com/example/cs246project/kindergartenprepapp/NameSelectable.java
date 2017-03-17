package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles selecting numbers of the user's name
 * @author Trevor Adams
 * */
public class NameSelectable extends AppCompatActivity implements View.OnTouchListener, AudioHandler {

    // Create a new Array list that will hold the filenames to reference
    private NameSelectableModel _model;

    // Find the horizontal scroll view
    private LinearLayout layout_name;
    private LinearLayout layout_top_name;

    private int position = 0;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_selectable);
        layout_name = (LinearLayout) findViewById(R.id.layout_name);
        layout_top_name = (LinearLayout) findViewById(R.id.layout_top_name);
        _model = new NameSelectableModel(this, "first");

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
            /**
             * Setup event listeners for media buttons
             * */
            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if(_model.isCorrectOrder((Character) ((MediaButton) v).getValue())) {
                            Log.d("NameSelectable", "------- CORRECT --------" + v.getId());
                            CharSequence text = "Correct!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            ViewGroup group = (ViewGroup) toast.getView();
                            TextView messageTextView = (TextView) group.getChildAt(0);
                            messageTextView.setTextSize(25);
                            View view = toast.getView();
                            view.setBackgroundColor(Color.parseColor("#00e676"));
                            view.setPadding(20, 10, 20, 10);
                            toast.show();

                            // Remove correct button selection from view
                            v.setVisibility(View.GONE);

                            ImageView letterTopLine = (ImageView) findViewById(position);
                            letterTopLine.setBackgroundResource(0);

                            // Progress name position indicator
                            if (position < (count - 1)) {
                                ImageView nextLetterSpot = (ImageView) findViewById(position + 1);
                                Drawable drawRes = getResources().getDrawable(R.drawable.button_border, getTheme());
                                nextLetterSpot.setBackground(drawRes);

                                Animation fadeIn = AnimationUtils.loadAnimation(NameSelectable.this, R.anim.fade_in_animation);
//                                LayoutAnimationController layoutAnimation = AnimationUtils.loadLayoutAnimation(NameSelectable.this, R.anim.fade_in_animation)

                                nextLetterSpot.startAnimation(fadeIn);

                            }
                            // Get the letter index for the letter of the name selected
                            int resourceIndex;
                            if (position == 0) {
                                resourceIndex = getResources().getIdentifier("upper_" + ((MediaButton) v).getValue().toString().toLowerCase(), "drawable", getPackageName());
                            } else {
                                resourceIndex = getResources().getIdentifier("lower_" + ((MediaButton) v).getValue().toString().toLowerCase(), "drawable", getPackageName());
                            }

                            letterTopLine.setImageResource(resourceIndex);

                            position++;
                        } else {
                            Log.d("NameSelectable", "------- WRONG --------" + v.getId());

                            CharSequence text = "Incorrect!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            ViewGroup group = (ViewGroup) toast.getView();
                            TextView messageTextView = (TextView) group.getChildAt(0);
                            messageTextView.setTextSize(25);
                            View view = toast.getView();
                            view.setBackgroundColor(Color.parseColor("#ff8a65"));
                            view.setPadding(20, 10, 20, 10);
                            toast.show();

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
            btn.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
            btn.setScaleType(ImageView.ScaleType.CENTER);
            btn.setAdjustViewBounds(true);

            layout_name.addView(btn);

            // Add letter options on Top
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(90, 90));
            imageView.setPadding(10, 0, 10, 0);
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

    /**
     * Will disable or enable the layout buttons
     * */
    private void enableDisableButtons(Boolean state){
        for (int i = 0; i < layout_name.getChildCount(); i++) {
            layout_name.getChildAt(i).setEnabled(state);
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
        if (_model._isActivityDone) {
            this.finish();
        } else {
            // Unlock buttons when sound is complete
            enableDisableButtons(true);
        }
    }

    public void returnToMenu(View view) {
        this.finish();
    }

}
