package com.example.cs246project.kindergartenprepapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
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
 * MEDIABUTTONGROUPFACTORY: This class will build buttons for use with the button activities.
 * A button assignment consists of two things. A Button will have required audio associated
 * with it and image. The class will create the specified number of buttons of which will
 * have the following properties: they are unique with no duplicates, options are reduced as
 * the user gets a question right via a passed in list. The first generated button is assigned
 * to be the answer. The class will have a function that will return the array of buttons back
 * where the buttons stand on there own.
 */
public class MediaButtonSetFactory {

    // ensures that the values are always true
    public static final int numberActivityType = 1;
    public static final int wordActivityType = 2;
    public static final int nameActivityType = 3;

    /**
     * Will generate buttons of two ways; one with a limitation of not including the already
     * answered questions, and the rest with all the available question options.
     *
     * @param activityType     the activity that the user requests buttons for.
     * @param answerExclusions list specifying the values already answered correctly.
     * @param count            how many buttons are needed.
     * @param context          relation to the activity
     */
    public static List<MediaButton> generateButtonList(int activityType, List<Integer> answerExclusions,
                                                int count, Context context) {

        // create the question bank so knows what values to choose from
        List<Integer> questionBank = new ArrayList<>();
        List<MediaButton> buttonList = new ArrayList<>();
        List<Integer> randomButtonValues = new ArrayList<>();

        // make sure the activity is not over because all values have been selected correctly
        //    otherwise would result in endless loop
        if (questionBank.size() != answerExclusions.size()) {

            // create a randomized list
            switch (activityType) {
                // create numbers 0-10. will also have index 1-10
                case numberActivityType:
                    for (int i = 0; i <= 10; i++) {
                        questionBank.add(i);
                    }

                    randomButtonValues = randomAnswerButtonGenerator(answerExclusions, questionBank,
                            count);

                    break;
                // create a-z and also can be used for letter sounds. This will also have index 0-25
                case wordActivityType:
                    for (int i = 'a'; i <= 'z'; i++) {
                        questionBank.add(i);
                    }

                    randomButtonValues = randomAnswerButtonGenerator(answerExclusions, questionBank,
                            count);
                    break;
                // letter sound activity
                case nameActivityType:
                    // get name from shared preferences
                    SharedPreferences sharedPreferences = context.getSharedPreferences("SETTINGS",
                            MODE_PRIVATE);
                    String usersName = sharedPreferences.getString("NAME", "");

                    for (int i = 0; i < usersName.length(); i++) {
                        questionBank.add((int) usersName.charAt(i));
                    }

                    randomButtonValues = randomAnswerButtonGenerator(answerExclusions, questionBank,
                            count);
                    break;
                default:
                    IOException exception = new IOException("Not valid range for activity");
                    try {
                        throw exception;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            // create the filename from this list


            // finally make buttons with associated values
            /*for (int i = 0; i < count; i++) {
                if (i == 0)
                    buttonList.add(new MediaButton(activityType, context, randomButtonValues.get(i),
                            true));
                else
                    buttonList.add(new MediaButton(activityType, context, randomButtonValues.get(i),
                            false));
            }*/
        }

        return buttonList;
    }

    /**
     * RANDOMBUTTONGENERATOR will generate an array of random values to be used for the buttons.
     * Will be unique values that are not used again if already answered.
     *
     * @param answerExclusions list specifying the values already answered correctly
     * @param questionBank     the values that are activtiy specific
     * @param count            how many buttons are needed
     */
    // RANDOMBUTTONGENERATOR will generate an array of random values to be used for the buttons.
    //    Will be unique values that are not used again if already answered
    private static List<Integer> randomAnswerButtonGenerator(List<Integer> answerExclusions,
                                                      List<Integer> questionBank, int count) {

        List<Integer> buttonValues = new ArrayList<>();
        Random randomValueRetriever = new Random();
        Integer answerNumber;

        // set the first value with conditions based on already answered questions
        do {

            // get random value
            answerNumber = questionBank.get(randomValueRetriever.nextInt(questionBank.size()));

            // check if random number already exists where -1 means it doesn't exists
            if (answerExclusions.indexOf(answerNumber) == -1)
                buttonValues.add(answerNumber);

            // once the right size array has been made then we have a randomized list
        } while (buttonValues.size() < 1);

        // generate the rest of the buttons with random values that don't match button 1st
        //    button made
        do {

            // get random value
            Integer randomNum = questionBank.get(randomValueRetriever.nextInt(questionBank.size()));

            // check if number already added, not added if -1 is returned
            if (buttonValues.indexOf(randomNum) == -1)
                buttonValues.add(randomNum);

            // once the right size array has been made then we have a randomized list
        } while (buttonValues.size() < count);

        return buttonValues;
    }
}
