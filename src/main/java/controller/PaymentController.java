package controller;

import model.*;
import service.PaymentService;
import util.PaymentValidationUtil;


public class PaymentController {

    private PaymentService paymentService = new PaymentService();

    // =========================================================================
    // MAIN PAYMENT PROCESSING
    // =========================================================================

    public void processPayment(Payment payment) {

        System.out.println("\n========================================");
        System.out.println("             PAYMENT - PROCESS"            );
        System.out.println("========================================");

        if (payment == null) {
            System.out.println("Error: Payment object is null");
            return;
        }

        paymentService.processPayment(payment);
    }

    public void processUPIPayment(double amount, String date, int bookingId, String upiId) {

        System.out.println("\n========================================");
        System.out.println("          PROCESS UPI PAYMENT             ");
        System.out.println("========================================");

        String upiError = PaymentValidationUtil.validateUpiId(upiId);
        if (upiError != null) {
            System.out.println("UPI Validation Failed: " + upiError);
            return;
        }

        System.out.println("UPI ID format is valid");

        String amountError = PaymentValidationUtil.validateAmount(amount);
        if (amountError != null) {
            System.out.println("Amount Validation Failed: " + amountError);
            return;
        }

        System.out.println("Amount is valid: Rs. " + amount);

        UPIPayment upiPayment = new UPIPayment(amount, date, "PENDING", bookingId, upiId);

        paymentService.processPayment(upiPayment);
    }

    public void processCreditCardPayment(double amount, String date, int bookingId,
                                         String cardNumber, String holderName,
                                         String expiry, String cvv) {

        System.out.println("\n========================================");
        System.out.println("       PROCESS CREDIT CARD PAYMENT ");
        System.out.println("========================================");

        String cardError = PaymentValidationUtil.validateCreditCard(
            cardNumber, holderName, expiry, cvv
        );

        if (cardError != null) {
            System.out.println("Credit Card Validation Failed: " + cardError);
            return;
        }

        System.out.println("Credit card details are valid");
        System.out.println("  Card: " + maskCardNumber(cardNumber));
        System.out.println("  Holder: " + holderName);

        String amountError = PaymentValidationUtil.validateAmount(amount);
        if (amountError != null) {
            System.out.println("Amount Validation Failed: " + amountError);
            return;
        }

        System.out.println("Amount is valid: Rs. " + amount);

        CreditCardPayment ccPayment = new CreditCardPayment(
            amount, date, "PENDING", bookingId, cardNumber, holderName
        );

        paymentService.processPayment(ccPayment);
    }

    public void processDebitCardPayment(double amount, String date, int bookingId,
                                        String cardNumber, String bankName,
                                        String expiry, String cvv) {

        System.out.println("\n========================================");
        System.out.println("        PROCESS DEBIT CARD PAYMENT   ");
        System.out.println("========================================");

        String cardError = PaymentValidationUtil.validateDebitCard(
            cardNumber, bankName, expiry, cvv
        );

        if (cardError != null) {
            System.out.println("Debit Card Validation Failed: " + cardError);
            return;
        }

        System.out.println("Debit card details are valid");
        System.out.println("  Card: " + maskCardNumber(cardNumber));
        System.out.println("  Bank: " + bankName);

        String amountError = PaymentValidationUtil.validateAmount(amount);
        if (amountError != null) {
            System.out.println("Amount Validation Failed: " + amountError);
            return;
        }

        System.out.println("Amount is valid: Rs. " + amount);

        DebitCardPayment dcPayment = new DebitCardPayment(
            amount, date, "PENDING", bookingId, cardNumber, bankName
        );

        paymentService.processPayment(dcPayment);
    }

    public boolean verifyUPIId(String upiId) {
        System.out.println("\n========================================");
        System.out.println("             VERIFY UPI ID  ");
        System.out.println("========================================");

        String error = PaymentValidationUtil.validateUpiId(upiId);
        if (error != null) {
            System.out.println(error);
            return false;
        }

        System.out.println("UPI ID is valid: " + maskUPIId(upiId));
        return true;
    }

    public boolean verifyCreditCard(String cardNumber, String holderName,
                                    String expiry, String cvv) {
        System.out.println("\n========================================");
        System.out.println("              VERIFY CREDIT CARD        ");
        System.out.println("========================================");

        String error = PaymentValidationUtil.validateCreditCard(
            cardNumber, holderName, expiry, cvv
        );

        if (error != null) {
            System.out.println(error);
            return false;
        }

        System.out.println("Credit card details are valid");
        System.out.println("  Card: " + maskCardNumber(cardNumber));
        System.out.println("  Holder: " + holderName);
        return true;
    }

    public boolean verifyDebitCard(String cardNumber, String bankName,
                                   String expiry, String cvv) {
        System.out.println("\n========================================");
        System.out.println("              VERIFY DEBIT CARD ");
        System.out.println("========================================");

        String error = PaymentValidationUtil.validateDebitCard(
            cardNumber, bankName, expiry, cvv
        );

        if (error != null) {
            System.out.println(error);
            return false;
        }

        System.out.println("Debit card details are valid");
        System.out.println("  Card: " + maskCardNumber(cardNumber));
        System.out.println("  Bank: " + bankName);
        return true;
    }

    public boolean verifyAmount(double amount) {
        String error = PaymentValidationUtil.validateAmount(amount);
        if (error != null) {
            System.out.println(error);
            return false;
        }
        System.out.println("Amount is valid: Rs. " + amount);
        return true;
    }

    public void viewPayment(int paymentId) {

        System.out.println("\n========================================");
        System.out.println("                PAYMENT - VIEW    ");
        System.out.println("========================================");

        Payment payment = paymentService.viewPayment(paymentId);

        if (payment != null) {
            System.out.println("\nPayment retrieved successfully");
        } else {
            System.out.println("\nFailed to retrieve payment");
        }
    }

    public void viewPaymentHistory(int bookingId) {

        System.out.println("\n========================================");
        System.out.println("               PAYMENT - HISTORY    ");
        System.out.println("========================================");

        paymentService.viewPaymentHistory(bookingId);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "INVALID";
        }
        String cleaned = cardNumber.replaceAll("[\\s-]", "");
        String lastFour = cleaned.substring(cleaned.length() - 4);
        return "XXXX XXXX XXXX " + lastFour;
    }

    private String maskUPIId(String upiId) {
        if (upiId == null || !upiId.contains("@")) {
            return "INVALID";
        }
        String[] parts = upiId.split("@");
        if (parts.length != 2) {
            return "INVALID";
        }

        String handle = parts[0];
        String bank = parts[1];

        if (handle.length() <= 3) {
            return "***@" + bank;
        }
        return handle.substring(0, 3) + "***@" + bank;
    }
}