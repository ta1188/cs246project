package com.example.cs246project.kindergartenprepapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-03-09
 */

/**
 * SELECTABLEMODEL: Class will build a list of random values used with the selectable activities.
 * The class will create the specified number of values of which will have the following
 * properties: they are unique with no duplicates, options are reduced as the user gets a
 * question right, The first generated value is assigned to be the answer. The class will
 * have a function that will return a list of value back.
 */
abstract class SelectableModel<T> extends Application {

    /* Member Variables */

    public Context _context;
    protected List<T> _questionBank;
    protected List<T> _answerBank;
    protected T _answer;
    protected Boolean _isActivityDone;
    protected int _optionCount;

    private static final String TAG = "SelectableModel";

    public SelectableModel (Context context){
        _context = context;
        _isActivityDone = false;
    }

    /* METHODS */

    /**
     * ISCORRECT will tell if the value is the correct answer and update the question bank if
     * already correct
     * @param value what has been selected by the user
     */
    public Boolean isCorrect(T value) {

        if(!_isActivityDone) {
            Log.w(TAG, "answer is " + _answer);
            if (String.valueOf(value) == String.valueOf(_answer)) {
                // find value in list and then remove it from the possibilities
                Log.w(TAG, "updateQuestionBank: removed " + value + " from possible questions");
                _answerBank.remove(value);
            } else {
                Log.w(TAG, "The value: " + value + " is NOT correct");
            }
        }

        // check if all questions have now been answered
        if (_answerBank.size() == 0) {
            _isActivityDone = true;
        }
        return (String.valueOf(value) == String.valueOf(_answer));
    }

    /**
     * ISACTIVITYDONE will tell if the activity is over and all values in the question
     */
    public Boolean isActivityDone() { return _isActivityDone; }

    /**
     * GETANSWERRESOURCEINDEX will get the answer for the activity as a resource index
     */
    int getAnswerResoureIndex(String filePrefix) {
        int resourceIndex = _context.getResources().getIdentifier(filePrefix + _answer, "drawable", _context.getPackageName());
        return resourceIndex;
    }

    /**
     * GETPROGRESS will tell if the activity is over
     */
    public int getProgress() {
        return _answerBank.size();
    }

    /**
     * BUILDINITIALQUESTIONANSWERBANKS will build a question bank
     */
    abstract protected void buildInitialQuestionAnswerBanks();


    /**
     * RANDOMBUTTONGENERATOR will generate an array of random values to be used for the buttons.
     * Will be unique values that are not used again if already answered
     */
    protected List<T> randomValuesGenerator() {

        List<T> valueList = new ArrayList<>();
        Random randomValueRetriever = new Random();

        // get random first value with conditions based on available answer bank questions
        _answer = _answerBank.get(randomValueRetriever.nextInt(_answerBank.size()));

        // check if random number already exists where -1 means it doesn't exists
        valueList.add(_answer);

        // generate the rest of the buttons with random values that don't match button 1st
        //    button made
        while (valueList.size() < _optionCount) {

            // get random value
            T randomNum = _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

            // check if number already added, not added if -1 is returned
            if (valueList.indexOf(randomNum) == -1)
                valueList.add(randomNum);
        }

        return valueList;
    }


    public String getAnswer() {
        return  String.valueOf(_answer);
    }
}
