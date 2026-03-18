package service;

import dao.PaymentDAO;
import model.Payment;

public class PaymentService {

    private PaymentDAO paymentDAO = new PaymentDAO();

    // Process Payment
    public void processPayment(Payment payment) {

        // Validation
        if (payment.getAmount() <= 0) {
            System.out.println("Invalid amount. Must be greater than 0");
            return;
        }

        if (payment.getPaymentDate() == null || payment.getPaymentDate().isEmpty()) {
            System.out.println("Payment date cannot be empty");
            return;
        }

        if (payment.getBookingId() <= 0) {
            System.out.println("Invalid Booking ID");
            return;
        }

        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
            System.out.println("Payment method is required");
            return;
        }

        // Business Logic
        payment.setStatus("SUCCESS");

        // DAO Call
        paymentDAO.processPayment(payment);
    }

    // View Single Payment
    public Payment viewPayment(int paymentId) {

        if (paymentId <= 0) {
            System.out.println("Invalid Payment ID");
            return null;
        }

        return paymentDAO.viewPayment(paymentId);
    }

    // View Payment History
    public void viewPaymentHistory(int bookingId) {

        if (bookingId <= 0) {
            System.out.println("Invalid Booking ID");
            return;
        }

        paymentDAO.viewPaymentHistory(bookingId);
    }
}