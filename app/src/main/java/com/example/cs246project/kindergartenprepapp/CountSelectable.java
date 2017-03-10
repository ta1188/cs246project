package com.example.cs246project.kindergartenprepapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CountSelectable extends AppCompatActivity {

    // Create a new Array list that will hold the filenames to reference
    ArrayList<String> myImages = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count_selectable);

        // This is just an example of images for the array
        // In the app these would be passed in or randomly generated
        myImages.add("lower_a");
        myImages.add("lower_a");
        myImages.add("lower_a");
        myImages.add("lower_a");
        myImages.add("lower_a");

        // Find the horizontal scroll view
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_count);

        /*
        * This will loop through our image array based on the length of the array
        * It will then get the index of each file in the drawable directory,
        * then it will update the image for each button.
        * */
        for (int i = 0; i < myImages.size(); i++) {

            /**************************************************************************************/
            /*                                 EXAMPLE/JUNK CODE                                  */
            /**************************************************************************************/
            // THIS SECTION WILL BE HANDLED BY THE "SelectableModel" SUB-CLASS
            int imageFileResourceIndex = getResources().getIdentifier(myImages.get(i), "drawable", getPackageName());
            List<Integer> audioFileResourceIndexes = new ArrayList<>();
            int audioFileResourceIndex1 = getResources().getIdentifier("a", "raw", getPackageName());
            int audioFileResourceIndex2 = getResources().getIdentifier("letter_sound_a", "raw", getPackageName());
            int audioFileResourceIndex3 = getResources().getIdentifier("upper", "raw", getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex1);
            audioFileResourceIndexes.add(audioFileResourceIndex2);
            audioFileResourceIndexes.add(audioFileResourceIndex3);
            MediaModel<Integer> mediaModel = new MediaModel<>(imageFileResourceIndex, audioFileResourceIndexes, 1);

            // THIS SECTION WILL BE HANDLED HERE IN THE "Selectable" ACTIVITIES
            MediaButton<Integer> mediaButton = new MediaButton<>(this, mediaModel);
            layout.addView(mediaButton);
            /**************************************************************************************/
        }
    }

    /* TO DO...
    *  - Add on click handlers for each button dynamically
    *  - Update display of clicked button to not display
    *       - Possibly remove it from the view altogether...?
    * */
}
