package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Button with sound and image
 * <p>
 * @author  Michael Lucero
 * @version 1.0
 * @since   2017-03-07
 */

/**
 * MEDIABUTTONLISTFACTORY: This class will build buttons for use with the button activities.
 * A button assignment consists of two things. A Button will have required audio associated
 * with it and image when calling an creating buttons. The class will create the specified
 * number of buttons of which will have the following properties: they are unique with no
 * duplicates, The first generated button is assigned to be the answer.
 * The class will have a function that will return the array of buttons back where the buttons
 * stand on there own. Will check the passed in question bank before new buttons are created.
 */
public class MediaButtonListFactory {

    // create the question bank so knows what values to choose from
    private List<Integer> _questionBank = new ArrayList<>();

    private String _activityType;
    private Context _context;


    // constructor sets up a button array
    public MediaButtonListFactory(String activityType, Context context) {

        _activityType = activityType;
        _context = context;

        // initialize question Bank
        buildInitialQuestionBank();
    }

    /**
     * BUILDINITIALQUESTIONBANK Defined question bank based on the requested activity
     */
    private void buildInitialQuestionBank()  {
        // create a randomized list
        switch (_activityType) {
            // create numbers 0-10. will also have index 1-10
            case "number" :
                for (int i = 0 ; i <= 10; i++) {
                    _questionBank.add(i);
                }
                break;
            // create a-z and also can be used for letter sounds. This will also have index 0-25
            case "letter" :
                for (int i = 'a' ; i <= 'z'; i++) {
                    _questionBank.add(i);
                }
                break;
            // letter sound activity
            case "letterSound" :
                for (int i = 'a' ; i <= 'z'; i++) {
                    _questionBank.add(i);
                }
                break;
            case "name" :
                // get name from shared preferences
                SharedPreferences sharedPreferences = _context.getSharedPreferences("SETTINGS", MODE_PRIVATE);
                String usersName = sharedPreferences.getString("NAME", "");

                // increment through letters
                for (int i = 0 ; i < usersName.length(); i++) {
                    _questionBank.add((int) usersName.charAt(i));
                }
                break;
            default:
                IOException exception = new IOException("No Activity Type Specified");
                try {
                    throw exception;
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }


    /**
     * Will generate buttons of two ways; one with a limitation of not including the already
     * answered questions, and the rest with all the available question options.
     * @param answerExclusions list specifying the values already answered correctly
     * @param count how many buttons are needed
     */
    public List<MediaButton> generateButtonList(List<Integer> answerExclusions, int count) {

        // create the question bank so knows what values to choose from
        List<MediaButton> buttonList = new ArrayList<>();
        List<Integer> randomButtonValues;

        // make sure the activity is not over because all values have been selected correctly
        //    otherwise would result in endless loop
        if (_questionBank.size() != answerExclusions.size())
            randomButtonValues = randomAnswerButtonGenerator(answerExclusions, count);
        else {
            IOException exception = new IOException("This activity is over");
            try {
                throw exception;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // finally make buttons with associated values
        for (int i = 0 ; i < count ; i++) {
            if(i == 0)
                buttonList.add(new MediaButton(_activityType, _context, randomButtonValues.get(i), true));
            else
                buttonList.add(new MediaButton(_activityType, _context, randomButtonValues.get(i), false));
        }

        // shuffle list to make is random
        Collections.shuffle(buttonList);

        return buttonList;
    }



    /**
     * RANDOMBUTTONGENERATOR will generate an array of random values to be used for the buttons.
     * Will be unique values that are not used again if already answered.
     * @param answerExclusions list specifying the values already answered correctly
     * @param count how many buttons are needed
     */
    private List<Integer> randomAnswerButtonGenerator(List<Integer> answerExclusions, int count) {

        List<Integer> buttonValues = new ArrayList<>();
        Random randomValueRetriever = new Random();
        Integer answerNumber;

        // set the first value with conditions based on already answered questions
        do {

            // get random value
            answerNumber = _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

            // check if random number already exists where -1 means it doesn't exists
            if (answerExclusions.indexOf(answerNumber) == -1)
                buttonValues.add(answerNumber);

            // once the right size array has been made then we have a randomized list
        } while (buttonValues.size() < 1);

        // generate the rest of the buttons with random values that don't match button 1st
        //    button made
        do {

            // get random value
            Integer randomNum = _questionBank.get(randomValueRetriever.nextInt(_questionBank.size()));

            // check if number already added, not added if -1 is returned
            if (buttonValues.indexOf(randomNum) == -1)
                buttonValues.add(randomNum);

            // once the right size array has been made then we have a randomized list
        } while (buttonValues.size() < count);

        return buttonValues;
    }
}
