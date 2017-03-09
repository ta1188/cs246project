package com.example.cs246project.kindergartenprepapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dan on 3/8/2017.
 */

abstract public class CharacterTraceModel {

    protected List<String> _valueBank;
    protected int _currentValueIndex;

    /**
     * Non-Default Constructor
     */
    public CharacterTraceModel() {
        generateValueBank();
        _currentValueIndex = 0;
    }

    /**
     * Initializes _values with
     */
    abstract protected void generateValueBank();

    abstract public List<String> getCurrentValues();

    public void goToNextValue() {
        if ((_currentValueIndex + 1) < _valueBank.size()) {
            _currentValueIndex++;
        }
    }

    public Boolean isComplete() {
        return (_currentValueIndex + 1) >= _valueBank.size();
    }

}
