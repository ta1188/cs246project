package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.graphics.Path;

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

abstract public class CharacterTraceableModel extends TraceableModel {

    /**
     * Non-Default Constructor generating the values and setting the index to the beginning.
     * @param context The Context in which the activity will be working in
     */
    public CharacterTraceableModel(Context context) {
        _context = context;
        generateValueBank();
        _currentValueIndex = 0;
        _instructionsAudioFileResourceIndex = context.getResources().getIdentifier(getInstructionsFileName(), "raw", _context.getPackageName());
    }

}
