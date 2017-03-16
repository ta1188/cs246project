package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.SharedPreferences;
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
public class NameSelectableModel extends SelectableModel {

    // member variables
    SharedPreferences _sharedPreferences;
    String _firstName;
    String _lastName;
    String _currenthName;

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

    /************ Constructors *************/

    public NameSelectableModel(Context context, String whichName) {
        super(context);


        // get name from shared preferences
        _sharedPreferences = _context.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        _firstName = _sharedPreferences.getString("FIRST_NAME", ""); // FIX 'NAME' to reference 'FIRST_NAME'
        _lastName = _sharedPreferences.getString("LAST_NAME", ""); // FIX 'NAME' to reference 'LAST_NAME'



        switch (whichName) {
            case "first" :
                if(_firstName.length() > 0) {
                    _optionCount = _firstName.length();
                    _currenthName = _firstName;
                    _isActivityDone = false;
                } else {
                    Log.i(TAG, "NameSelectableModel: No first name entered");
                }

                break;
            case "last" :
                if(_lastName.length() > 0) {
                    _optionCount = _lastName.length();
                    _currenthName = _lastName;
                    _isActivityDone = false;
                } else {
                    Log.i(TAG, "NameSelectableModel: No last name entered ");
                }
                break;
            default :
                _isActivityDone = true;
                return;
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

        // increment through letters
        for (char i = 0 ; i < _optionCount; i++) {
            _questionBank.add(Character.toString(_currenthName.charAt(i)));
            _answerBank.add(Character.toString(_currenthName.charAt(i)));
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

        // Using the random values now associate the images and sounds to buttons to be used
        //    by the calling activity
        for (String value : randomValues) {

            List<Integer> audioFileResourceIndexes = new ArrayList<>();
            int imageFileResourceIndex;

            //check if upper case letter is input
            char[] tempLetter = value.toCharArray();
            if (Character.isUpperCase(tempLetter[0])) {
               // get the image resource for name
               imageFileResourceIndex = _context.getResources().getIdentifier("upper_" + value.toLowerCase(), "drawable", _context.getPackageName());

            } else { // lowercase
                // get the image resource for name
                imageFileResourceIndex = _context.getResources().getIdentifier("lower_" + value.toLowerCase(), "drawable", _context.getPackageName());
            }

            // get the audio resource for name
            int audioFileResourceIndex1 = _context.getResources().getIdentifier(value.toLowerCase(), "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex1);

            // letter sound
            int audioFileResourceIndex3 = _context.getResources().getIdentifier("letter_sound_" + value.toLowerCase(), "raw", _context.getPackageName());
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