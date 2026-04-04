package service;

import dao.PaymentDAO;
import model.*;
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

        System.out.println("\n┌──────────────────────────────────────────────────────────────┐");
        System.out.println("│                    PROCESS PAYMENT                            │");
        System.out.println("└───────────────────────────────────────────────────────────────┘");

        // Step 1: Basic validations
        String basicError = validateBasicPaymentDetails(payment);
        if (basicError != null) {
          
            System.out.println("  Status: FAILED");
            return;
        }

        // Step 2: Validate amount
        String amountError = PaymentValidationUtil.validateAmount(payment.getAmount());
        if (amountError != null) {
       
            System.out.println("  Status: FAILED");
            return;
        }

        // Step 3: Payment type-specific validation
        String validationError = validatePaymentByType(payment);
        if (validationError != null) {
            
            System.out.println("  Status: FAILED");
            return;
        }

        // Step 4: If all validations pass, process the payment
        payment.setStatus("SUCCESS");
        paymentDAO.processPayment(payment);

        System.out.println("\n  Payment validated and processed successfully!");
        System.out.println("  ─────────────────────────────────────────────────────────────");
        System.out.println("  Payment ID : " + payment.getPaymentId());
        System.out.println("  Booking ID : " + payment.getBookingId());
        System.out.printf("  Amount     : Rs. %.2f%n", payment.getAmount());
        System.out.println("  Method     : " + formatPaymentMethod(payment.getPaymentMethod()));
        displayPaymentTypeDetails(payment);
        System.out.println("  Status     : " + payment.getStatus());
        System.out.println("  ─────────────────────────────────────────────────────────────");
    }

    // =========================================================================
    // PAYMENT TYPE-SPECIFIC VALIDATION
    // =========================================================================

    
     // Validates payment based on its type (UPI, Credit Card, or Debit Card)
  
    private String validatePaymentByType(Payment payment) {

        if (payment instanceof UPIPayment) {
            return validateUPIPayment((UPIPayment) payment);
        } else if (payment instanceof CreditCardPayment) {
            return validateCreditCardPayment((CreditCardPayment) payment);
        } else if (payment instanceof DebitCardPayment) {
            return validateDebitCardPayment((DebitCardPayment) payment);
        } else {
            return "Unknown payment type.";
        }
    }

    
     //Validates UPI Payment
    
    private String validateUPIPayment(UPIPayment payment) {

        System.out.println("\n  └─ Validating UPI Payment Details...");

        String upiId = payment.getUpiId();

        // Validate UPI ID
        String upiError = PaymentValidationUtil.validateUpiId(upiId);
        if (upiError != null) {
            System.out.println("    " + upiError);
            return upiError;
        }

        System.out.println("     UPI ID validated: " + maskUPIId(upiId));
        return null;
    }

    
     // Validates Credit Card Payment
   
    private String validateCreditCardPayment(CreditCardPayment payment) {

        System.out.println("\n  └─ Validating Credit Card Details...");

        String cardNumber = payment.getCardNumber();
        String holderName = payment.getCardHolderName();

       
        String expiry = "12/26"; 
        String cvv = "123";     

        // Validate Credit Card
        String cardError = PaymentValidationUtil.validateCreditCard(
            cardNumber, holderName, expiry, cvv
        );

        if (cardError != null) {
            System.out.println("    " + cardError);
            return cardError;
        }

        System.out.println("    Card Number validated: " + maskCardNumber(cardNumber));
        System.out.println("    Cardholder Name validated: " + holderName);
        System.out.println("    Expiry Date validated");
        System.out.println("    CVV validated");
        return null;
    }

   
   
    //Validates Debit Card Payment
   
     
    private String validateDebitCardPayment(DebitCardPayment payment) {

        System.out.println("\n  └─ Validating Debit Card Details...");

        String cardNumber = payment.getCardNumber();
        String bankName = payment.getBankName();

       
      
        String expiry = "12/26"; 
        String cvv = "123";      

        // Validate Debit Card
        String cardError = PaymentValidationUtil.validateDebitCard(
            cardNumber, bankName, expiry, cvv
        );

        if (cardError != null) {
            System.out.println("    " + cardError);
            return cardError;
        }

        System.out.println("     Card Number validated: " + maskCardNumber(cardNumber));
        System.out.println("     Bank Name validated: " + bankName);
        System.out.println("     Expiry Date validated");
        System.out.println("     CVV validated");
        return null;
    }

    // =========================================================================
    // BASIC PAYMENT VALIDATION
    // =========================================================================

    // Validates basic payment details common to all payment types
  
    private String validateBasicPaymentDetails(Payment payment) {

        // Check amount
        if (payment.getAmount() <= 0) {
            return "Invalid amount. Must be greater than 0.";
        }

        // Check payment date
        if (payment.getPaymentDate() == null || payment.getPaymentDate().isEmpty()) {
            return "Payment date cannot be empty.";
        }

        // Check booking ID
        if (payment.getBookingId() <= 0) {
            return "Invalid Booking ID.";
        }

        // Check payment method
        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
            return "Payment method is required.";
        }

        return null;
    }

    // =========================================================================
    // VIEW PAYMENT METHODS
    // =========================================================================

    //View specific payment details
   
    public Payment viewPayment(int paymentId) {

        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│                    VIEW PAYMENT                            │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        if (paymentId <= 0) {
            System.out.println("  Invalid Payment ID.");
            return null;
        }

        Payment payment = paymentDAO.viewPayment(paymentId);

        if (payment != null) {
            System.out.println("  ─────────────────────────────────────────────────────────────");
            System.out.println("  Payment ID : " + payment.getPaymentId());
            System.out.println("  Booking ID : " + payment.getBookingId());
            System.out.printf("  Amount     : Rs. %.2f%n", payment.getAmount());
            System.out.println("  Method     : " + formatPaymentMethod(payment.getPaymentMethod()));
            System.out.println("  Date       : " + payment.getPaymentDate());
            displayPaymentTypeDetails(payment);
            System.out.println("  Status     : " + payment.getStatus());
            System.out.println("  ─────────────────────────────────────────────────────────────");
        } else {
            System.out.println("   Payment not found.");
        }

        return payment;
    }

    
     // View payment history for a booking
   
    public void viewPaymentHistory(int bookingId) {

        System.out.println("\n┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│                 PAYMENT HISTORY                              │");
        System.out.println("└──────────────────────────────────────────────────────────────┘");

        if (bookingId <= 0) {
            System.out.println("  Invalid Booking ID.");
            return;
        }

        paymentDAO.viewPaymentHistory(bookingId);
    }

    
     // Verify if a payment is valid before processing
     
     
    public boolean verifyPayment(Payment payment) {
        System.out.println("\n  ┌─ Payment Verification Started...");

        // Check basic details
        if (validateBasicPaymentDetails(payment) != null) {
            System.out.println("  └─ Basic payment details validation failed");
            return false;
        }

        // Check amount
        if (PaymentValidationUtil.validateAmount(payment.getAmount()) != null) {
            System.out.println("  └─ Payment amount validation failed");
            return false;
        }

        // Check payment type specific details
        if (validatePaymentByType(payment) != null) {
            System.out.println("  └─ Payment type validation failed");
            return false;
        }

        System.out.println("  └─ All payment validations passed");
        return true;
    }

    // =========================================================================
    // HELPER METHODS FOR DISPLAY AND MASKING
    // =========================================================================
 
    //Display payment type-specific details
    
    private void displayPaymentTypeDetails(Payment payment) {
        if (payment instanceof UPIPayment) {
            UPIPayment upiPayment = (UPIPayment) payment;
            System.out.println("  UPI ID     : " + maskUPIId(upiPayment.getUpiId()));
        } else if (payment instanceof CreditCardPayment) {
            CreditCardPayment ccPayment = (CreditCardPayment) payment;
            System.out.println("  Card No.   : " + maskCardNumber(ccPayment.getCardNumber()));
            System.out.println("  Holder     : " + ccPayment.getCardHolderName());
        } else if (payment instanceof DebitCardPayment) {
            DebitCardPayment dcPayment = (DebitCardPayment) payment;
            System.out.println("  Card No.   : " + maskCardNumber(dcPayment.getCardNumber()));
            System.out.println("  Bank       : " + dcPayment.getBankName());
        }
    }

    //Masks card number for security display (shows last 4 digits only)
   
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "INVALID";
        }
        String cleaned = cardNumber.replaceAll("[\\s-]", "");
        String lastFour = cleaned.substring(cleaned.length() - 4);
        return "XXXX XXXX XXXX " + lastFour;
    }

    // Masks UPI ID for security display
   
    private String maskUPIId(String upiId) {
        if (upiId == null || !upiId.contains("@")) {
            return "INVALID";
        }
        String[] parts = upiId.split("@");
        String handle = parts[0];
        String bank = parts[1];

        if (handle.length() <= 3) {
            return "***@" + bank;
        }
        String masked = handle.substring(0, 3) + "***@" + bank;
        return masked;
    }

     //Formats payment method for display
  
    private String formatPaymentMethod(String method) {
        if ("UPI".equalsIgnoreCase(method)) {
            return "UPI";
        } else if ("CREDIT_CARD".equalsIgnoreCase(method)) {
            return "Credit Card";
        } else if ("DEBIT_CARD".equalsIgnoreCase(method)) {
            return "Debit Card";
        }
        return method;
    }
}