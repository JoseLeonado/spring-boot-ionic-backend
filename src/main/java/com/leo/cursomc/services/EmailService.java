package com.leo.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.leo.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
