package com.example.cs246project.kindergartenprepapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-03-09
 */

/**
 * Class will build a list of random values used with the selectable activities.
 * The class will create the specified number of values of which will have the following
 * properties: they are unique with no duplicates, options are reduced as the user gets a
 * question right, The first generated value is assigned to be the answer. The class will
 * have a function that will return a list of value back.
 * @param <T> will support additional types per activity called.
 */
abstract class SelectableModel<T> extends Application {

    /* MEMBER VARIABLES */

    public Context _context;
    protected List<T> _questionBank;
    protected List<T> _answerBank;
    protected T _answer;
    //protected T _answerIndex;
    protected Boolean _isActivityDone;
    protected int _optionCount;

    private static final String TAG = "SelectableModel";

    /* CONSTRUCTOR */

    public SelectableModel (Context context){
        _context = context;
        _isActivityDone = false;
    }

    /* METHODS */

    /**
     * Tells if the value is the correct answer and update the question bank if
     * already correct
     * @param value what has been selected by the user
     * @return Compares the answer with value selected to see if correct
     */
    public Boolean isCorrect(T value) {

        if(!_isActivityDone) {
            Log.w(TAG, "answer is " + _answer);
            if (value == _answer) {
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
        return (value == _answer);
    }

    /**
     * Tells if the activity is over and all values in the question bank have been used
     */
    public Boolean isActivityDone() { return _isActivityDone; }

    /**
     * Tells if the activity is over
     * @return question bank current size
     */
    public int getProgress() {
        return _answerBank.size();
    }

    /**
     * Handle toast messages
     * */
    public void displayToast(boolean correctAnswer) {
        CharSequence text;
        String toastColor;

        if (correctAnswer) {
            text = "Correct!";
            toastColor = "#00e676";
        } else {
            text = "Incorrect!";
            toastColor = "#ff8a65";
        }


        int duration = Toast.LENGTH_SHORT;

        final Toast toast = Toast.makeText(_context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(25);
        View view = toast.getView();
        view.setBackgroundColor(Color.parseColor(toastColor));
        view.setPadding(20, 10, 20, 10);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown = new CountDownTimer(800, 1000 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
                toast.show();
            }
            public void onFinish() {
                toast.cancel();
            }
        };

        // Show the toast and starts the countdown
        toast.show();
        toastCountDown.start();
    }


    /**
     * abstract requires building of a question bank
     */
    abstract protected void buildInitialQuestionAnswerBanks();

//    /**
//     *  Gets the instruction audio resource index for the activity.
//     *  @return The audio resource index for the activity instruction.
//     */
//    abstract public int getActivityInstructionsIndex();


    /**
     * Generate an array of random values to be used for the buttons.
     * Values are unique that are not used again if already answered.
     * @return set random random values to be used for media association and buttons
     */
    protected List<T> randomValuesGenerator() {

        List<T> valueList = new ArrayList<>();
        Random randomValueRetriever = new Random();

        // get random first value with conditions based on available answer bank questions
        _answer = _answerBank.get(randomValueRetriever.nextInt(_answerBank.size()));

        // add the initial random value to list to start
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

    public int getNameLength() {
        return  _optionCount;
    }
}
