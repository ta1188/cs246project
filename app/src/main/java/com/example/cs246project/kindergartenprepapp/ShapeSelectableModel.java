package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * <p>
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-03-23
 */

public class ShapeSelectableModel extends SelectableModel {

     /* Member Variables */

    // static resource for word activity instruction
    private static final String _activityInstructions =
            "instruct_find_the_shape_thats_a";

    // for changing up the motivational messages
    static final List<String> correct = new ArrayList<String>(){{
        add("motivate_great_job");
        add("motivate_you_did_it");
        add("motivate_way_to_go");
        add("motivate_you_are_awesome");
        add("motivate_stupendous");
        add("motivate_you_found_the_shape");
    }};

    // for changing up the motivational messages
    static final List<String> incorrect = new ArrayList<String>(){{
        add("motivate_try_again");
        add("motivate_dont_give_up");
        add("motivate_keep_practicing");
        add("motivate_give_it_another_try");
        add("motivate_not_quite");
    }};

    /* Constructors */

    public ShapeSelectableModel(Context context, int optionCount) {
        super(context);
        if((optionCount > 0) && (optionCount <= 10)) {
            _optionCount = optionCount;
            _isActivityDone = false;
        } else {
            Log.i(TAG, "ShapeSelectableModel: 0 < option count <= 10; out of possible range");
        }

        // initialize question bank
        buildInitialQuestionAnswerBanks();
    }

    /* METHODS */

    /**
     *  Gets the instruction audio resource index for the word selectable activity.
     *  @return The audio resource index for the activity instruction.
     */
    public int getActivityInstructionsIndex() {

        return _context.getResources().getIdentifier(_activityInstructions, "raw", _context.getPackageName());
    }

    /**
     * Build values that can be randomly pulled from.
     */
    protected void buildInitialQuestionAnswerBanks() {
        _answerBank  = new ArrayList<String>() {{
            add("circle");
            add("rhombus");
            add("heart");
            add("hexagon");
            add("octagon");
            add("pentagon");
            add("rectangle");
            add("square");
            add("star");
            add("triangle");
        }};

        _questionBank = new ArrayList<String>() {{
            add("circle");
            add("rhombus");
            add("heart");
            add("hexagon");
            add("octagon");
            add("pentagon");
            add("rectangle");
            add("square");
            add("star");
            add("triangle");
        }};
    }

    /**
     * Get the answer for the activity as a resource index.
     */
    public int getAnswerResourceIndex() {
        int resourceIndex = _context.getResources().getIdentifier("shape_" + _answer.toString(), "drawable", _context.getPackageName());
        return resourceIndex;
    }

    /**
     * Build a set of indexes required for retrieving audio and image files
     * Functions as follows:
     */
    @Override
    public List<MediaModel> generateValueList() {

        List<String> randomValues;
        List<MediaModel> questionsAndAnswer = new ArrayList<>();

        // make sure the activity is not over because all values have been selected correctly
        //    otherwise would result in endless loop in random activity
        if (!_isActivityDone) {
            // get random values with is parameters
            randomValues = randomValuesGenerator();
        }
        else {
            return null;
        }

        // shuffle list to make is random
        Collections.shuffle(randomValues);

        // Using the random values now associate the images and sounds to buttons to be used
        //    by the calling activity
        for (String value : randomValues) {

            // set the picture to match the letter sound
            int imageFileResourceIndex = _context.getResources().getIdentifier("shape_" + value, "drawable", _context.getPackageName());
            List<Integer> audioFileResourceIndexes = new ArrayList<>();

            // retrieve letter audio
            int audioFileResourceIndex1 = _context.getResources().getIdentifier("shape_" + value, "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex1);

            // used to make the motivations different each time
            Collections.shuffle(correct);

            // used to make the motivations different each time
            Collections.shuffle(incorrect);

            // correct answer
            if (value == _answer) {
                int audioFileResourceIndex2 = _context.getResources().getIdentifier(correct.get(0).toString(), "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            } else { // incorrect
                int audioFileResourceIndex2 = _context.getResources().getIdentifier(incorrect.get(0).toString(), "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            }

            // retrieve and associate buttons with image and audio
            MediaModel<String> mediaModel = new MediaModel<>(imageFileResourceIndex, audioFileResourceIndexes, value);
            questionsAndAnswer.add(mediaModel);
        }

        return questionsAndAnswer;
    }
}
