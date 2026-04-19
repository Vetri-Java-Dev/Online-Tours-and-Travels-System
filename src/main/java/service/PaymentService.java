/*
 * Author         : Subhashree R
 * Created Date   : 10-Apr-2026
 * Description    : Service layer handling robust payment processing, validation workflows, and transaction history retrieval.
 * Module         : Payment Module
 * Java version   : 24
 */
package service;

import dao.PaymentDAO;
import model.*;
import util.ColorText;
import util.PaymentValidationUtil;

/**
 * Payment Service
 * Handles payment processing using polymorphic Payment model classes.
 */
public class PaymentService {

    private PaymentDAO paymentDAO = new PaymentDAO();

    // =========================================================================
    // MAIN PAYMENT PROCESSING WITH POLYMORPHISM
    // =========================================================================

    public void processPayment(Payment payment) {

        System.out.println(ColorText.warning("\n  ╔══════════════════════════════════════════════════╗"));
        System.out.println(ColorText.warning("  ║") + ColorText.bold("               PROCESS PAYMENT                    ") + ColorText.warning("║"));
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));

        // Step 1: Basic shared validations
        String basicError = validateBasicPaymentDetails(payment);
        if (basicError != null) {
            System.out.println(ColorText.error("  ✘  Status: FAILED — " + basicError));
            return;
        }

        // Step 2: Validate amount via Utility
        String amountError = PaymentValidationUtil.validateAmount(payment.getAmount());
        if (amountError != null) {
            System.out.println(ColorText.error("  ✘  Status: FAILED — " + amountError));
            return;
        }

        // Step 3: Polymorphic type-specific validation
        String validationError = payment.validate();
        if (validationError != null) {
            System.out.println(ColorText.error("  ✘  Status: FAILED — " + validationError));
            return;
        }

        // Step 4: Process the payment
        payment.setStatus("SUCCESS");
        paymentDAO.processPayment(payment);

        System.out.println(ColorText.success("\n  ✔  Payment validated and processed successfully!"));
        System.out.println(ColorText.warning("  ╔══════════════════════════════════════════════════╗"));
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Payment ID") + " : %-35d" + ColorText.warning("║") + "%n", payment.getPaymentId());
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Booking ID") + " : %-35d" + ColorText.warning("║") + "%n", payment.getBookingId());
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Amount    ") + " : Rs. %-31.2f" + ColorText.warning("║") + "%n", payment.getAmount());
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Method    ") + " : %-35s" + ColorText.warning("║") + "%n", formatPaymentMethod(payment.getPaymentMethod()));
        
        // Polymorphic display call
        payment.displayDetails();
        
        System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Status    ") + " : %-35s" + ColorText.warning("║") + "%n", payment.getStatus());
        System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));
    }

    // =========================================================================
    // BASIC PAYMENT VALIDATION
    // =========================================================================

    private String validateBasicPaymentDetails(Payment payment) {
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
            
            // Polymorphic display call
            payment.displayDetails();
            
            System.out.printf (ColorText.warning("  ║") + "  " + ColorText.cyan("Status    ") + " : %-35s" + ColorText.warning("║") + "%n", payment.getStatus());
            System.out.println(ColorText.warning("  ╚══════════════════════════════════════════════════╝"));
        } else {
            System.out.println(ColorText.error("  ✘  Payment not found."));
        }
        return payment;
    }

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

        // Polymorphic validation call
        if (payment.validate() != null) {
            System.out.println(ColorText.error("  └─ ✘  Payment type validation failed."));
            return false;
        }

        System.out.println(ColorText.success("  └─ ✔  All payment validations passed."));
        return true;
    }

    // Helper to format payment method names
    private String formatPaymentMethod(String method) {
        if ("UPI".equalsIgnoreCase(method))          return "UPI";
        if ("CREDIT_CARD".equalsIgnoreCase(method))  return "Credit Card";
        if ("DEBIT_CARD".equalsIgnoreCase(method))   return "Debit Card";
        return method;
    }

    // =========================================================================
    // LUHN ALGORITHM (Industry-standard check digit validation)
    // =========================================================================

    /**
     * Performs Luhn algorithm check for card number validation.
     * @param number The card number string (should contain only digits)
     * @return true if valid, false otherwise
     */
    public static boolean luhnCheck(String number) {
        int sum = 0;
        boolean alt = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';

            if (alt) {
                n *= 2;
                if (n > 9)
                    n -= 9;
            }
            sum += n;
            alt = !alt;
        }

        return (sum % 10 == 0);
    }
}