package com.example.cs246project.kindergartenprepapp;

import java.util.ArrayList;
import java.util.List;

/**
 * The model class associated with a letter tracing activity which handles all of the logic of
 * building the image resource files and tracking values.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-09
 */

public class LetterTraceModel extends CharacterTraceModel{

    /**
     * Generate Value Bank
     * Initializes _values with "a" - "z"
     */
    @Override
    protected void generateValueBank() {
        // Instantiate _values
        _valueBank = new ArrayList<String>();

        // Fill values with a - z using ascii values (97 = "a")
        for (int i = 97; i < (97 + 26); i++) {
            _valueBank.add((Character.valueOf((char) i)).toString());
        }
    }

    /**
     * Get Current Values
     * Gets the current filename value(s) from _values in upper and lower case forms;
     * @return a list of values (1 or more).
     */
    @Override
    public List<String> getCurrentValues() {
        List<String> values = new ArrayList<>();

        values.add("upper_" + _valueBank.get(_currentValueIndex));
        values.add("lower_" + _valueBank.get(_currentValueIndex));

        return values;
    }
}
