package com.example.cs246project.kindergartenprepapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;

/**
 * An Activity that allows the user to trace letters in the alphabet, numbers, or words (including
 * their name). This is meant to help them improve their fine motor, writing, and recognition
 * skills.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class LetterTraceActivity extends CharacterTraceActivity {

    @Override
    protected void instantiateModel() {
        _model = new LetterTraceModel();
    }

    @Override
    protected void setLayoutParamStartPoint(FrameLayout.LayoutParams layoutParams, int startPoint) {
        layoutParams.setMarginStart(startPoint);
    }

}
