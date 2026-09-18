package com.example.be.web;

import java.time.Instant;
import java.util.List;

public record ApiError(Instant timestamp, int status, String error, List<String> messages) {

	public static ApiError badRequest(List<String> messages) {
		return new ApiError(Instant.now(), 400, "Bad Request", messages);
	}

	public static ApiError conflict(List<String> messages) {
		return new ApiError(Instant.now(), 409, "Conflict", messages);
	}

	public static ApiError unauthorized(List<String> messages) {
		return new ApiError(Instant.now(), 401, "Unauthorized", messages);
	}

	public static ApiError notFound(List<String> messages) {
		return new ApiError(Instant.now(), 404, "Not Found", messages);
	}

	public static ApiError forbidden(List<String> messages) {
		return new ApiError(Instant.now(), 403, "Forbidden", messages);
	}

	public static ApiError serviceUnavailable(List<String> messages) {
		return new ApiError(Instant.now(), 503, "Service Unavailable", messages);
	}
}
