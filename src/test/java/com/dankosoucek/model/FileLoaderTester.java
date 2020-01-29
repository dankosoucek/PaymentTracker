package com.dankosoucek.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.dankosoucek.view.OutputWriter;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;

public class FileLoaderTester {

    @Mocked
    private Files files;

    @Mocked
    private OutputWriter outputWriter;

    private FileLoader tested;

    @Test
    public void testLoadInitialBalancesFromFile_fileNotExists() {
        PaymentProcessor paymentProcessor = new PaymentProcessor(outputWriter);
        tested = new FileLoader(outputWriter, paymentProcessor);
        new Expectations() {
            {
                Files.exists((Path) any);
                result = false;

                outputWriter.writeToConsole("invalidPath is not a valid path to a text file. No balances will be loaded.");
            }
        };
        tested.loadInitialBalancesFromFile("invalidPath");

        new Verifications(){
            {
                outputWriter.writeToConsole("invalidPath is not a valid path to a text file. No balances will be loaded.");
                times = 1;
            }
        };
    }

    @Test
    public void testLoadInitialBalancesFromFile_fileExists() throws IOException {
        PaymentProcessor paymentProcessor = new PaymentProcessor(outputWriter);
        tested = new FileLoader(outputWriter, paymentProcessor);
        Stream<String> stream = Arrays.asList("USD 25").stream();

        new Expectations() {
            {
                Files.exists((Path) any);
                result = true;

                Files.lines((Path) any);
                result = stream;
            }
        };
        tested.loadInitialBalancesFromFile("validPath");

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
