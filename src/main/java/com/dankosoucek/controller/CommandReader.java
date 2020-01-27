package com.dankosoucek.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.dankosoucek.model.FileLoader;
import com.dankosoucek.model.PaymentProcessor;
import com.dankosoucek.view.OutputWriter;

public class CommandReader extends Thread {
    private static final String QUIT_COMMAND = "quit";

    private OutputWriter outputWriter;
    private PaymentProcessor paymentProcessor;
    private FileLoader fileLoader;

    private BufferedReader reader;

    public CommandReader(OutputWriter summaryWriter, PaymentProcessor paymentProcessor, FileLoader fileLoader) {
        this.outputWriter = summaryWriter;
        this.paymentProcessor = paymentProcessor;
        this.fileLoader = fileLoader;
        this.reader = new BufferedReader(new InputStreamReader(System.in));

        loadInitialBalances();
    }

    private void loadInitialBalances() {
        outputWriter.writeToConsole("If you want to load initial balances, please input a full path to a text file. Otherwise press Return.");
        String pathToFile;
        try {
            pathToFile = reader.readLine();
            if (pathToFile != null && !pathToFile.isEmpty()) {
                fileLoader.loadInitialBalancesFromFile(pathToFile);
            }
        } catch (IOException e) {
            outputWriter.printException(e);
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                String input = reader.readLine();
                if (QUIT_COMMAND.equalsIgnoreCase(input)) {
                    outputWriter.writeToConsole("Quitting application...");
                    outputWriter.closeWriter();
                    break;
                }
                paymentProcessor.handleInput(input);
            } catch (IOException e) {
                outputWriter.printException(e);
                outputWriter.closeWriter();
            }
        }
        System.exit(0);
    }

}
