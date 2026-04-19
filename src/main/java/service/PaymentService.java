/*
 * Author         : Subhashree R
 * Created Date   : 10-Apr-2026
 * Modified Date  : 19-April-2026
 * Description    : Service layer handling payment processing, validation workflows,
 *                  and transaction history retrieval.
 *                
 * Module         : Payment Module
 * Java version   : 24
 */
package service;

import dao.PaymentDAO;
import model.*;
import util.ColorText;

import java.util.regex.Pattern;

public class PaymentService {

    private PaymentDAO paymentDAO = new PaymentDAO();

    // =========================================================================
    // REGEX PATTERNS (moved from PaymentValidationUtil)
    // =========================================================================

    private static final Pattern UPI_PATTERN =
        Pattern.compile("^[a-zA-Z0-9._-]{3,50}@[a-zA-Z]{2,20}$");

    private static final Pattern CARD_NUMBER_PATTERN =
        Pattern.compile("^[0-9]{4}[\\s-]?[0-9]{4}[\\s-]?[0-9]{4}[\\s-]?[0-9]{4}$");

    private static final Pattern NAME_PATTERN =
        Pattern.compile("^[a-zA-Z ]{2,50}$");

    private static final Pattern CVV_PATTERN =
        Pattern.compile("^[0-9]{3,4}$");

    private static final Pattern EXPIRY_PATTERN =
        Pattern.compile("^(0[1-9]|1[0-2])/([0-9]{2})$");

    // =========================================================================
    // MAIN PAYMENT PROCESSING
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

        // Step 2: Validate amount
        String amountError = validateAmount(payment.getAmount());
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

        if (validateAmount(payment.getAmount()) != null) {
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

    // =========================================================================
    // AMOUNT VALIDATION (moved from PaymentValidationUtil)
    // =========================================================================

    public String validateAmount(double amount) {
        if (amount <= 0)
            return "Payment amount must be greater than 0.";

        if (amount > 999999.99)
            return "Payment amount exceeds maximum limit.";

        return null;
    }

    // =========================================================================
    // UPI VALIDATION (moved from PaymentValidationUtil)
    // =========================================================================

    public String validateUpiId(String upiId) {
        if (upiId == null || upiId.trim().isEmpty())
            return "UPI ID cannot be empty.";

        upiId = upiId.trim();

        if (upiId.length() < 3)
            return "UPI ID is too short (minimum 3 characters).";

        if (upiId.length() > 50)
            return "UPI ID is too long (maximum 50 characters).";

        if (!upiId.contains("@"))
            return "Invalid UPI ID format. UPI ID must contain '@' symbol.";

        String[] parts  = upiId.split("@");
        if (parts.length != 2)
            return "Invalid UPI ID format. Expected exactly one '@' symbol.";

        String handle   = parts[0];
        String bankCode = parts[1];

        if (handle.isEmpty())
            return "UPI ID handle (before @) cannot be empty.";

        if (bankCode.isEmpty())
            return "Bank handle (after @) cannot be empty.";

        if (!handle.matches("[a-zA-Z0-9._-]+"))
            return "UPI handle contains invalid characters. Use only alphanumeric, dots, hyphens, or underscores.";

        if (!bankCode.matches("[a-zA-Z]+"))
            return "Bank handle contains invalid characters. Use only letters.";

        if (!UPI_PATTERN.matcher(upiId).matches())
            return "Invalid UPI ID format. Expected: yourname@bankhandle (e.g., john@okaxis, 9876543210@paytm)";

        return null;
    }

    // =========================================================================
    // CREDIT CARD VALIDATION (moved from PaymentValidationUtil)
    // =========================================================================

    public String validateCreditCard(String cardNumber, String holderName,
                                      String expiry, String cvv) {
        String cardErr = validateCardNumber(cardNumber);
        if (cardErr != null) return cardErr;

        if (!luhnCheck(cardNumber.replaceAll("[\\s-]", "")))
            return "Please verify and re-enter your card number";

        if (holderName == null || holderName.trim().isEmpty())
            return "Cardholder name cannot be empty.";

        holderName = holderName.trim();
        if (holderName.length() < 2)
            return "Cardholder name must be at least 2 characters.";

        if (holderName.length() > 50)
            return "Cardholder name is too long (maximum 50 characters).";

        if (!NAME_PATTERN.matcher(holderName).matches())
            return "Cardholder name must contain only letters and spaces.";

        String expiryErr = validateExpiry(expiry);
        if (expiryErr != null) return expiryErr;

        String cvvErr = validateCVV(cvv);
        if (cvvErr != null) return cvvErr;

        return null;
    }

    // =========================================================================
    // DEBIT CARD VALIDATION (moved from PaymentValidationUtil)
    // =========================================================================

    public String validateDebitCard(String cardNumber, String bankName,
                                     String expiry, String cvv) {
        String cardErr = validateCardNumber(cardNumber);
        if (cardErr != null) return cardErr;

        if (!luhnCheck(cardNumber.replaceAll("[\\s-]", "")))
            return "Please verify and re-enter your card number";

        if (bankName == null || bankName.trim().isEmpty())
            return "Bank name cannot be empty.";

        bankName = bankName.trim();
        if (bankName.length() < 2)
            return "Bank name must be at least 2 characters.";

        if (bankName.length() > 50)
            return "Bank name is too long (maximum 50 characters).";

        if (!bankName.matches("[a-zA-Z0-9 ]+"))
            return "Bank name contains invalid characters. Use only alphanumeric characters and spaces.";

        String expiryErr = validateExpiry(expiry);
        if (expiryErr != null) return expiryErr;

        String cvvErr = validateCVV(cvv);
        if (cvvErr != null) return cvvErr;

        return null;
    }

    // =========================================================================
    // HELPER VALIDATION METHODS (moved from PaymentValidationUtil)
    // =========================================================================

    private String validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty())
            return "Please verify and re-enter your card number";

        cardNumber = cardNumber.trim();
        String cleanCardNumber = cardNumber.replaceAll("[\\s-]", "");

        if (cleanCardNumber.length() != 16 || !cleanCardNumber.matches("[0-9]+")
                || !CARD_NUMBER_PATTERN.matcher(cardNumber).matches())
            return "Please verify and re-enter your card number";

        return null;
    }

    private String validateCVV(String cvv) {
        if (cvv == null || cvv.trim().isEmpty())
            return "CVV cannot be empty.";

        cvv = cvv.trim();

        if (!cvv.matches("[0-9]+"))
            return "CVV must contain only digits.";

        if (!CVV_PATTERN.matcher(cvv).matches())
            return "CVV must be 3 or 4 digits.";

        return null;
    }

    private String validateExpiry(String expiry) {
        if (expiry == null || expiry.trim().isEmpty())
            return "Expiry date cannot be empty. Format: MM/YY";

        expiry = expiry.trim();

        if (!expiry.matches("[0-9]{2}/[0-9]{2}"))
            return "Expiry date format is incorrect. Use MM/YY (e.g., 08/27)";

        if (!EXPIRY_PATTERN.matcher(expiry).matches())
            return "Invalid expiry date. Month must be 01-12, format: MM/YY (e.g., 08/27)";

        String[] parts = expiry.split("/");
        int month = Integer.parseInt(parts[0]);
        int year  = 2000 + Integer.parseInt(parts[1]);

        java.time.LocalDate now     = java.time.LocalDate.now();
        java.time.LocalDate expires = java.time.LocalDate.of(year, month, 1)
                                        .plusMonths(1).minusDays(1);

        if (now.isAfter(expires))
            return "This card has expired (expiry: " + expiry + "). Please use a valid card.";

        return null;
    }

    // =========================================================================
    // LUHN ALGORITHM (moved from PaymentValidationUtil)
    // =========================================================================

    private boolean luhnCheck(String number) {
        int sum     = 0;
        boolean alt = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';

            if (alt) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alt = !alt;
        }

        return (sum % 10 == 0);
    }

    // =========================================================================
    // MASKING UTILITIES (moved from PaymentValidationUtil)
    // =========================================================================

    public String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "INVALID";
        String cleaned  = cardNumber.replaceAll("[\\s-]", "");
        if (cleaned.length() < 4) return "INVALID";
        String lastFour = cleaned.substring(cleaned.length() - 4);
        return "XXXX XXXX XXXX " + lastFour;
    }

    public String maskUPIId(String upiId) {
        if (upiId == null || !upiId.contains("@")) return "INVALID";
        String[] parts = upiId.split("@");
        String handle  = parts[0];
        String bank    = parts[1];
        if (handle.length() <= 3) return "***@" + bank;
        return handle.substring(0, 3) + "***@" + bank;
    }

    // =========================================================================
    // HELPER
    // =========================================================================

    private String formatPaymentMethod(String method) {
        if ("UPI".equalsIgnoreCase(method))         return "UPI";
        if ("CREDIT_CARD".equalsIgnoreCase(method)) return "Credit Card";
        if ("DEBIT_CARD".equalsIgnoreCase(method))  return "Debit Card";
        return method;
    }
}