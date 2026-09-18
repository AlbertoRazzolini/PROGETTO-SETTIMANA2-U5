package com.example.be.exception;

public class ServizioIaNonDisponibileException extends RuntimeException {

	public ServizioIaNonDisponibileException(String messaggio) {
		super(messaggio);
	}
}
