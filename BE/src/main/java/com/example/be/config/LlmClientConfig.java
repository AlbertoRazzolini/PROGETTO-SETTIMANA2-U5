package com.example.be.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Client verso OpenRouter, che espone il protocollo di OpenAI
 * (/chat/completions, autenticazione Bearer). Costruito una volta e riusato.
 */
@Configuration
public class LlmClientConfig {

	private static final Logger log = LoggerFactory.getLogger(LlmClientConfig.class);

	@Bean
	public RestClient llmClient(RestClient.Builder builder,
			@Value("${app.llm.base-url}") String baseUrl,
			@Value("${app.llm.api-key:}") String apiKey) {

		if (apiKey == null || apiKey.isBlank()) {
			log.warn("app.llm.api-key non impostata: le chiamate riceveranno 401");
		}
		log.info("client LLM verso {}", baseUrl);

		return builder
				.baseUrl(baseUrl)
				.defaultHeader("Authorization", "Bearer " + apiKey)
				.build();
	}
}
