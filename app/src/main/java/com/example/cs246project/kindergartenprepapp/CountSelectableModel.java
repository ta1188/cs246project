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
 * COUNTSELECTABLEMODEL Class will build a list of random values used with the count selectable
 * activities. Subclass of Selectable Model
 */
public class CountSelectableModel extends SelectableModel {

    // for changing up the motivational messages
    static final List<String> correct = new ArrayList<String>(){{
        add("motivate_great_job");
        add("motivate_you_did_it");
        add("motivate_you_found_the_number");
    }};

    // for changing up the motivational messages
    static final List<String> incorrect = new ArrayList<String>(){{
        add("motivate_try_again");
    }};

    /************ Constructors *************/

    /**
     * @param optionCount how many values are need to be generated
     */
    public CountSelectableModel(Context context, int optionCount) {
        super(context);
        if((optionCount > 0) && (optionCount <= 10)) {
            _optionCount = optionCount;
            _isActivityDone = false;
        }
        else {
            Log.i(TAG, "CountSelectableModel: 0 < option count <= 10; out of range");

        }

        // initialize question bank
        buildInitialQuestionAnswerBanks();
    }

    /************** METHODS ***************/

    /**
     *  BUILDINITIALQUESTIONBANK method to build values that can be randomly pulled from
     */
    protected void buildInitialQuestionAnswerBanks() {
        _answerBank  = new ArrayList<>();
        _questionBank = new ArrayList<>();

        for (int i = 0 ; i <= 10; i++) {
            _questionBank.add(i);
            _answerBank.add(i);
        }
    }

    /**
     * GENERATEVALUELIST will build a set of indexes required for retrieving audio and image files
     */
    public List<MediaModel> generateValueList() {

        List<String> randomValues = new ArrayList<>();
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

        // Using the random values now associate the images and sounds to buttons to be used
        //    by the calling activity
        for (String value : randomValues) {

            // set the picture to match the number
            int imageFileResourceIndex = _context.getResources().getIdentifier("number_" + value, "drawable", _context.getPackageName());
            List<Integer> audioFileResourceIndexes = new ArrayList<>();

            // number name
            int audioFileResourceIndex3 = _context.getResources().getIdentifier("number_" + value, "raw", _context.getPackageName());
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