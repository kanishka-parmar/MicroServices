package com.example.cartAndOrder.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendCheckoutEmail(String userEmail,String userName) {
       try {
           String prefix = userName;

           String subject = "Order Confirmation";
           String body = "Hello, "+ prefix + "\nThank you for your purchase! Your order has been confirmed.";

           SimpleMailMessage message = new SimpleMailMessage();
           message.setTo(userEmail);
           message.setSubject(subject);
           message.setText(body);
           message.setFrom("krishnavineeth0304@gmail.com");  // Use your own email here

           mailSender.send(message);
           System.out.println("Email sent successfully.");
           return true;
       }catch (Exception e){
           System.out.println("Email not sent");
           return false;
       }
    }
}
