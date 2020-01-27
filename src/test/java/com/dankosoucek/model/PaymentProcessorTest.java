package com.dankosoucek.model;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.dankosoucek.view.OutputWriter;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentProcessorTest {

    private PaymentProcessor tested;

    @Mocked
    private OutputWriter outputWriter;

    @Test
    public void testGetListOfBalances_emptyList() {
        tested = new PaymentProcessor(outputWriter);

        List<String> listOfBalances = tested.getListOfBalances();
        Assertions.assertTrue(listOfBalances.isEmpty(), "The initial List of balances is expected to be empty.");
    }

    @Test
    public void testGetListOfBalances_nonEmptyList() {
        tested = new PaymentProcessor(outputWriter);
        tested.handleInput("USD 25");

        List<String> listOfBalances = tested.getListOfBalances();
        assertEquals(1, listOfBalances.size(), "The List of balances is expected one element.");
        assertEquals("USD 25.0", listOfBalances.get(0), "The List of balances does not contain the expected balance.");


        new Verifications(){
            {
                outputWriter.writeToConsole("The payment USD 25 is in the wrong format.");
                times = 0;

                outputWriter.writeToConsole("Unexpected error occurred when parsing a payment from the input!");
                times = 0;
            }
        };
    }

    @Test
    public void testHandleInput_invalidInput() {
        tested = new PaymentProcessor(outputWriter);

        new Expectations() {
            {
                outputWriter.writeToConsole("The payment Invalid is in the wrong format.");
            }
        };
        tested.handleInput("Invalid");
    }

    @Test
    public void testHandleInput_validInput() {
        tested = new PaymentProcessor(outputWriter);
        tested.handleInput("USD 25");

        new Verifications(){
            {
                outputWriter.writeToConsole("The payment USD 25 is in the wrong format.");
                times = 0;

                outputWriter.writeToConsole("Unexpected error occurred when parsing a payment from the input!");
                times = 0;
            }
        };
    }

}
