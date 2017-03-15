package com.example.cs246project.kindergartenprepapp;

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

public class NameTraceModel {

    protected String _name;

    /**
     * {@inheritDoc}
     * Creates a NameTraceModel using a name
     * @param name The name of the user
     */
    public NameTraceModel(String name) {
        _name = name;
    }

    /**
     * {@inheritDoc}
     * Gets the filename of each character in _values (e.g. characters in name);
     * @return a list of values (1 or more).
     */
    public List<String> getValues() {
        List<String> values = new ArrayList<String>();

        for (int i = 0; i < _name.length(); i++) {
            if (Character.isUpperCase(_name.charAt(i))) {
                values.add("upper_" + (Character.toString(_name.charAt(i))).toLowerCase());
            } else {
                values.add("lower_" + (Character.toString(_name.charAt(i))).toLowerCase());
            }
        }

        return values;
    }

    /**
     * {@inheritDoc}
     * Gets the length of the _name.
     * @return the number of characters in _name
     */
    public int getNumberOfCharacters() {
        return _name.length();
    }

    /**
     * {@inheritDoc}
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    protected String getInstructionsFileName() {
        return "";
    }
}
