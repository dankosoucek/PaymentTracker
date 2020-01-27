package com.dankosoucek.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.dankosoucek.view.OutputWriter;

public class FileLoader {
    private final OutputWriter outputWriter;
    private final PaymentProcessor paymentProcessor;

    public FileLoader(OutputWriter outputWriter, PaymentProcessor paymentProcessor) {

        this.outputWriter = outputWriter;
        this.paymentProcessor = paymentProcessor;
    }

    public void loadInitialBalancesFromFile(String pathToFile) {
        Path path = Paths.get(pathToFile);
        if (!Files.exists(path)) {
            outputWriter.writeToConsole(pathToFile + " is not a valid path to a text file. No balances will be loaded.");
            return;
        }
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(paymentProcessor::handleInput);
        } catch (IOException e) {
            outputWriter.printException(e);
            outputWriter.closeWriter();
        }
    }
}
