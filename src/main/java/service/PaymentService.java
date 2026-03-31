package service;

import dao.PaymentDAO;
import model.Payment;

public class PaymentService {

    private PaymentDAO paymentDAO = new PaymentDAO();

    public void processPayment(Payment payment) {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           PROCESS PAYMENT           │");
        System.out.println("└─────────────────────────────────────┘");

        if (payment.getAmount() <= 0) {
            System.out.println("  Invalid amount. Must be greater than 0.");
            return;
        }

        if (payment.getPaymentDate() == null || payment.getPaymentDate().isEmpty()) {
            System.out.println("  Payment date cannot be empty.");
            return;
        }

        if (payment.getBookingId() <= 0) {
            System.out.println("  Invalid Booking ID.");
            return;
        }

        if (payment.getPaymentMethod() == null || payment.getPaymentMethod().isEmpty()) {
            System.out.println("  Payment method is required.");
            return;
        }

        payment.setStatus("SUCCESS");
        paymentDAO.processPayment(payment);

        System.out.println("  Payment processed successfully!");
        System.out.println("  ─────────────────────────────────────");
        System.out.println("  Payment ID : " + payment.getPaymentId());
        System.out.println("  Booking ID : " + payment.getBookingId());
        System.out.printf ("  Amount     : Rs. %.2f%n", payment.getAmount());
        System.out.println("  Method     : " + payment.getPaymentMethod());
        System.out.println("  Status     : " + payment.getStatus());
        System.out.println("  ─────────────────────────────────────");
    }

    public Payment viewPayment(int paymentId) {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│             VIEW PAYMENT            │");
        System.out.println("└─────────────────────────────────────┘");

        if (paymentId <= 0) {
            System.out.println("  Invalid Payment ID.");
            return null;
        }

        Payment payment = paymentDAO.viewPayment(paymentId);

        if (payment != null) {
            System.out.println("  ─────────────────────────────────────");
            System.out.println("  Payment ID : " + payment.getPaymentId());
            System.out.println("  Booking ID : " + payment.getBookingId());
            System.out.printf ("  Amount     : Rs. %.2f%n", payment.getAmount());
            System.out.println("  Method     : " + payment.getPaymentMethod());
            System.out.println("  Status     : " + payment.getStatus());
            System.out.println("  ─────────────────────────────────────");
        } else {
            System.out.println("  Payment not found.");
        }

        return payment;
    }

    public void viewPaymentHistory(int bookingId) {

        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│           PAYMENT HISTORY           │");
        System.out.println("└─────────────────────────────────────┘");

        if (bookingId <= 0) {
            System.out.println("  Invalid Booking ID.");
            return;
        }

        paymentDAO.viewPaymentHistory(bookingId);
    }
}