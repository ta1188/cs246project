package com.example.cs246project.kindergartenprepapp;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * The model class associated with a shape tracing activity which handles all of the logic of
 * building the image resource files and tracking values.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-03-09
 */

public class ShapeTraceableModel extends CharacterTraceableModel {

    public ShapeTraceableModel(Context context) {
        super(context);
    }

    /**
     * {@inheritDoc}
     * Initializes _valueBank with all the shape file names
     */
    @Override
    protected void generateValueBank() {
        _valueBank = new ArrayList<String>();
        _valueBank.add("circle");
        _valueBank.add("heart");
        _valueBank.add("hexagon");
        _valueBank.add("octagon");
        _valueBank.add("pentagon");
        _valueBank.add("rhombus");
        _valueBank.add("rectangle");
        _valueBank.add("square");
        _valueBank.add("star");
        _valueBank.add("triangle");
    }

    /**
     * {@inheritDoc}
     * Get Current Values
     * Gets the current filename value(s) from _values in upper and lower case forms;
     * @return a list of values (1 or more).
     */
    @Override
    public List<Integer> getCurrentValues() {
        List<Integer> values = new ArrayList<>();
        int value = _context.getResources().getIdentifier("shape_outline_" + _valueBank.get(_currentValueIndex), "drawable", _context.getPackageName());
        values.add(value);

        return values;
    }

    /**
     * {@inheritDoc}
     * Gets the file name fo the instructions audio raw file.
     * @return the file name of the instructions audio file
     */
    protected String getInstructionsFileName() {
        return "instruct_trace_the_shape_with_your_finger";
    }

    /**
     * {@inheritDoc}
     * Gets the audio resource index for the current value.
     * @return The audio index for the current value in _valueBank.
     */
    public int getCurrentValueAudioResourceIndex() {
        String audioFileName = "shape_" + _valueBank.get(_currentValueIndex);
        return _context.getResources().getIdentifier(audioFileName, "raw" , _context.getPackageName());
    }
}
