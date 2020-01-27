package com.dankosoucek;

import com.dankosoucek.model.FileLoader;
import com.dankosoucek.model.PaymentProcessor;
import com.dankosoucek.controller.CommandReader;
import com.dankosoucek.view.OutputWriter;

public class Main {
    public static void main( String[] args ) {
        OutputWriter outputWriter = new OutputWriter();
        PaymentProcessor paymentProcessor = new PaymentProcessor(outputWriter);
        FileLoader fileLoader = new FileLoader(outputWriter, paymentProcessor);

        //initialization of CommandReader will trigger initial payments load from the specified file
        CommandReader commandReader = new CommandReader(outputWriter, paymentProcessor, fileLoader);

        //after the initial payments load, additional command reading and periodical balance report
        // is run in separate thread.
        commandReader.start();
        paymentProcessor.start();
    }
}
