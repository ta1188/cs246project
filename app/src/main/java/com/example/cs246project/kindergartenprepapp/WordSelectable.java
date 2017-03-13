package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

public class WordSelectable extends AppCompatActivity implements View.OnTouchListener, AudioHandler {

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
            final MediaButton btn = new MediaButton(this, item, this);
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
        final Context context = this;

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
                }
                return false;
            }
        });
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

    @Override
    public void onAudioComplete() {
        Log.d("WordSelectable", "AUDIO SHOULD BE COMPLETE");
    }

}
