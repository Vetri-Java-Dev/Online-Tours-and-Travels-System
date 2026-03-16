
package controller;

import dao.PaymentDAO;
import model.Payment;

public class PaymentController {

    private PaymentDAO paymentDAO = new PaymentDAO();

    public void processPayment(Payment payment) {

        payment.setStatus("SUCCESS");

        paymentDAO.processPayment(payment);
    }

    public void viewPayment(int paymentId) {

        Payment payment = paymentDAO.viewPayment(paymentId);

        if(payment != null) {

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

        paymentDAO.viewPaymentHistory(bookingId);
    }
}
