package com.example.cs246project.kindergartenprepapp;

import android.util.Log;
import java.util.ArrayList;

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

    /************ Constructors *************/

    /**
     * @param optionCount how many values are need to be generated
     */
    public WordSelectableModel(int optionCount) {

        if((optionCount > 0) && (optionCount <= 26)) {
        _optionCount = optionCount;
        _isActivityDone = false;
    }
        else {
        Log.i(TAG, "CountSelectableModel: 0 < option count <= 26; out of possible range");

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

        for (char i = 'a' ; i <= 'z'; i++) {
            _questionBank.add(i);
            _answerBank.add(i);
        }
    }
}