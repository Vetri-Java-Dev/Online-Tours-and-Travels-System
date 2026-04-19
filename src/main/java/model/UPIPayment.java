/*
 * Author         : Subhashree R
 * Created Date   : 11-March-2026
 * Modified Date  : 19-April-2026
 * Description    : Model class representing a UPI-based payment.
 *                  
 * Module         : Payment Module
 * Java version   : 24
 */
package model;

import service.PaymentService;
import util.ColorText;

public class UPIPayment extends Payment {

    private String upiId;

    
    public UPIPayment(double amount, String paymentDate, String status, int bookingId,
                      String upiId) {
        super(amount, paymentDate, status, bookingId, "UPI");
        this.upiId = upiId;
    }

    @Override
    public String validate() {
        System.out.println(ColorText.yellow("\n  └─ Validating UPI Payment Details..."));
        PaymentService paymentService = new PaymentService();
        String error = paymentService.validateUpiId(this.upiId);
        if (error == null) {
            System.out.println(ColorText.success("    ✔  UPI ID validated: ") + ColorText.cyan(paymentService.maskUPIId(this.upiId)));
        }
        return error;
    }

    @Override
    public void displayDetails() {
        PaymentService paymentService = new PaymentService();
        System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("UPI ID    ") + " : %-35s" + ColorText.warning("║") + "%n", paymentService.maskUPIId(this.upiId));
    }

    public String getUpiId() {
        return upiId;
    }
}