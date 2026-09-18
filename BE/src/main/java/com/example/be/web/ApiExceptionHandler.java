package com.example.be.web;

import com.example.be.exception.DatoGiaRegistratoException;
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
}
