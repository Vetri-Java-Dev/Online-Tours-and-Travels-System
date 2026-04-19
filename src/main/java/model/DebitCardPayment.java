/*
 * Author         : Subhashree R
 * Created Date   : 11-March-2026
 * Description    : Model class representing a Debit Card-based payment.
 * Module         : Payment Module
 * Java version   : 24
 */
package model;

import util.ColorText;
import util.PaymentValidationUtil;

public class DebitCardPayment extends Payment {

    private String cardNumber;
    private String bankName;
    private String expiryDate;
    private String cvv;


    // Constructor used when creating new payment
    public DebitCardPayment(double amount, String paymentDate, String status, int bookingId,
                            String cardNumber, String bankName, String expiryDate, String cvv) {
        super(amount, paymentDate, status, bookingId, "DEBIT_CARD");
        this.cardNumber = cardNumber;
        this.bankName = bankName;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public String validate() {
        System.out.println(ColorText.yellow("\n  └─ Validating Debit Card Details..."));
        String error = PaymentValidationUtil.validateDebitCard(this.cardNumber, this.bankName, this.expiryDate, this.cvv);
        if (error == null) {
            System.out.println(ColorText.success("    ✔  Card Number validated : ") + ColorText.cyan(PaymentValidationUtil.maskCardNumber(this.cardNumber)));
            System.out.println(ColorText.success("    ✔  Bank Name validated   : ") + ColorText.cyan(this.bankName));
            System.out.println(ColorText.success("    ✔  Expiry Date validated"));
            System.out.println(ColorText.success("    ✔  CVV validated"));
        }
        return error;
    }

    @Override
    public void displayDetails() {
        System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("Card No.  ") + " : %-35s" + ColorText.warning("║") + "%n", PaymentValidationUtil.maskCardNumber(this.cardNumber));
        System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("Bank      ") + " : %-35s" + ColorText.warning("║") + "%n", this.bankName);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCvv() {
        return cvv;
    }
}