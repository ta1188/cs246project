package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

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

    /*public ArrayList<String> valuesToFileNames() {

        //Boolean isupper = Character.isUpperCase(myImages.get(0).charAt(0));

        // gather images for one file
        for (int i = 0; i < randomValues.size(); i++) {
            int indexImageFile = this.getResources().getIdentifier
                    (("number_" + Integer.toString((Integer) randomValues.get(i))),
                            "drawable", this.getPackageName());
            int indexAudioFile = this.getResources().getIdentifier
                    (("number_" + Integer.toString((Integer) randomValues.get(i))),
                            "raw", this.getPackageName());
            //randomValues
        }
        return null;
    }*/
}