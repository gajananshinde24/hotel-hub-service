package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtils {
	
	  @Autowired
	  private JavaMailSender mailSender;
	
	 public void sendEmail(String to, String subject, String body) {
	        MimeMessage mimeMessage = mailSender.createMimeMessage();
	        try {
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	            helper.setTo(to);
	            helper.setSubject(subject);
	            helper.setText(body, true); // Set to true if the body contains HTML
	            mailSender.send(mimeMessage);
	        } catch (Exception e) {
	            e.printStackTrace();  
	        }
	    }

}
