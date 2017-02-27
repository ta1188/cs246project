package com.example.cs246project.kindergartenprepapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * The main (menu) Activity where the user can select an tracing or selection "activity" to help
 * them prepare for Kindergarten by enhancing their reading, writing, and comprehension skills.
 * <p>
 * @author  Micheal Lucero, Trevor Adams, & Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendToWordSelectable(View view) {
        Intent intent = new Intent(getBaseContext(), WordSelectable.class);
        startActivity(intent);
    }
}
