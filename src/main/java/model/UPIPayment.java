/*
 * Author         : Subhashree R
 * Created Date   : 11-March-2026
 * Description    : Model class representing a UPI-based payment.
 * Module         : Payment Module
 * Java version   : 24
 */
package model;

import util.ColorText;
import util.PaymentValidationUtil;

public class UPIPayment extends Payment {

    private String upiId;

    // Constructor used when creating new payment
    public UPIPayment(double amount, String paymentDate, String status, int bookingId,
                      String upiId) {
        super(amount, paymentDate, status, bookingId, "UPI");
        this.upiId = upiId;
    }

    @Override
    public String validate() {
        System.out.println(ColorText.yellow("\n  └─ Validating UPI Payment Details..."));
        String error = PaymentValidationUtil.validateUpiId(this.upiId);
        if (error == null) {
            System.out.println(ColorText.success("    ✔  UPI ID validated: ") + ColorText.cyan(PaymentValidationUtil.maskUPIId(this.upiId)));
        }
        return error;
    }

    @Override
    public void displayDetails() {
        System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("UPI ID    ") + " : %-35s" + ColorText.warning("║") + "%n", PaymentValidationUtil.maskUPIId(this.upiId));
    }

    public String getUpiId() {
        return upiId;
    }
}
