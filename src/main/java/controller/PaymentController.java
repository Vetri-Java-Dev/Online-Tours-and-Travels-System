package controller;

import model.Payment;
import service.PaymentService;

public class PaymentController {

    private PaymentService paymentService = new PaymentService();

    public void processPayment(Payment payment) {
        paymentService.processPayment(payment);
    }

    public void viewPayment(int paymentId) {

        Payment payment = paymentService.viewPayment(paymentId);

        if (payment != null) {

            System.out.println("Payment ID: " + payment.getPaymentId());
            System.out.println("Amount: " + payment.getAmount());
            System.out.println("Date: " + payment.getPaymentDate());
            System.out.println("Method: " + payment.getPaymentMethod());
            System.out.println("Status: " + payment.getStatus());

        } else {
            System.out.println("Payment not found");
        }
    }

    public void viewPaymentHistory(int bookingId) {
        paymentService.viewPaymentHistory(bookingId);
    }
}