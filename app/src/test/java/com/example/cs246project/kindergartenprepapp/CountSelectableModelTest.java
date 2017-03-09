package com.example.cs246project.kindergartenprepapp;

import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Michael Lucero on 3/8/17.
 */
public class CountSelectableModelTest extends SelectableModel {


    @Test
    public void questionAnswerBanksRangeTest() throws Exception {

        // should only go to a possible amount of 10 because of question bank size
        for (int i = 1 ; i < 10 ; i++) {

            CountSelectableModel countSelectableModel = new CountSelectableModel(i);

            List generatedList = countSelectableModel.generateValueList();

            // check generated list of values is size requested
            assertEquals(i, generatedList.size());

            // check that the bank answers are removed as expected

                countSelectableModel.updateQuestionBank(countSelectableModel._answer);

                assertEquals(i, generatedList.size());
        }

        CountSelectableModel countSelectableModel = new CountSelectableModel(4);

        // check that values are removed completely
        for (int i = 1 ; i < 10 ; i++) {
            boolean isAnswer;

            List generatedList = countSelectableModel.generateValueList();

            countSelectableModel.updateQuestionBank(countSelectableModel._answer);

            // check the answer exists
            isAnswer = countSelectableModel.isCorrect(generatedList.get(0));
            isAnswer = countSelectableModel.isCorrect(generatedList.get(1));
            isAnswer = countSelectableModel.isCorrect(generatedList.get(2));
            isAnswer = countSelectableModel.isCorrect(generatedList.get(3));

        }
    }
}