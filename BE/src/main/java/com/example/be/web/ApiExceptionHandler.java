package com.example.be.web;

import com.example.be.exception.AccessoNegatoException;
import com.example.be.exception.DatoGiaRegistratoException;
import com.example.be.exception.EmailNonInviataException;
import com.example.be.exception.RisorsaNonTrovataException;
import com.example.be.exception.ServizioIaNonDisponibileException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleValidazione(MethodArgumentNotValidException ex) {
		List<String> messaggi = ex.getBindingResult().getFieldErrors().stream()
				.map(errore -> errore.getDefaultMessage())
				.toList();
		return ApiError.badRequest(messaggi);
	}

	@ExceptionHandler(DatoGiaRegistratoException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiError handleDatoGiaRegistrato(DatoGiaRegistratoException ex) {
		return ApiError.conflict(List.of(ex.getMessage()));
	}

	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiError handleCredenzialiErrate(BadCredentialsException ex) {
		return ApiError.unauthorized(List.of("credenziali non valide"));
	}

	@ExceptionHandler(RisorsaNonTrovataException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiError handleRisorsaNonTrovata(RisorsaNonTrovataException ex) {
		return ApiError.notFound(List.of(ex.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ApiError handleIllegalArgument(IllegalArgumentException ex) {
		return ApiError.badRequest(List.of(ex.getMessage()));
	}

	@ExceptionHandler(AccessoNegatoException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiError handleAccessoNegato(AccessoNegatoException ex) {
		return ApiError.forbidden(List.of(ex.getMessage()));
	}

	@ExceptionHandler(ServizioIaNonDisponibileException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ApiError handleServizioIaNonDisponibile(ServizioIaNonDisponibileException ex) {
		return ApiError.serviceUnavailable(List.of(ex.getMessage()));
	}

	@ExceptionHandler(EmailNonInviataException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public ApiError handleEmailNonInviata(EmailNonInviataException ex) {
		return ApiError.serviceUnavailable(List.of(ex.getMessage()));
	}
}
