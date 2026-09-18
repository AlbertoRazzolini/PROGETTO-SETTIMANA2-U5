package com.example.be.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender mailSender;
	private final String mittente;

	public EmailService(JavaMailSender mailSender, @Value("${app.mail.mittente}") String mittente) {
		this.mailSender = mailSender;
		this.mittente = mittente;
	}

	public void inviaHtml(String destinatario, String oggetto, String testoAlternativo, String html)
			throws MessagingException {
		MimeMessage messaggio = mailSender.createMimeMessage();
		var helper = new MimeMessageHelper(messaggio, true, "UTF-8");
		helper.setFrom(mittente);
		helper.setTo(destinatario);
		helper.setSubject(oggetto);
		helper.setText(testoAlternativo, html);

		mailSender.send(messaggio);
	}
}
