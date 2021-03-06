package com.example.cs246project.kindergartenprepapp;

import android.content.Context;

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

public class NumberTraceableModel extends CharacterTraceableModel {


    public NumberTraceableModel(Context context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     * Generate Value Bank
     * Initializes _values with 0 - 10
     */
    @Override
    protected void generateValueBank() {
        // Instantiate _values
        _valueBank = new ArrayList<String>();

        // Fill values with 0 - 9 using ascii values (48 = "0")
        for (int i = 48; i < (48 + 10); i++) {
            _valueBank.add((Character.valueOf((char) i)).toString());
        }
        _valueBank.add("10");

        super.generateValueBank();
    }

    /**
     * {@inheritDoc}
     * Gets the current filename value(s) from _values;
     * @return a list of values (1 or more).
     */
    public List<Integer> getCurrentValues() {
        List<Integer> values = new ArrayList<>();
        int value = _context.getResources().getIdentifier("number_" + _valueBank.get(_currentValueIndex), "drawable", _context.getPackageName());
        values.add(value);

        return values;
    }

    /**
     * {@inheritDoc}
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    protected String getInstructionsFileName() {
        return "instruct_trace_number_with_finger";
    }

    /**
     * Gets the current value's audio resource index.
     * @return the audio index of the current value in the _valueBank.
     */
    public int getCurrentValueAudioResourceIndex() {
        String audioFileName = "number_" + _valueBank.get(_currentValueIndex);
        return _context.getResources().getIdentifier(audioFileName, "raw" , _context.getPackageName());
    }
}
