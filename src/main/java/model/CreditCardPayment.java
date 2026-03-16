package model;


public class CreditCardPayment extends Payment {

    private String cardNumber;
    private String cardHolderName;

    // Constructor when retrieving from database
    public CreditCardPayment(int paymentId, double amount, String paymentDate, String status, int bookingId,
                             String cardNumber, String cardHolderName) {

        super(paymentId, amount, paymentDate, status, bookingId, "CREDIT_CARD");

        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    // Constructor when creating new payment
    public CreditCardPayment(double amount, String paymentDate, String status, int bookingId,
                             String cardNumber, String cardHolderName) {

        super(amount, paymentDate, status, bookingId, "CREDIT_CARD");

        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }
}