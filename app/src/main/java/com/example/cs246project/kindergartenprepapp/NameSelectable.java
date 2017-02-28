package com.example.cs246project.kindergartenprepapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class NameSelectable extends AppCompatActivity {

    // Create a new Array list that will hold the filenames to reference
    ArrayList<String> myImages = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_selectable);

        // This is just an example of images for the array
        // In the app these would be passed in or randomly generated
        myImages.add("ic_3");
        myImages.add("ic_3");
        myImages.add("ic_3");

        // Find the horizontal scroll view
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_name);

        /*
        * This will loop through our image array based on the length of the array
        * It will then get the index of each file in the drawable directory,
        * then it will update the image for each button.
        * */
        for (int i = 0; i < myImages.size(); i++) {
            int index = this.getResources().getIdentifier(myImages.get(i), "drawable", this.getPackageName());
            ImageButton imageButton = new ImageButton(this);
            imageButton.setId(i);
            imageButton.setImageResource(index);
            layout.addView(imageButton);
        }
    }

    /* TO DO...
    *  - Add on click handlers for each button dynamically
    *  - Update display of clicked button to not display
    *       - Possibly remove it from the view altogether...?
    * */
}
