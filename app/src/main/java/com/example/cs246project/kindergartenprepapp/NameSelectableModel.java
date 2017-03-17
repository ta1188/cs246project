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
 * @version 1.1
 * @since   2017-03-16
 */

/**
 * Class controls logic and creates a list of random values used with the name selectable
 * activity. Subclass of Selectable Model
 */
public class NameSelectableModel extends SelectableModel {

    /* MEMBER VARIABLES */

    // member variables
    SharedPreferences _sharedPreferences;
    String _firstName;
    String _lastName;
    String _currentName;
    Character _answerOrder;

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

    /* CONSTRUCTORS */

    /**
     * Will set up the initial state for the name seletable activity based on first or last
     * name solection
     * @param context helps identify caller context
     * @param whichName identifies choice of "first" or "last" name. Quoted values required
     *                  exactly to work.
     */
    public NameSelectableModel(Context context, String whichName) {
        super(context);


        // get full name from shared preferences
        _sharedPreferences = _context.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        _firstName = _sharedPreferences.getString("FIRST_NAME", ""); // FIX 'NAME' to reference
                                                                     //    'FIRST_NAME'
        _lastName = _sharedPreferences.getString("LAST_NAME", "");   // FIX 'NAME' to reference
                                                                     //    'LAST_NAME'

        // determine proper set up of answer bank based on name requested
        switch (whichName) {
            case "first" :
                if(_firstName.length() > 0) {
                    _optionCount = _firstName.length();
                    _currentName = _firstName;       // set up a common variable to store the name
                    _isActivityDone = false;
                } else {
                    Log.i(TAG, "NameSelectableModel: No first name entered");
                }

                break;
            case "last" :
                if(_lastName.length() > 0) {
                    _optionCount = _lastName.length();
                    _currentName = _lastName;
                    _isActivityDone = false;
                } else {
                    Log.i(TAG, "NameSelectableModel: No last name entered ");
                }
                break;
            default :
                _isActivityDone = true;
                return;
        }

        // set the initial answer as the first letter of the name
        _answerOrder = _currentName.charAt(0);

        // initialize question bank
        buildInitialQuestionAnswerBanks();

    }

    /* METHODS */

    /**
     * Checks if the button selected is correct and increments to the next correct choice
     * in the name.
     * @param value is the answer that was chosen and passed for validation
     * @return will tell if value was correct or not.
     */
    public Boolean isCorrectOrder(Character value) {

        // keeps track of the current correct answer for returning. _answerOrder is going to
        //    be updated after to the next correct answer in the question bank
        _answer = _answerOrder;

        // check if activity is done
        if(!_isActivityDone) {
            Log.w(TAG, "answer is " + _answerOrder);
            if (value == _answerOrder) {

                // find value in list and then remove it from the possibilities
                Log.w(TAG, "updateQuestionBank: removed " + value + " from possible questions");
                _answerBank.remove(value);

                // make sure not to over extend bounds
                if (_answerBank.size() > 0) {
                    // set the answer for the next letter in the name
                    _answerOrder = (Character) _questionBank.get(_questionBank.indexOf(value) + 1);
                }
            } else {
                Log.w(TAG, "The value: " + value + " is NOT correct");
            }
        }

        // check if all questions have now been answered
        if (_answerBank.size() == 0) {
            _isActivityDone = true;
        }
        return (value == _answer);
    }

    /**
     * Method to build values that can be randomly pulled from.
     */
    protected void buildInitialQuestionAnswerBanks() {
        _answerBank  = new ArrayList<>();
        _questionBank = new ArrayList<>();

        // increment through letters
        for (int i = 0 ; i < _optionCount; i++) {
            _questionBank.add(_currentName.charAt(i));
            _answerBank.add(_currentName.charAt(i));
        }
    }

    /**
     * Will get the answer for the activity as a resource index.
     */
    public int getAnswerResourceIndex() {
        int resourceIndex = _context.getResources().getIdentifier("object_" + _answerOrder, "drawable", _context.getPackageName());
        return resourceIndex;
    }

    /**
     * Build a set of indexes required for retrieving audio and image files.
     */
    public List<MediaModel> generateValueList() {

        List<Character> randomValues;
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
        for (Character value : randomValues) {

            List<Integer> audioFileResourceIndexes = new ArrayList<>();
            int imageFileResourceIndex;

            //check if upper case letter is input
            char tempLetter = value;
            if (Character.isUpperCase(tempLetter)) {
               // get the image resource for name
               imageFileResourceIndex = _context.getResources().getIdentifier("upper_" + value.toString().toLowerCase(), "drawable", _context.getPackageName());

            } else { // lowercase
                // get the image resource for name
                imageFileResourceIndex = _context.getResources().getIdentifier("lower_" + value.toString().toLowerCase(), "drawable", _context.getPackageName());
            }

            // get the audio resource for name
            int audioFileResourceIndex1 = _context.getResources().getIdentifier(value.toString().toLowerCase(), "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex1);

            // letter sound
            int audioFileResourceIndex3 = _context.getResources().getIdentifier("letter_sound_" + value.toString().toLowerCase(), "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex3);

            // used to make the motivations different each time
            Collections.shuffle(correct);

            // used to make the motivations different each time
            Collections.shuffle(incorrect);

            // correct answer
            if (value == _answerOrder) {
                int audioFileResourceIndex2 = _context.getResources().getIdentifier(correct.get(0), "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            } else { // incorrect
                int audioFileResourceIndex2 = _context.getResources().getIdentifier(incorrect.get(0), "raw", _context.getPackageName());
                audioFileResourceIndexes.add(audioFileResourceIndex2);
            }

            // retrieve and associate buttons with image and audio
            MediaModel<Character> mediaModel = new MediaModel<>(imageFileResourceIndex, audioFileResourceIndexes, value);
            results.add(mediaModel);
        }

        return results;
    }
}