/*
 * Author         : Subhashree R
 * Created Date   : 11-March-2026
 * Description    : Model class representing a Credit Card-based payment.
 * Module         : Payment Module
 * Java version   : 24
 */
package model;

import util.ColorText;
import util.PaymentValidationUtil;

public class CreditCardPayment extends Payment {

    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private String cvv;


    public CreditCardPayment(double amount, String paymentDate, String status, int bookingId,
                             String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        super(amount, paymentDate, status, bookingId, "CREDIT_CARD");
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public String validate() {
        System.out.println(ColorText.yellow("\n  └─ Validating Credit Card Details..."));
        String error = PaymentValidationUtil.validateCreditCard(this.cardNumber, this.cardHolderName, this.expiryDate, this.cvv);
        if (error == null) {
            System.out.println(ColorText.success("    ✔  Card Number validated : ") + ColorText.cyan(PaymentValidationUtil.maskCardNumber(this.cardNumber)));
            System.out.println(ColorText.success("    ✔  Cardholder validated  : ") + ColorText.cyan(this.cardHolderName));
            System.out.println(ColorText.success("    ✔  Expiry Date validated"));
            System.out.println(ColorText.success("    ✔  CVV validated"));
        }
        return error;
    }

    @Override
    public void displayDetails() {
        System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("Card No.  ") + " : %-35s" + ColorText.warning("║") + "%n", PaymentValidationUtil.maskCardNumber(this.cardNumber));
        System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("Holder    ") + " : %-35s" + ColorText.warning("║") + "%n", this.cardHolderName);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCvv() {
        return cvv;
    }
}