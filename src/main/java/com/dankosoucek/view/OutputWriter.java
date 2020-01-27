package com.dankosoucek.view;

import java.io.PrintWriter;
import java.util.List;

public class OutputWriter{
    private PrintWriter writer = new PrintWriter(System.out, true);

    public OutputWriter() {

    }

    public void printBalances(List<String> balanceList) {
        if (balanceList.isEmpty()) {
            this.writer.println("No balances are available at the moment. Add a non-zero payment to see a new balance.");
        } else {
            this.writer.println("Current balances:");
            balanceList.forEach(balance -> this.writer.println(balance));
        }
    }

    public void printException(Exception e) {
        this.writer.println("An exception occurred:");
        this.writer.println(e);
    }

    public void writeToConsole(String message) {
        this.writer.println(message);
    }

    public void closeWriter() {
        this.writer.close();
    }
}
