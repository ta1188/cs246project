package com.example.cs246project.kindergartenprepapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

    /* Parameters:
        * Factory:
        *   Type
        *   ArrayList of answers
        *   Number of buttons
        *   Context
        *
        * If name...
        *   Type
        *   LetterArray
        *   Context
    * */

    // Create a new Array list that will hold the filenames to reference
    ArrayList<String> myImages = new ArrayList<String>();

    // Find the horizontal scroll view
    LinearLayout layout;

    ProgressBar _progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_selectable);
        layout = (LinearLayout) findViewById(R.id.layout_word);
        _progBar = (ProgressBar) findViewById(R.id.progressBar2);

        // This is just an example of images for the array
        // In the app these would be passed in or randomly generated
        myImages.add("upper_r");
        myImages.add("upper_h");
        myImages.add("upper_j");
        myImages.add("upper_t");

        viewSetUp();

        setMainImage();
    }

    public void viewSetUp() {
         /*
        * This will loop through the returned array based on the length of the array
        * It will then get the index of each file in the drawable directory,
        * then it will update the image for each button.
        * */
        for (int i = 0; i < myImages.size(); i++) {
            int index = this.getResources().getIdentifier(myImages.get(i), "drawable", this.getPackageName());
            ImageButton imageButton = new ImageButton(this);
            if (i == 0) {
                imageButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            _progBar.incrementProgressBy(1);
                            Log.d("WordSelectable", "------- CORRECT --------");
                            CharSequence text = "Correct!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            resetActivity();
                        }
                        return false;
                    }
                });
            } else {
                imageButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            Log.d("WordSelectable", "------- WRONG --------");
                            CharSequence text = "Incorrect!";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        return false;
                    }
                });
            }

            imageButton.setImageResource(index);
            layout.addView(imageButton);

        }
    }

    public void setMainImage() {
        // Parse the first element for letter to match
        String answerImg = myImages.get(0).substring(myImages.get(0).length() - 1);
        // Structure filename of object
        answerImg = "object_" + answerImg;

        // Grab the image resource and set the image drawable
        int imageId = this.getResources().getIdentifier(answerImg, "drawable", this.getPackageName());
        Drawable res = getResources().getDrawable(imageId, getTheme());
        ImageView imageView = (ImageView) findViewById(R.id.objectImage);
        imageView.setImageDrawable(res);
    }


    public void resetActivity() {
        layout.removeAllViews();
        myImages.clear();
        myImages.add("upper_b");
        myImages.add("upper_g");
        myImages.add("upper_i");
        myImages.add("upper_a");

        setMainImage();
        viewSetUp();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

}
