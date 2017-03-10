package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;

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


    /************ Constructors *************/

    public NameSelectableModel(Context context) {
        super(context);
        _isActivityDone = false;

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

        // get name from shared preferences
        SharedPreferences sharedPreferences = _context.getSharedPreferences("SETTINGS", MODE_PRIVATE);
        String usersName = sharedPreferences.getString("NAME", "");

        _optionCount = usersName.length();

        // increment through letters
        for (char i = 0 ; i < usersName.length(); i++) {
            _questionBank.add((int) usersName.charAt(i));
        }
    }
}