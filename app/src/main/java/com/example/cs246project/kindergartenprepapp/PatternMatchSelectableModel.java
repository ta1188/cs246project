package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Algorithm will have 4 possible pattern types that are equally likely to be chosen and will be
 * preformed a total of specified amount of times possible. For example each can be repeated with
 * random conditions 3 times making a total of 12 patterns before activity is over. The types are:
 * type 0 - one box with all shapes possible
 * type 1 - two boxes with all shapes possible in each box
 * type 2 - three boxes with all shapes possible in each box
 * type 3 - three boxes with all shapes possible in two boxes with the remaining box
 *          being replicated.
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-03-27
 */

public class PatternMatchSelectableModel extends SelectableModel {

    /* Member Variables */

    // static resource for word activity instruction
    private static final String _activityInstructions =
            // todo new instructions
            "instruct_pick_letter_that_matches_first_sound_of_picture";

    // for changing up the motivational messages
    static final List<String> correct = new ArrayList<String>(){{
        add("motivate_great_job");
        add("motivate_you_did_it");
        // TODO: 3/24/17 change to you found the pattern
        add("motivate_you_found_the_letter");
    }};

    // for changing up the motivational messages
    static final List<String> incorrect = new ArrayList<String>(){{
        add("motivate_try_again");
    }};

    int _activityRoundLength;
    protected PatternType _currentPatternType;
    protected List<String> _answerList;
    protected String _answerOrder;


    private class PatternType {

        protected int _patternLength;
        protected int _completeQuestionPatternLength;
        protected int _patternVariation;
        protected Boolean _isPatternQuestionDone;

        public PatternType(int patternLength, int patternVariation) {
            _patternLength = patternLength;
            _patternVariation = patternVariation;
            _completeQuestionPatternLength = patternLength + patternLength + patternLength;
            _isPatternQuestionDone = false;
        }
    }

    /* Constructors */

    public PatternMatchSelectableModel(Context context, int activityRoundLength) {
        super(context);

            _activityRoundLength = activityRoundLength;

            _isActivityDone = false;

            // initialize question bank
            buildInitialQuestionAnswerBanks();
    }


    /* METHODS */

    /**
     *  Gets the instruction audio resource index for the word selectable activity.
     *  @return The audio resource index for the activity instruction.
     */
    public int getActivityInstructionsIndex() {

        return _context.getResources().getIdentifier(_activityInstructions, "raw", _context.getPackageName());
    }

    public int getAnswerAudioIndex(Boolean isCorrect) {
        Collections.shuffle(correct);
        return isCorrect ? _context.getResources().getIdentifier(correct.get(0), "raw", _context.getPackageName()) : _context.getResources().getIdentifier(incorrect.get(0), "raw", _context.getPackageName());
    }

    /**
     * Build values that can be randomly pulled from.
     */
    protected void buildInitialQuestionAnswerBanks() {
        _answerBank = new ArrayList();

        // _answerBank is a pattern choice question bank
        //type 0 - one box with all shapes possible
        //type 1 - two boxes with all shapes possible in each box
        //type 2 - three boxes with all shapes possible in each box
        //type 3 - three boxes with all shapes possible in two boxes with the remaining box
        // parameters are pattern length, pattern variation, times can be used.
        for (int i = 1 ; i <= 4; i++) {
            if (i == 4) {

                // pattern type 3 is slightly different and is not 4 long but 3
                _answerBank.add(new PatternType((i - 1), (i - 1)));
            }
            else {
                _answerBank.add(new PatternType(i, (i - 1)));
            }
        }

        _questionBank = new ArrayList<String>() {{
            add("shape_circle");
            add("shape_rhombus");
            add("shape_heart");
            add("shape_hexagon");
            add("shape_octagon");
            add("shape_pentagon");
            add("shape_rectangle");
            add("shape_square");
            add("shape_star");
            add("shape_triangle");
            for (char i = 'a' ; i <= 'z'; i++) {
                add("object_" + i);
            }
        }};
    }

    /**
     * Will double the value of the pattern for repetition
     * @param shortObjectValueList
     * @return
     */
    private List<String> doubleQuestionPattern(List<String> shortObjectValueList) {
        List<String> expandedObjectValueList = new ArrayList<>(shortObjectValueList);

        for (String value : shortObjectValueList) {
            expandedObjectValueList.add(value);
        }

        return expandedObjectValueList;
    }

    /**
     * Get the answer for the activity as a resource indexes for the full length of the given
     * pattern which is 2 times of the answer.
     */
    public List<Integer> getPatternImageAnswerResourceIndex() {

        // double the length of the answer pattern
        List<String> temp = new ArrayList<>(doubleQuestionPattern(_answerList));
        List<Integer> resourceIndexes = new ArrayList<>();

        for (String value : temp ) {
            resourceIndexes.add(_context.getResources().getIdentifier(value, "drawable", _context.getPackageName()));
        }
        return resourceIndexes;
    }

    public List<Integer> getPatternAudioAnswerResourceIndex() {

        // double the length of the answer pattern
        List<String> temp = new ArrayList<>(doubleQuestionPattern(_answerList));
        List<Integer> resourceIndexes = new ArrayList<>();

        for (String value : temp ) {
            // retrieve object audio
            resourceIndexes.add(_context.getResources().getIdentifier(value, "raw", _context.getPackageName()));
        }
        return resourceIndexes;
    }

    /**
     *
     * @param value
     * @return
     */
    public Boolean isCorrectOrder(String value) {

        // keeps track of the current correct answer for returning. _answerOrder is going to
        //    be updated after to the next correct answer in the question bank
        _answer = _answerOrder;

        // check if activity is done and if the question activity is done.
        if(!_isActivityDone && !_currentPatternType._isPatternQuestionDone) {
            Log.w(TAG, "answer is " + _answerOrder);
            if (value == _answerOrder) {

                // remove the value from the possibilities where because the value is in order
                //    the first item in the list will be the correct answer
                Log.w(TAG, "updateQuestionBank: removed " + value + " from possible questions");
                _answerList.remove(0);

                // make sure not to over extend bounds
                if (_answerList.size() > 0) {
                    // set the answer for the next letter in the name which will be the first value
                    //    in the array because the correct answer was just removed from that spot.
                    _answerOrder = (String) _answerList.get(0);
                }
            } else {

                Log.w(TAG, "The value: " + value + " is NOT correct");
            }
        }

        // check if all questions have now been answered
        if (_answerList.size() == 0) {
            // grab the current pattern type from the question bank and find out how many times it has
            // been used. If over 3 times then set to null so it can not be used in the selection any more
            _currentPatternType._isPatternQuestionDone = true;

            // one round is complete
            _activityRoundLength--;

            if (_activityRoundLength == 0) {
                _isActivityDone = true;
            }

        }
        return (value == _answer);
    }

    /**
     * Tells if the activity is over and all values in the question bank have been used
     */
    public Boolean isPatternQuestionDone() {

        return _currentPatternType._isPatternQuestionDone;
    }

    public int getCurrentPatternLengh() {
        return _currentPatternType._patternLength;
    }

    /**
     * Tells if the activity is over and all values in the question bank have been used
     */
    public Boolean isActivityDone() { return _isActivityDone; }

    /**
     * Grabs a random pattern type
     * @return
     */
    private PatternType generateRandomPatternType() {
        Random randomPatternTypeRetriever = new Random();

        // grab a pattern type randomly
        return (PatternType) _answerBank.get(randomPatternTypeRetriever.nextInt(_answerBank.size()));
    }

    /**
     * Build a set of indexes required for retrieving audio and image files
     */
    @Override
    public List<MediaModel> generateValueList() {

        List<String> randomObjectValueList;
        List<MediaModel> questionsAndAnswerOptions = new ArrayList<>();

        // make sure the activity is not over because all values have been selected correctly
        //    otherwise would result in endless loop in random activity
        if (!_isActivityDone) {

            // get from available pattern types a random pattern this will be used by
            //    randomValueGenerator
            _currentPatternType = generateRandomPatternType();

            // get random values with shapes/objects as parameters in the specified number
            //    according to pattern type length
            randomObjectValueList = randomValuesGenerator();
        }
        else {
            Log.w(TAG, "generateButtonList: able to generate random values " +
                    _answerBank.size() + " > 0");
            return null;
        }

        // shuffle list to make it random
        Collections.shuffle(randomObjectValueList);

        // Using the random values now associate the images and sounds to buttons to be used
        //    by the calling activity
        for (String value : randomObjectValueList) {

            // set the picture to match the object sound
            int imageFileResourceIndex = _context.getResources().getIdentifier(value, "drawable", _context.getPackageName());
            List<Integer> audioFileResourceIndexes = new ArrayList<>();

            // retrieve object audio
            int audioFileResourceIndex1 = _context.getResources().getIdentifier(value, "raw", _context.getPackageName());
            audioFileResourceIndexes.add(audioFileResourceIndex1);

            // retrieve and associate buttons with image and audio
            MediaModel<String> mediaModel = new MediaModel<>(imageFileResourceIndex, audioFileResourceIndexes, value);
            questionsAndAnswerOptions.add(mediaModel);
        }

        return questionsAndAnswerOptions;
    }

    /**
     * Generate an array of random values to be used for the buttons and question set up.
     * @return set random random values to be used for media association and buttons
     */
    @Override
    protected List<String> randomValuesGenerator() {

        List<String> valueList = new ArrayList<>();
        Random randomValueRetriever = new Random();

        // for type 3 - three boxes with all shapes possible in two boxes with the remaining box
        if (_currentPatternType._patternVariation == 3) {

            // get random value
            String randomObject = (String) _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

            // add the first random value to the list and based on what it is duplicate left or right randomly
            //    and fill the last in with a different value.
            valueList.add(randomObject);

            // random left or right choice
            if (randomValueRetriever.nextBoolean()) {
                valueList.add(randomObject); // add to end duplicate
                while (valueList.size() < _currentPatternType._patternLength) {
                    randomObject = (String) _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

                    // check if number already added, not added if -1 is returned from check
                    if (valueList.indexOf(randomObject) == -1) {
                        valueList.add(0, randomObject); // add to front
                    }
                }
            } else {  // every other pattern type will be randomly created
                valueList.add(0, randomObject); // add to front duplicate
                while (valueList.size() < _currentPatternType._patternLength) {
                    randomObject = (String) _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

                    // check if number already added, not added if -1 is returned from check
                    if (valueList.indexOf(randomObject) == -1) {
                        valueList.add(randomObject); // add to end
                    }
                }
            }
        } else {  // for all other pattern types

            while (valueList.size() < _currentPatternType._patternLength) {

                // get random value
                String randomObject = (String) _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

                // add the random to the list
                valueList.add(randomObject);
            }
        }

        // store the answer list for question generation
        _answerList = new ArrayList<>(valueList);
        _answerOrder = _answerList.get(0);

        // add random values to make a total of 2 extra options
        while (valueList.size() < (_currentPatternType._patternLength + 2)) {

            // get random value
            String randomObject = (String) _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

            // check if number already added, not added if -1 is returned
            if (valueList.indexOf(randomObject) == -1)
                valueList.add(randomObject);
        }

        return valueList;
    }
}
