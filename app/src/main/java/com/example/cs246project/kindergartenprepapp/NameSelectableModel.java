package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    // static resource for word activity instruction
    private static final String _activityInstructions = "instruct_spell_your_name";

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

                    // set the initial answer as the first letter of the name
                    _answerOrder = _currentName.charAt(0);

                    // initialize question bank
                    buildInitialQuestionAnswerBanks();

                } else {
                    Log.i(TAG, "NameSelectableModel: No first name entered");
                    _isActivityDone = true;
                }

                break;
            case "last" :
                if(_lastName.length() > 0) {
                    _optionCount = _lastName.length();
                    _currentName = _lastName;
                    _isActivityDone = false;

                    // set the initial answer as the first letter of the name
                    _answerOrder = _currentName.charAt(0);

                    // initialize question bank
                    buildInitialQuestionAnswerBanks();
                } else {
                    Log.i(TAG, "NameSelectableModel: No last name entered ");
                    _isActivityDone = true;
                }
                break;
            default :
                _isActivityDone = true;
        }
    }

    /* METHODS */

    public Boolean hasFirstName() {
        return (_firstName.length() > 0);
    }

    public Boolean hasLastName() {
        return (_lastName.length() > 0);
    }

    /**
     *  Gets the instruction audio resource index for the word selectable activity.
     *  @return The audio resource index for the activity instruction.
     */
    public int getActivityInstructionsIndex() {

        return _context.getResources().getIdentifier(_activityInstructions, "raw", _context.getPackageName());
    }

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

                // remove the value from the possibilities where because the value is in order
                //    the first item in the list will be the correct answer
                Log.w(TAG, "updateQuestionBank: removed " + value + " from possible questions");
                _answerBank.remove(0);

                // make sure not to over extend bounds
                if (_answerBank.size() > 0) {
                    // set the answer for the next letter in the name which will be the first value
                    //    in the array because the correct answer was just removed from that spot.
                    _answerOrder = (Character) _answerBank.get(0);
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

    public int getAnswerAudioIndex(Boolean isCorrect) {
        Collections.shuffle(correct);
        return isCorrect ? _context.getResources().getIdentifier(correct.get(0), "raw", _context.getPackageName()) : _context.getResources().getIdentifier(incorrect.get(0), "raw", _context.getPackageName());
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
     * Build a set of indexes required for retrieving audio and image files.
     */
    public List<MediaModel> generateValueList() {

        List<Character> randomValues;
        List<MediaModel> questionsAndAnswer = new ArrayList<>();

        // make sure the activity is not over because all values have been selected correctly
        //    otherwise would result in endless loop in random activity
        if (!_isActivityDone) {
            // get random values with its parameters
            randomValues = randomValuesGenerator();
        }
        else {
            Log.w(TAG, "generateButtonList: able to generate random values " +
                    "_answerBank.size() < 1");

            return questionsAndAnswer;
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

            // retrieve and associate buttons with image and audio
            MediaModel<Character> mediaModel = new MediaModel<>(imageFileResourceIndex, audioFileResourceIndexes, value);
            questionsAndAnswer.add(mediaModel);
        }

        return questionsAndAnswer;
    }

    /**
     * Generate an array of random values to be used for the buttons.
     * Values are unique that are not used again if already answered.
     * @return set random random values to be used for media association and buttons
     */
    @Override
    protected List<Character> randomValuesGenerator() {

        List<Character> valueList = new ArrayList<>();
        Random randomValueRetriever = new Random();

        // generate the rest of the buttons with random values that don't match button 1st
        //    button made
        while (valueList.size() < _optionCount) {

            // get random value
            Character randomNum = (Character) _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

            // value has now been used so don't use it again
            _questionBank.remove(randomNum);

            // add the random to the list to use latter
            valueList.add(randomNum);
        }

        return valueList;
    }

}