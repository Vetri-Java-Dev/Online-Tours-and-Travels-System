package controller;

import model.*;
import service.PaymentService;
import util.ColorText;
import util.PaymentValidationUtil;


public class PaymentController {

    private PaymentService paymentService = new PaymentService();

    // =========================================================================
    // MAIN PAYMENT PROCESSING
    // =========================================================================

    public void processPayment(Payment payment) {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("          PAYMENT — PROCESS           ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        if (payment == null) {
            System.out.println(ColorText.error("  ✘  Error: Payment object is null."));
            return;
        }

        paymentService.processPayment(payment);
    }

    public void processUPIPayment(double amount, String date, int bookingId, String upiId) {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("        PROCESS UPI PAYMENT           ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        String upiError = PaymentValidationUtil.validateUpiId(upiId);
        if (upiError != null) {
            System.out.println(ColorText.error("  ✘  UPI Validation Failed: " + upiError));
            return;
        }
        System.out.println(ColorText.success("  ✔  UPI ID format is valid."));

        String amountError = PaymentValidationUtil.validateAmount(amount);
        if (amountError != null) {
            System.out.println(ColorText.error("  ✘  Amount Validation Failed: " + amountError));
            return;
        }
        System.out.println(ColorText.success("  ✔  Amount is valid: ") + ColorText.cyan("Rs. " + amount));

        UPIPayment upiPayment = new UPIPayment(amount, date, "PENDING", bookingId, upiId);
        paymentService.processPayment(upiPayment);
    }

    public void processCreditCardPayment(double amount, String date, int bookingId,
                                         String cardNumber, String holderName,
                                         String expiry, String cvv) {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("     PROCESS CREDIT CARD PAYMENT      ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        String cardError = PaymentValidationUtil.validateCreditCard(
            cardNumber, holderName, expiry, cvv
        );
        if (cardError != null) {
            System.out.println(ColorText.error("  ✘  Credit Card Validation Failed: " + cardError));
            return;
        }

        System.out.println(ColorText.success("  ✔  Credit card details are valid."));
        System.out.println(ColorText.warning("  ╔══════════════════════════════════════╗"));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Card  ") + " : %-30s" + ColorText.warning("║") + "%n", maskCardNumber(cardNumber));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Holder") + " : %-30s" + ColorText.warning("║") + "%n", holderName);
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════╝"));

        String amountError = PaymentValidationUtil.validateAmount(amount);
        if (amountError != null) {
            System.out.println(ColorText.error("  ✘  Amount Validation Failed: " + amountError));
            return;
        }
        System.out.println(ColorText.success("  ✔  Amount is valid: ") + ColorText.cyan("Rs. " + amount));

        CreditCardPayment ccPayment = new CreditCardPayment(
            amount, date, "PENDING", bookingId, cardNumber, holderName
        );
        paymentService.processPayment(ccPayment);
    }

    public void processDebitCardPayment(double amount, String date, int bookingId,
                                        String cardNumber, String bankName,
                                        String expiry, String cvv) {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("      PROCESS DEBIT CARD PAYMENT      ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        String cardError = PaymentValidationUtil.validateDebitCard(
            cardNumber, bankName, expiry, cvv
        );
        if (cardError != null) {
            System.out.println(ColorText.error("  ✘  Debit Card Validation Failed: " + cardError));
            return;
        }

        System.out.println(ColorText.success("  ✔  Debit card details are valid."));
        System.out.println(ColorText.warning("  ╔══════════════════════════════════════╗"));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Card") + " : %-32s" + ColorText.warning("║") + "%n", maskCardNumber(cardNumber));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Bank") + " : %-32s" + ColorText.warning("║") + "%n", bankName);
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════╝"));

        String amountError = PaymentValidationUtil.validateAmount(amount);
        if (amountError != null) {
            System.out.println(ColorText.error("  ✘  Amount Validation Failed: " + amountError));
            return;
        }
        System.out.println(ColorText.success("  ✔  Amount is valid: ") + ColorText.cyan("Rs. " + amount));

        DebitCardPayment dcPayment = new DebitCardPayment(
            amount, date, "PENDING", bookingId, cardNumber, bankName
        );
        paymentService.processPayment(dcPayment);
    }

    public boolean verifyUPIId(String upiId) {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           VERIFY UPI ID              ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        String error = PaymentValidationUtil.validateUpiId(upiId);
        if (error != null) {
            System.out.println(ColorText.error("  ✘  " + error));
            return false;
        }

        System.out.println(ColorText.success("  ✔  UPI ID is valid: ") + ColorText.cyan(maskUPIId(upiId)));
        return true;
    }

    public boolean verifyCreditCard(String cardNumber, String holderName,
                                    String expiry, String cvv) {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("         VERIFY CREDIT CARD           ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        String error = PaymentValidationUtil.validateCreditCard(
            cardNumber, holderName, expiry, cvv
        );
        if (error != null) {
            System.out.println(ColorText.error("  ✘  " + error));
            return false;
        }

        System.out.println(ColorText.success("  ✔  Credit card details are valid."));
        System.out.println(ColorText.warning("  ╔══════════════════════════════════════╗"));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Card  ") + " : %-30s" + ColorText.warning("║") + "%n", maskCardNumber(cardNumber));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Holder") + " : %-30s" + ColorText.warning("║") + "%n", holderName);
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════╝"));
        return true;
    }

    public boolean verifyDebitCard(String cardNumber, String bankName,
                                   String expiry, String cvv) {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("          VERIFY DEBIT CARD           ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        String error = PaymentValidationUtil.validateDebitCard(
            cardNumber, bankName, expiry, cvv
        );
        if (error != null) {
            System.out.println(ColorText.error("  ✘  " + error));
            return false;
        }

        System.out.println(ColorText.success("  ✔  Debit card details are valid."));
        System.out.println(ColorText.warning("  ╔══════════════════════════════════════╗"));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Card") + " : %-32s" + ColorText.warning("║") + "%n", maskCardNumber(cardNumber));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Bank") + " : %-32s" + ColorText.warning("║") + "%n", bankName);
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════╝"));
        return true;
    }

    public boolean verifyAmount(double amount) {
        String error = PaymentValidationUtil.validateAmount(amount);
        if (error != null) {
            System.out.println(ColorText.error("  ✘  " + error));
            return false;
        }
        System.out.println(ColorText.success("  ✔  Amount is valid: ") + ColorText.cyan("Rs. " + amount));
        return true;
    }

    public void viewPayment(int paymentId) {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("           PAYMENT — VIEW             ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        Payment payment = paymentService.viewPayment(paymentId);

        if (payment != null) {
            System.out.println(ColorText.success("  ✔  Payment retrieved successfully."));
        } else {
            System.out.println(ColorText.error("  ✘  Failed to retrieve payment."));
        }
    }

    public void viewPaymentHistory(int bookingId) {

        System.out.println(ColorText.warning("\n╔══════════════════════════════════════╗"));
        System.out.println(ColorText.warning("║") + ColorText.bold("         PAYMENT — HISTORY            ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("╚══════════════════════════════════════╝"));

        paymentService.viewPaymentHistory(bookingId);
    }

    // =========================================================================
    // PRIVATE HELPER METHODS
    // =========================================================================

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
        String bank   = parts[1];
        if (handle.length() <= 3) {
            return "***@" + bank;
        }
        return handle.substring(0, 3) + "***@" + bank;
    }
}