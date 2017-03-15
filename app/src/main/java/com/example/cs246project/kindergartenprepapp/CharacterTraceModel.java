package com.example.cs246project.kindergartenprepapp;

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

abstract public class CharacterTraceModel {

    // A bank of values in which the activity uses.
    protected List<String> _valueBank;

    // The current index in the _valueBank.
    protected int _currentValueIndex;

    /**
     * CharacterTraceModel
     * Non-Default Constructor generating the values and setting the index to the beginning.
     */
    public CharacterTraceModel() {
        generateValueBank();
        _currentValueIndex = 0;
    }

    /**
     * Generate Value Bank
     * Initializes _values with the appropriate values according to the activity's needs.
     */
    abstract protected void generateValueBank();

    /**
     * Get Current Values
     * Gets the current filename value(s) from _values;
     * @return a list of values (1 or more).
     */
    abstract public List<String> getCurrentValues();

    /**
     * Go To Next Value
     * Increments to the next value in _values if possible.
     */
    public void goToNextValue() {
        if ((_currentValueIndex + 1) < _valueBank.size()) {
            _currentValueIndex++;
        }
    }

    /**
     * Go To Previous Value
     * Decrements to the previous value in _values if possible.
     */
    public void goToPreviousValue() {
        if ((_currentValueIndex + 1) > 0) {
            _currentValueIndex--;
        }
    }

    /**
     * Is Complete
     * Is the activity complete (e.g. reached the end of the _values)?
     * @return a flag for whether or not the activity is complete.
     */
    public Boolean isComplete() {
        return (_currentValueIndex + 1) >= _valueBank.size();
    }

    /**
     * Is At Beginning
     * Is the activity at the beginning (e.g. _currentValueIndex = 0)?
     * @return a flag for whether or not the activity is at the beginning.
     */
    public Boolean isAtBeginning() {
        return _currentValueIndex == 0;
    }

}
