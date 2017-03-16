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
 * @since   2017-03-08
 */

/**
 * WORDSELECTABLEMODEL Class will build a list of random values used with the word selectable
 * activity. Subclass of Selectable Model
 */
public class WordSelectableModel extends SelectableModel {

    /*********** Member Variables ************/

    // static resource for word activity instruction
    //private static final String _activityInstructions =
      //      "instruct_pick_letter_that_matches_first_sound_of_picture";

    // for changing up the motivational messages
    static final List<String> correct = new ArrayList<String>(){{
        add("motivate_great_job");
        add("motivate_you_did_it");
        add("motivate_you_found_the_letter");
    }};

    // for changing up the motivational messages
    static final List<String> incorrect = new ArrayList<String>(){{
        add("motivate_try_again");
    }};


    /* Constructors */

    public WordSelectableModel(Context context, int optionCount) {
        super(context);
        if((optionCount > 0) && (optionCount <= 26)) {
            _optionCount = optionCount;
            _isActivityDone = false;
        } else {
            Log.i(TAG, "CountSelectableModel: 0 < option count <= 26; out of possible range");
        }

        // initialize question bank
        buildInitialQuestionAnswerBanks();
    }

    /* METHODS */

    /**
     *  GETACTIVITYINSTRUCTIONSINDEX will associate and return instruction sound index to word
     *  selectable activity
     */
    //public int getActivityInstructionsIndex() {

      //  return _context.getResources().getIdentifier(_activityInstructions, "raw", _context.getPackageName());
    //}

    

    /**
     *  BUILDINITIALQUESTIONBANK method to build values that can be randomly pulled from
     */
    protected void buildInitialQuestionAnswerBanks() {
        _answerBank  = new ArrayList<>();
        _questionBank = new ArrayList<>();

        for (char i = 'a' ; i <= 'z'; i++) {
            _questionBank.add(Character.toString(i));
            _answerBank.add(Character.toString(i));
        }
    }

    /**
     * GETANSWERRESOURCEINDEX will get the answer for the activity as a resource index
     */
    public int getAnswerResourceIndex() {
        int resourceIndex = _context.getResources().getIdentifier("object_" + _answer, "drawable", _context.getPackageName());
        return resourceIndex;
    }

    /**
     * GENERATEVALUELIST will build a set of indexes required for retrieving audio and image files
     */
    public List<MediaModel> generateValueList() {

        List<String> randomValues;
        List<MediaModel> results = new ArrayList<>();

        // make sure the activity is not over because all values have been selected correctly
        //    otherwise would result in endless loop in random activity
        if (!_isActivityDone) {
            // get random values with is parameters
            randomValues = randomValuesGenerator();
        }
        else {
            Log.w(TAG, "generateButtonList: able to generate random values " +
                    _answerBank.size() + " > 0");
            return null;
        }

        // shuffle list to make is random
        Collections.shuffle(randomValues);

        // Useing the random values now associate the images and sounds to buttons to be used
        //    by the calling activity
        for (String value : randomValues) {

            // set the picture to match the letter sound
            int imageFileResourceIndex = _context.getResources().getIdentifier("upper_" + value, "drawable", _context.getPackageName());
            List<Integer> audioFileResourceIndexes = new ArrayList<>();

            // retrieve letter audio
            int audioFileResourceIndex1 = _context.getResources().getIdentifier(value, "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex1);

            // letter sound
            int audioFileResourceIndex3 = _context.getResources().getIdentifier("letter_sound_" + value, "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex3);

            // used to make the motivations different each time
            Collections.shuffle(correct);

            // used to make the motivations different each time
            Collections.shuffle(incorrect);

            // correct answer
            if (value == _answer) {
                int audioFileResourceIndex2 = _context.getResources().getIdentifier(correct.get(0), "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            } else { // incorrect
                int audioFileResourceIndex2 = _context.getResources().getIdentifier(incorrect.get(0), "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            }

            // retrieve and associate buttons with image and audio
            MediaModel<String> mediaModel = new MediaModel<>(imageFileResourceIndex, audioFileResourceIndexes, value);
            results.add(mediaModel);
        }

        return results;
    }
}