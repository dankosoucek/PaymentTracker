package com.dankosoucek.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import com.dankosoucek.view.OutputWriter;
import com.google.common.collect.Lists;

public class PaymentProcessor extends Thread{
    private static final Pattern PAYMENT_PATTERN = Pattern.compile("([A-Z]{3})\\s(-?\\d+(?:\\.\\d+)?)");
    private static final int BALANCE_PRINTING_PERIOD = 60000;

    private OutputWriter outputWriter;

    private ConcurrentMap<String, Double> paymentMap;

    public PaymentProcessor(OutputWriter outputWriter) {
        this.outputWriter = outputWriter;
        this.paymentMap = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        while (true) {
            outputWriter.printBalances(getListOfBalances());
            try {
                Thread.sleep(BALANCE_PRINTING_PERIOD);
            } catch (InterruptedException e) {
                interrupt();
                break;
            }
        }
    }

    @Override
    public void interrupt() {
        outputWriter.writeToConsole("The PaymentProcessor thread has been interrupted...");
        outputWriter.closeWriter();
    }

    @Nonnull
    public List<String> getListOfBalances() {
        List<String> balanceList = Lists.newArrayList();
        for (Map.Entry<String, Double> paymentEntry : paymentMap.entrySet()) {
            if (paymentEntry.getValue() != 0.0d) {
                balanceList.add(paymentEntry.getKey() + " " + paymentEntry.getValue());
            }
        }
        return balanceList;
    }

    public void handleInput(String input) {
        if (!isValidPayment(input)) {
            outputWriter.writeToConsole(String.format("The payment %s is in the wrong format.", input));
        } else {
            Payment payment = getPaymentFromInput(input);
            if (payment == null) {
                outputWriter.writeToConsole("Unexpected error occurred when parsing a payment from the input!");
                return;
            }
            addPayment(payment);
        }
    }

    private boolean isValidPayment(String input) {
        Matcher matcher = PAYMENT_PATTERN.matcher(input);
        return matcher.matches();
    }

    private Payment getPaymentFromInput(String input) {
        Matcher matcher = PAYMENT_PATTERN.matcher(input);
        if (matcher.find( )) {
            String currency = matcher.group(1);
            double amount = Double.parseDouble(matcher.group(2));
            return new Payment(currency, amount);
        }
        return null;
    }

    private void addPayment(Payment payment) {
        Double newAmount = this.paymentMap.computeIfPresent(payment.getCurrency(), (c, a) -> a + payment.getAmount());
        if (newAmount == null) {
            this.paymentMap.put(payment.getCurrency(), payment.getAmount());
        }
    }
}
