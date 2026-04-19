/*
 * Author         : Subhashree R
 * Created Date   : 10-Apr-2026
 * Description    : Utility class for validating payment formats (UPI, Card) and providing secure data masking.
 * Module         : Payment Module
 * Java version   : 24
 */
package util;

import java.util.regex.Pattern;
import service.PaymentService;

/**
 * Payment Validation Utility
 * Validates UPI IDs, credit card details, and debit card details
 * with comprehensive error checking before payment processing.
 */
public class PaymentValidationUtil {

    // =========================================================================
    // REGEX PATTERNS FOR VALIDATION
    // =========================================================================

    // UPI ID Pattern: name@bankhandle
    private static final Pattern UPI_PATTERN =
        Pattern.compile("^[a-zA-Z0-9._-]{3,50}@[a-zA-Z]{2,20}$");

    // 16-digit card number (with optional spaces or hyphens)
    private static final Pattern CARD_NUMBER_PATTERN =
        Pattern.compile("^[0-9]{4}[\\s-]?[0-9]{4}[\\s-]?[0-9]{4}[\\s-]?[0-9]{4}$");

    // Cardholder/Bank name: letters and spaces only
    private static final Pattern NAME_PATTERN =
        Pattern.compile("^[a-zA-Z ]{2,50}$");

    // CVV: 3 or 4 digits
    private static final Pattern CVV_PATTERN =
        Pattern.compile("^[0-9]{3,4}$");

    // Expiry date: MM/YY format
    private static final Pattern EXPIRY_PATTERN =
        Pattern.compile("^(0[1-9]|1[0-2])/([0-9]{2})$");

    // =========================================================================
    // UPI VALIDATION
    // =========================================================================

    
    public static String validateUpiId(String upiId) {
        if (upiId == null || upiId.trim().isEmpty())
            return "UPI ID cannot be empty.";

        upiId = upiId.trim();

        if (upiId.length() < 3)
            return "UPI ID is too short (minimum 3 characters).";

        if (upiId.length() > 50)
            return "UPI ID is too long (maximum 50 characters).";

        if (!upiId.contains("@"))
            return "Invalid UPI ID format. UPI ID must contain '@' symbol.";

        String[] parts = upiId.split("@");
        if (parts.length != 2)
            return "Invalid UPI ID format. Expected exactly one '@' symbol.";

        String handle = parts[0];
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
    // CREDIT CARD VALIDATION
    // =========================================================================

    public static String validateCreditCard(String cardNumber, String holderName,
                                             String expiry, String cvv) {
        // 1. Validate card number
        String cardErr = validateCardNumber(cardNumber);
        if (cardErr != null) return cardErr;

        // 2. Luhn check (industry standard for card validation)
        if (!PaymentService.luhnCheck(cardNumber.replaceAll("[\\s-]", "")))
            return "Please verify and re-enter your card number";

        // 3. Validate cardholder name
        if (holderName == null || holderName.trim().isEmpty())
            return "Cardholder name cannot be empty.";

        holderName = holderName.trim();
        if (holderName.length() < 2)
            return "Cardholder name must be at least 2 characters.";

        if (holderName.length() > 50)
            return "Cardholder name is too long (maximum 50 characters).";

        if (!NAME_PATTERN.matcher(holderName).matches())
            return "Cardholder name must contain only letters and spaces.";

        // 4. Validate expiry date
        String expiryErr = validateExpiry(expiry);
        if (expiryErr != null) return expiryErr;

        // 5. Validate CVV
        String cvvErr = validateCVV(cvv);
        if (cvvErr != null) return cvvErr;

        return null; 
    }

    // =========================================================================
    // DEBIT CARD VALIDATION
    // =========================================================================

  
    public static String validateDebitCard(String cardNumber, String bankName,
                                            String expiry, String cvv) {
        // 1. Validate card number
        String cardErr = validateCardNumber(cardNumber);
        if (cardErr != null) return cardErr;

        // 2. Luhn check (industry standard for card validation)
        if (!PaymentService.luhnCheck(cardNumber.replaceAll("[\\s-]", "")))
            return "Please verify and re-enter your card number";

        // 3. Validate bank name
        if (bankName == null || bankName.trim().isEmpty())
            return "Bank name cannot be empty.";

        bankName = bankName.trim();
        if (bankName.length() < 2)
            return "Bank name must be at least 2 characters.";

        if (bankName.length() > 50)
            return "Bank name is too long (maximum 50 characters).";

        // Check for special characters (bank names should be mostly alphanumeric)
        if (!bankName.matches("[a-zA-Z0-9 ]+"))
            return "Bank name contains invalid characters. Use only alphanumeric characters and spaces.";

        // 4. Validate expiry date
        String expiryErr = validateExpiry(expiry);
        if (expiryErr != null) return expiryErr;

        // 5. Validate CVV
        String cvvErr = validateCVV(cvv);
        if (cvvErr != null) return cvvErr;

        return null; 
    }

    // =========================================================================
    // HELPER VALIDATION METHODS
    // =========================================================================
    private static String validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty())
            return "Please verify and re-enter your card number";

        cardNumber = cardNumber.trim();
        String cleanCardNumber = cardNumber.replaceAll("[\\s-]", "");

        if (cleanCardNumber.length() != 16 || !cleanCardNumber.matches("[0-9]+") || !CARD_NUMBER_PATTERN.matcher(cardNumber).matches())
            return "Please verify and re-enter your card number";

        return null;
    }

  
     //Validates CVV format
    
    private static String validateCVV(String cvv) {
        if (cvv == null || cvv.trim().isEmpty())
            return "CVV cannot be empty.";

        cvv = cvv.trim();

        if (!cvv.matches("[0-9]+"))
            return "CVV must contain only digits.";

        if (!CVV_PATTERN.matcher(cvv).matches())
            return "CVV must be 3 or 4 digits.";

        return null;
    }

    
     //Validates expiry date format and checks if card is not expired
   
    private static String validateExpiry(String expiry) {
        if (expiry == null || expiry.trim().isEmpty())
            return "Expiry date cannot be empty. Format: MM/YY";

        expiry = expiry.trim();

        if (!expiry.matches("[0-9]{2}/[0-9]{2}"))
            return "Expiry date format is incorrect. Use MM/YY (e.g., 08/27)";

        if (!EXPIRY_PATTERN.matcher(expiry).matches())
            return "Invalid expiry date. Month must be 01-12, format: MM/YY (e.g., 08/27)";

        // Check if card is not expired
        String[] parts = expiry.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = 2000 + Integer.parseInt(parts[1]);

        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate expires = java.time.LocalDate.of(year, month, 1)
                                        .plusMonths(1).minusDays(1);

        if (now.isAfter(expires))
            return "This card has expired (expiry: " + expiry + "). Please use a valid card.";

        return null;
    }

    // =========================================================================
    // SUMMARY VALIDATION METHOD
    // =========================================================================

  
    public static String validateAmount(double amount) {
        if (amount <= 0)
            return "Payment amount must be greater than 0.";

        if (amount > 999999.99)
            return "Payment amount exceeds maximum limit.";

        return null;
    }

    
     // Gets a summary of validation for logging/debugging
    
    public static String getValidationSummary(String type, boolean isValid) {
        String status = isValid ? "✓ VALID" : "✗ INVALID";
        return String.format("[%s] %s Payment Validation: %s", 
                           java.time.LocalDateTime.now().format(
                                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                           type, status);
    }

    // =========================================================================
    // MASKING UTILITIES
    // =========================================================================

    public static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "INVALID";
        String cleaned  = cardNumber.replaceAll("[\\s-]", "");
        if (cleaned.length() < 4) return "INVALID";
        String lastFour = cleaned.substring(cleaned.length() - 4);
        return "XXXX XXXX XXXX " + lastFour;
    }

    public static String maskUPIId(String upiId) {
        if (upiId == null || !upiId.contains("@")) return "INVALID";
        String[] parts  = upiId.split("@");
        String handle   = parts[0];
        String bank     = parts[1];
        if (handle.length() <= 3) return "***@" + bank;
        return handle.substring(0, 3) + "***@" + bank;
    }
}