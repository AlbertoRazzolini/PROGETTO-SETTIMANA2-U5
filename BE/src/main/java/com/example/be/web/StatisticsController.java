package com.example.be.web;

import com.example.be.dto.StatisticsResponse;
import com.example.be.service.StatisticsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatisticsController {

	private final StatisticsService statisticsService;

	public StatisticsController(StatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}

	@GetMapping("/me")
	public StatisticsResponse me(Authentication autenticazione) {
		return statisticsService.calcola(autenticazione.getName());
	}

	@PostMapping("/me/email")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inviaViaEmail(Authentication autenticazione) {
		statisticsService.inviaViaEmail(autenticazione.getName());
	}
}
