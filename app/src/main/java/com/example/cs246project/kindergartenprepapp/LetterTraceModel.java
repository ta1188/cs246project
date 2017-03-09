package com.example.cs246project.kindergartenprepapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 3/8/2017.
 */

public class LetterTraceModel extends CharacterTraceModel{

    /**
     * Initializes _values with
     */
    @Override
    protected void generateValueBank() {
        // Instantiate _values
        _valueBank = new ArrayList<String>();

        // Fill values with A - Z using ascii values (65 = "A")
        for (int i = 97; i < (97 + 26); i++) {
            _valueBank.add((Character.valueOf((char) i)).toString());
        }
    }

    @Override
    public List<String> getCurrentValues() {
        List<String> values = new ArrayList<>();

        values.add("upper_" + _valueBank.get(_currentValueIndex));
        values.add("lower_" + _valueBank.get(_currentValueIndex));

        return values;
    }
}
