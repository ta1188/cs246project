package com.example.cs246project.kindergartenprepapp;

/**
 * A common ancestor for selectable activities.
 * <p>
 * @author  Dan Rix
 * @version 1.0
 * @since   2017-02-20
 */

public abstract class SelectableActivity extends SkipTapActivity {

    @Override
    protected String getInstructionToastText() {
        return "Tap a Button";
    }


}
