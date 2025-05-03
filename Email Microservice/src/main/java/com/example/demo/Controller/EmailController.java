package com.example.demo.Controller;

import com.example.demo.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sendemail")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public String checkout(@RequestParam String userEmail) {
        // Extract the prefix of the email (part before '@')
        String prefix = userEmail.split("@")[0];

        // Create the dynamic email body with HTML
        String subject = "Order Confirmation";
        String body = "<html>" +
                "<body>" +
                "<p>Dear " + prefix + ",</p>" +
                "<p>Thank you for your purchase! Your order has been confirmed.</p>" +
                "<p>To view your order history, please click the button below:</p>" +
                "<a href='http://localhost' style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #007bff; text-decoration: none; border-radius: 5px;'>View Order History</a>" +
                "</body>" +
                "</html>";

        // Send the email
        emailService.sendCheckoutEmail(userEmail, subject, body);

        return "Checkout successful and email sent.";
    }

}
