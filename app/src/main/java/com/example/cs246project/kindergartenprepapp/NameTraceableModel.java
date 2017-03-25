package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.SharedPreferences;

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

public class NameTraceableModel {

    protected String _firstName;
    protected String _lastName;

    protected Context _context;

    /**
     * {@inheritDoc}
     * Creates a NameTraceableModel using a name
     * @param context The Context in which the activity will be working in
     */
    public NameTraceableModel(Context context) {
        _context = context;

        SharedPreferences settings = context.getSharedPreferences(AppConstants.sharePreferenceSettings, context.MODE_PRIVATE);
        _firstName = settings.getString(AppConstants.sharePreferenceFirstName, "");
        _lastName = settings.getString(AppConstants.sharePreferenceLastName, "");
    }

    /**
     * {@inheritDoc}
     * Gets the filename of each character in _values (e.g. characters in name);
     * @return a list of values (1 or more).
     */
    public String getValues() {
        return _firstName + _lastName;
    }

    /**
     * {@inheritDoc}
     * Gets the length of the _name.
     * @return the number of characters in _name
     */
    public int getNumberOfCharacters() {
        if (!_lastName.isEmpty()) {
            return (_firstName.length() + _lastName.length());
        } else {
            return _firstName.length();
        }
    }

    /**
     * {@inheritDoc}
     * Gets the file name fo the instructions audio raw file
     * @return the file name of the instructions audio file
     */
    protected String getInstructionsFileName() {
        return "instruct_trace_letters_in_name_using_finger";
    }

    public int getCompletionAudioIndex() {
        return _context.getResources().getIdentifier("motivate_great_job", "raw", _context.getPackageName());
    }
}
