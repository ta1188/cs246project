package com.example.cs246project.kindergartenprepapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class WordSelectable extends AppCompatActivity implements View.OnTouchListener {

    // Create a new Array list that will hold the filenames to reference
    WordSelectableModel _model;

    // Find the horizontal scroll view
    LinearLayout layout;

    ProgressBar _progBar;
    private Handler _handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_selectable);
        layout = (LinearLayout) findViewById(R.id.layout_word);
        _progBar = (ProgressBar) findViewById(R.id.progressBar2);
        _model = new WordSelectableModel(this, 4);

        viewSetUp();

        setMainImage();
    }

    public void viewSetUp() {
         /*
        * This will loop through the returned array based on the length of the array
        * It will then get the index of each file in the drawable directory,
        * then it will update the image for each button.
        * */
        for (MediaModel item : _model.generateValueList()) {
            MediaButton btn = new MediaButton(this, item);
            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if(_model.isCorrect(((MediaButton) v).getValue())) {
                            _progBar.incrementProgressBy(1);
                            Log.d("WordSelectable", "------- CORRECT --------");

                            CharSequence text = "Correct!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            // Set up runnable for handler to reference
                            Runnable myRunnable = new Runnable() {
                                public void run() {
                                    resetActivity();
                                }
                            };

                            // Run on new thread...
                            _handler = new Handler();
                            _handler.postDelayed(myRunnable, 3000);

                        } else {
                            Log.d("WordSelectable", "------- WRONG --------");

                            CharSequence text = "Incorrect!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }

                    }
                    return false;
                }
            });

            layout.addView(btn);
        }
    }

    public void setMainImage() {
        // Grab the image resource and set the image drawable
        Drawable res = getResources().getDrawable(_model.getAnswerResoureIndex(), getTheme());
        ImageView imageView = (ImageView) findViewById(R.id.objectImage);
        imageView.setImageDrawable(res);
    }


    public void resetActivity() {
        layout.removeAllViews();

        viewSetUp();
        setMainImage();

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}
