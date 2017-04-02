package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * The common ancestor for all Tracing Models.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

abstract public class TraceableModel {

    // The context is used to get shared preferences and resource indexes
    Context _context;

    // Instruction audio file resource index
    protected int _instructionsAudioFileResourceIndex;

    // A bank of values in which the activity uses.
    protected List<String> _valueBank;

    // A list of drawn Paths corresponding to values in the _valueBank
    protected List<List<Path>> _valuePaths;

    // The current index in the _valueBank.
    protected int _currentValueIndex;


    /**
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    abstract protected String getInstructionsFileName();

    /**
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    public int getInstructionsAudioFileResourceIndex() {
        return _instructionsAudioFileResourceIndex;
    }

    /**
     * Initializes _values with the appropriate values according to the activity's needs.
     */
    protected void generateValueBank() {
        _valuePaths = new ArrayList<>();
        for (int i = 0; i < _valueBank.size(); i++) {
            _valuePaths.add(new ArrayList<Path>());
        }
    }

    /**
     * Gets the current filename value(s) from _values;
     * @return a list of values (1 or more).
     */
    abstract public List<Integer> getCurrentValues();

    /**
     * Gets the current path from for the current value;
     * @return a list of paths.
     */
    public List<Path> getCurrentValuePath() {
        return _valuePaths.get(_currentValueIndex);
    }

    /**
     *
     */
    public void setCurrentValuePaths(List<Path> paths) {
        _valuePaths.set(_currentValueIndex, paths);
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
