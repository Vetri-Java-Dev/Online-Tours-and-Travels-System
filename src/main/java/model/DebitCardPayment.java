package model;

public class DebitCardPayment extends Payment {

    private String cardNumber;
    private String bankName;

    // Constructor when retrieving from database
    public DebitCardPayment(int paymentId, double amount, String paymentDate, String status, int bookingId,
                            String cardNumber, String bankName) {

        super(paymentId, amount, paymentDate, status, bookingId, "DEBIT_CARD");

        this.cardNumber = cardNumber;
        this.bankName = bankName;
    }

    // Constructor when creating new payment
    public DebitCardPayment(double amount, String paymentDate, String status, int bookingId,
                            String cardNumber, String bankName) {

        super(amount, paymentDate, status, bookingId, "DEBIT_CARD");

        this.cardNumber = cardNumber;
        this.bankName = bankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getBankName() {
        return bankName;
    }
}