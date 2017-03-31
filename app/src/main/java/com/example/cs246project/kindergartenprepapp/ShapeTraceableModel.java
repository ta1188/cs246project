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
        _valueBank.add("shape_outline_circle");
        _valueBank.add("shape_outline_rhombus");
        _valueBank.add("shape_outline_heart");
        _valueBank.add("shape_outline_hexagon");
        _valueBank.add("shape_outline_octagon");
        _valueBank.add("shape_outline_pentagon");
        _valueBank.add("shape_outline_rectangle");
        _valueBank.add("shape_outline_square");
        _valueBank.add("shape_outline_star");
        _valueBank.add("shape_outline_triangle");
    }

    /**
     * {@inheritDoc}
     * Get Current Values
     * Gets the current filename value(s) from _values in upper and lower case forms;
     * @return a list of values (1 or more).
     */
    @Override
    public List<String> getCurrentValues() {
        List<String> values = new ArrayList<>();

        values.add(_valueBank.get(_currentValueIndex));

        return values;
    }

    /**
     * {@inheritDoc}
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    protected String getInstructionsFileName() {
        return "instruct_trace_letter_with_finger";
    }

    public int getCurrentValueAudioResourceIndex() {
        String audioFileName = "a"; // TODO: CHANGE THIS!!! //_valueBank.get(_currentValueIndex);
        return _context.getResources().getIdentifier(audioFileName, "raw" , _context.getPackageName());
    }
}
