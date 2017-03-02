package com.example.cs246project.kindergartenprepapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

/**
 * The Login Activity where the user will enter their name to be used in the app's "activities".
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

        SharedPreferences settings = this.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        String name = settings.getString("NAME", "");

        // Retrieve name
        EditText nameField = (EditText) findViewById(R.id.editTextName);
        nameField.setText(name);
    }

    public void goToMainMenu(View view) {
        SharedPreferences settings = this.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // get value from the text field
        EditText nameField = (EditText) findViewById(R.id.editTextName);

        // will save the name value
        editor.putString("NAME", nameField.getText().toString());
        editor.commit();

        // create intent to start menu activity
        Intent intent = new Intent(getBaseContext(), MenuActivity.class);
        startActivity(intent);


    }

}
