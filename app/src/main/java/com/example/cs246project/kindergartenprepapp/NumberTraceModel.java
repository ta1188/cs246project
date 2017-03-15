package com.example.cs246project.kindergartenprepapp;

import java.util.ArrayList;
import java.util.List;

/**
 * The model class associated with a number tracing activity which handles all of the logic of
 * building the image resource files and tracking values.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-09
 */

public class NumberTraceModel extends CharacterTraceModel {

    /**
     * Generate Value Bank
     * Initializes _values with 1 - 10
     */
    @Override
    protected void generateValueBank() {
        // Instantiate _values
        _valueBank = new ArrayList<String>();

        // Fill values with 1 - 9 using ascii values (49 = "1")
        for (int i = 49; i < (48 + 10); i++) {
            _valueBank.add((Character.valueOf((char) i)).toString());
        }
        _valueBank.add("10");
    }

    /**
     * Get Current Values
     * Gets the current filename value(s) from _values;
     * @return a list of values (1 or more).
     */
    public List<String> getCurrentValues() {
        List<String> values = new ArrayList<>();

        values.add("number_" + _valueBank.get(_currentValueIndex));

        return values;
    }
}
