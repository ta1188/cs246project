package com.example.cs246project.kindergartenprepapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract class associated with character tracing activities which handles all of the logic of
 * building the image resource files and tracking values.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-09
 */

abstract public class CharacterTraceableModel {

    Context _context;

    // Instruction audio file resource index
    protected int _instructionsAudioFileResouceIndex;

    // A bank of values in which the activity uses.
    protected List<String> _valueBank;

    // The current index in the _valueBank.
    protected int _currentValueIndex;

    /**
     * Non-Default Constructor generating the values and setting the index to the beginning.
     * @param context The Context in which the activity will be working in
     */
    public CharacterTraceableModel(Context context) {
        _context = context;
        generateValueBank();
        _currentValueIndex = 0;
        _instructionsAudioFileResouceIndex = context.getResources().getIdentifier(getInstructionsFileName(), "raw", _context.getPackageName());
    }

    /**
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    abstract protected String getInstructionsFileName();

    /**
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    public int getInstructionsAudioFileResouceIndex() {
        return _instructionsAudioFileResouceIndex;
    }

    /**
     * Initializes _values with the appropriate values according to the activity's needs.
     */
    abstract protected void generateValueBank();

    /**
     * Gets the current filename value(s) from _values;
     * @return a list of values (1 or more).
     */
    abstract public List<String> getCurrentValues();

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
     * Gets the resource index of the raw audio file of the current value in the _valueBank.
     * @return The resource index of the raw audio file of the current value in the _valueBank
     */
    abstract public int getCurrentValueAudioResourceIndex();

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
     * Is the next value in the _valueBank the last value in the valueBank?
     * @return a flag for whether or not the next value is the last.
     */
    public Boolean isNextValueEnd() {
        return (_currentValueIndex + 2) >= _valueBank.size();
    }

    //isPreviousValueStart
    /**
     * Is the previous value in the _valueBank the first value in the valueBank?
     * @return a flag for whether or not the previous value is the fist.
     */
    public Boolean isPreviousValueStart() {
        return (_currentValueIndex - 1) == 0;
    }

    public int getCompletionAudioIndex() {
        return _context.getResources().getIdentifier("motivate_great_job", "raw", _context.getPackageName());
    }
}
