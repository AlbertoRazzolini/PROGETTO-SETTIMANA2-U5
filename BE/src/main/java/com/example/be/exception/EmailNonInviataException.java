package com.example.be.exception;

public class EmailNonInviataException extends RuntimeException {

	public EmailNonInviataException(String messaggio, Throwable causa) {
		super(messaggio, causa);
	}
}
