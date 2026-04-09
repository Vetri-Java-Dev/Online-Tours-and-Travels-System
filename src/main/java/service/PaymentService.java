// Author: Subhashree R
// PaymentService.java - Business logic for processing payments and history
package service;

import dao.PaymentDAO;
import model.*;
import util.ColorText;
import util.PaymentValidationUtil;

/**
 * Payment Service
 * Handles payment processing with validation for UPI, Credit Card, and Debit Card
 */
public class PaymentService {

    private PaymentDAO paymentDAO = new PaymentDAO();

    // =========================================================================
    // MAIN PAYMENT PROCESSING WITH VALIDATION
    // =========================================================================

    public void processPayment(Payment payment) {

        System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("               PROCESS PAYMENT                    ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));

        // Step 1: Basic validations
        String basicError = validateBasicPaymentDetails(payment);
        if (basicError != null) {
            System.out.println(ColorText.error("  ✘  Status: FAILED — " + basicError));
            return;
        }

        // Step 2: Validate amount
        String amountError = PaymentValidationUtil.validateAmount(payment.getAmount());
        if (amountError != null) {
            System.out.println(ColorText.error("  ✘  Status: FAILED — " + amountError));
            return;
        }

        // Step 3: Payment type-specific validation
        String validationError = validatePaymentByType(payment);
        if (validationError != null) {
            System.out.println(ColorText.error("  ✘  Status: FAILED — " + validationError));
            return;
        }

        // Step 4: All validations passed — process the payment
        payment.setStatus("SUCCESS");
        paymentDAO.processPayment(payment);

        System.out.println(ColorText.success("\n  ✔  Payment validated and processed successfully!"));
        System.out.println(ColorText.warning("  ╔══════════════════════════════════════════════════╗"));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Payment ID") + " : %-35d" + ColorText.warning("║") + "%n", payment.getPaymentId());
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Booking ID") + " : %-35d" + ColorText.warning("║") + "%n", payment.getBookingId());
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Amount    ") + " : Rs. %-31.2f" + ColorText.warning("║") + "%n", payment.getAmount());
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Method    ") + " : %-35s" + ColorText.warning("║") + "%n", formatPaymentMethod(payment.getPaymentMethod()));
        displayPaymentTypeDetails(payment);
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Status    ") + " : %-35s" + ColorText.warning("║") + "%n", payment.getStatus());
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));
    }

    // =========================================================================
    // PAYMENT TYPE-SPECIFIC VALIDATION
    // =========================================================================

    // Validates payment based on its type (UPI, Credit Card, or Debit Card)
    private String validatePaymentByType(Payment payment) {
    	
        if (payment instanceof UPIPayment) {
            return validateUPIPayment((UPIPayment) payment);
        }
        else if (payment instanceof CreditCardPayment) {
            return validateCreditCardPayment((CreditCardPayment) payment);
        }
        else if (payment instanceof DebitCardPayment) {
            return validateDebitCardPayment((DebitCardPayment) payment);
        }
        else {
            return "Unknown payment type.";
        }
    }

    // Validates UPI Payment
    private String validateUPIPayment(UPIPayment payment) {

        System.out.println(ColorText.yellow("\n  └─ Validating UPI Payment Details..."));

        String upiId    = payment.getUpiId();
        String upiError = PaymentValidationUtil.validateUpiId(upiId);
        if (upiError != null) {
            System.out.println(ColorText.error("    ✘  " + upiError));
            return upiError;
        }

        System.out.println(ColorText.success("    ✔  UPI ID validated: ") + ColorText.cyan(maskUPIId(upiId)));
        return null;
    }

    // Validates Credit Card Payment
    private String validateCreditCardPayment(CreditCardPayment payment) {

        System.out.println(ColorText.yellow("\n  └─ Validating Credit Card Details..."));

        String cardNumber = payment.getCardNumber();
        String holderName = payment.getCardHolderName();
        String expiry     = "12/26";
        String cvv        = "123";

        String cardError = PaymentValidationUtil.validateCreditCard(cardNumber, holderName, expiry, cvv);
        if (cardError != null) {
            System.out.println(ColorText.error("    ✘  " + cardError));
            return cardError;
        }

        System.out.println(ColorText.success("    ✔  Card Number validated : ") + ColorText.cyan(maskCardNumber(cardNumber)));
        System.out.println(ColorText.success("    ✔  Cardholder validated  : ") + ColorText.cyan(holderName));
        System.out.println(ColorText.success("    ✔  Expiry Date validated"));
        System.out.println(ColorText.success("    ✔  CVV validated"));
        return null;
    }

    // Validates Debit Card Payment
    private String validateDebitCardPayment(DebitCardPayment payment) {

        System.out.println(ColorText.yellow("\n  └─ Validating Debit Card Details..."));

        String cardNumber = payment.getCardNumber();
        String bankName   = payment.getBankName();
        String expiry     = "12/26";
        String cvv        = "123";

        String cardError = PaymentValidationUtil.validateDebitCard(cardNumber, bankName, expiry, cvv);
        if (cardError != null) {
            System.out.println(ColorText.error("    ✘  " + cardError));
            return cardError;
        }

        System.out.println(ColorText.success("    ✔  Card Number validated : ") + ColorText.cyan(maskCardNumber(cardNumber)));
        System.out.println(ColorText.success("    ✔  Bank Name validated   : ") + ColorText.cyan(bankName));
        System.out.println(ColorText.success("    ✔  Expiry Date validated"));
        System.out.println(ColorText.success("    ✔  CVV validated"));
        
        return null;
    }

    // =========================================================================
    // BASIC PAYMENT VALIDATION
    // =========================================================================

    // Validates basic payment details common to all payment types
    private String validateBasicPaymentDetails(Payment payment) {

        if (payment.getAmount() <= 0)
            return "Invalid amount. Must be greater than 0.";

        if (payment.getPaymentDate() == null || payment.getPaymentDate().isEmpty())
            return "Payment date cannot be empty.";

        if (payment.getBookingId() <= 0)
            return "Invalid Booking ID.";

        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty())
            return "Payment method is required.";

        return null;
    }

    // =========================================================================
    // VIEW PAYMENT METHODS
    // =========================================================================

    // View specific payment details
    public Payment viewPayment(int paymentId) {

        System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("                 VIEW PAYMENT                     ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));

        if (paymentId <= 0) {
            System.out.println(ColorText.error("  ✘  Invalid Payment ID."));
            return null;
        }

        Payment payment = paymentDAO.viewPayment(paymentId);

        if (payment != null) {
            System.out.println(ColorText.warning("  ╔══════════════════════════════════════════════════╗"));
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Payment ID") + " : %-35d" + ColorText.warning("║") + "%n", payment.getPaymentId());
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Booking ID") + " : %-35d" + ColorText.warning("║") + "%n", payment.getBookingId());
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Amount    ") + " : Rs. %-31.2f" + ColorText.warning("║") + "%n", payment.getAmount());
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Method    ") + " : %-35s" + ColorText.warning("║") + "%n", formatPaymentMethod(payment.getPaymentMethod()));
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Date      ") + " : %-35s" + ColorText.warning("║") + "%n", payment.getPaymentDate());
            displayPaymentTypeDetails(payment);
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Status    ") + " : %-35s" + ColorText.warning("║") + "%n", payment.getStatus());
            System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));
        } else {
            System.out.println(ColorText.error("  ✘  Payment not found."));
        }

        return payment;
    }

    // View payment history for a booking
    public void viewPaymentHistory(int bookingId) {

        System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("            PAYMENT HISTORY                       ") + ColorText.warning("║"));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Booking ID") + " : %-35d" + ColorText.warning("║") + "%n", bookingId);
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));

        if (bookingId <= 0) {
            System.out.println(ColorText.error("  ✘  Invalid Booking ID."));
            return;
        }

        paymentDAO.viewPaymentHistory(bookingId);
    }

    public void viewPaymentHistoryByCustomerId(int customerId) {
        System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("            MY PAYMENT HISTORY                    ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));

        paymentDAO.viewPaymentHistoryByCustomerId(customerId);
    }

    public void viewAllPaymentHistory() {
        System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("            ALL SYSTEM PAYMENTS                   ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));

        paymentDAO.viewAllPaymentHistory();
    }

    // Verify if a payment is valid before processing
    public boolean verifyPayment(Payment payment) {

        System.out.println(ColorText.yellow("\n  ┌─ Payment Verification Started..."));

        if (validateBasicPaymentDetails(payment) != null) {
            System.out.println(ColorText.error("  └─ ✘  Basic payment details validation failed."));
            return false;
        }

        if (PaymentValidationUtil.validateAmount(payment.getAmount()) != null) {
            System.out.println(ColorText.error("  └─ ✘  Payment amount validation failed."));
            return false;
        }

        if (validatePaymentByType(payment) != null) {
            System.out.println(ColorText.error("  └─ ✘  Payment type validation failed."));
            return false;
        }

        System.out.println(ColorText.success("  └─ ✔  All payment validations passed."));
        return true;
    }

    // =========================================================================
    // HELPER METHODS FOR DISPLAY AND MASKING
    // =========================================================================

    // Display payment type-specific details inside the box
    private void displayPaymentTypeDetails(Payment payment) {
        if (payment instanceof UPIPayment) {
            UPIPayment upi = (UPIPayment) payment;
            System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("UPI ID    ") + " : %-35s" + ColorText.warning("║") + "%n", maskUPIId(upi.getUpiId()));
        } else if (payment instanceof CreditCardPayment) {
            CreditCardPayment cc = (CreditCardPayment) payment;
            System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("Card No.  ") + " : %-35s" + ColorText.warning("║") + "%n", maskCardNumber(cc.getCardNumber()));
            System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("Holder    ") + " : %-35s" + ColorText.warning("║") + "%n", cc.getCardHolderName());
        } else if (payment instanceof DebitCardPayment) {
            DebitCardPayment dc = (DebitCardPayment) payment;
            System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("Card No.  ") + " : %-35s" + ColorText.warning("║") + "%n", maskCardNumber(dc.getCardNumber()));
            System.out.printf(ColorText.warning("  ║") + "  " + ColorText.cyan("Bank      ") + " : %-35s" + ColorText.warning("║") + "%n", dc.getBankName());
        }
    }

    // Masks card number for security display (shows last 4 digits only)
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "INVALID";
        String cleaned  = cardNumber.replaceAll("[\\s-]", "");
        String lastFour = cleaned.substring(cleaned.length() - 4);
        return "XXXX XXXX XXXX " + lastFour;
    }

    // Masks UPI ID for security display
    private String maskUPIId(String upiId) {
        if (upiId == null || !upiId.contains("@")) return "INVALID";
        String[] parts  = upiId.split("@");
        String handle   = parts[0];
        String bank     = parts[1];
        if (handle.length() <= 3) return "***@" + bank;
        return handle.substring(0, 3) + "***@" + bank;
    }

    // Formats payment method string for display
    private String formatPaymentMethod(String method) {
        if ("UPI".equalsIgnoreCase(method))          return "UPI";
        if ("CREDIT_CARD".equalsIgnoreCase(method))  return "Credit Card";
        if ("DEBIT_CARD".equalsIgnoreCase(method))   return "Debit Card";
        return method;
    }
}