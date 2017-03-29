package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * The model class associated with a name tracing activity which handles all of the logic of
 * building the image resource files and tracking values.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-09
 */

public class SightWordTraceableModel {

    // A bank of values in which the activity uses.
    protected List<String> _valueBank;

    // The current index in the _valueBank.
    protected int _currentValueIndex;

    protected Context _context;

    /**
     * {@inheritDoc}
     * Creates a NameTraceableModel using a name
     * @param context The Context in which the activity will be working in
     */
    public SightWordTraceableModel(Context context) {
        _context = context;

        _valueBank = new ArrayList<String>();
        _valueBank.add("the");
        _valueBank.add("a");
        _valueBank.add("for");
        _valueBank.add("of");
        _valueBank.add("as");
        _valueBank.add("and");
        _valueBank.add("are");
        _valueBank.add("is");
        _valueBank.add("was");
        _valueBank.add("be");
        _valueBank.add("at");
        _valueBank.add("to");
        _valueBank.add("in");
        _valueBank.add("on");
        _valueBank.add("he");
        _valueBank.add("she");
        _valueBank.add("it");
        _valueBank.add("his");
        _valueBank.add("her");
        _valueBank.add("I");
        _valueBank.add("me");
        _valueBank.add("you");
    }

    /**
     * Increments to the next value in _values if possible.
     */
    public void goToNextValue() {
        if ((_currentValueIndex + 1) < _valueBank.size()) {
            _currentValueIndex++;
        }
    }

    /**
     * Decrements to the previous value in _values if possible.
     */
    public void goToPreviousValue() {
        if ((_currentValueIndex + 1) > 0) {
            _currentValueIndex--;
        }
    }

    /**
     * Is the activity complete (e.g. reached the end of the _values)?
     * @return a flag for whether or not the activity is complete.
     */
    public Boolean isComplete() {
        return (_currentValueIndex + 1) >= _valueBank.size();
    }

    /**
     * Is the activity at the beginning (e.g. _currentValueIndex = 0)?
     * @return a flag for whether or not the activity is at the beginning.
     */
    public Boolean isAtBeginning() {
        return _currentValueIndex == 0;
    }

    /**
     * {@inheritDoc}
     * Gets the filename of each character in _values (e.g. characters in name);
     * @return a list of values (1 or more).
     */
    public List<Integer> getCurrentValues() {
        List<Integer> result = new ArrayList<>();

        for(int i = 0; i < _valueBank.get(_currentValueIndex).length(); i++) {
            String filename = "lower_" + (Character)_valueBank.get(_currentValueIndex).charAt(i);
            result.add(_context.getResources().getIdentifier(filename, "drawable", _context.getPackageName()));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * Gets the length of the _name.
     * @return the number of characters in _name
     */
    public int getNumberOfCharacters() {
        return _valueBank.get(_currentValueIndex).length();
    }

    /**
     * {@inheritDoc}
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    protected String getInstructionsFileName() {
        return "instruct_trace_letters_in_name_using_finger";
    }

    public int getCompletionAudioIndex() {
        return _context.getResources().getIdentifier("motivate_great_job", "raw", _context.getPackageName());
    }
}
