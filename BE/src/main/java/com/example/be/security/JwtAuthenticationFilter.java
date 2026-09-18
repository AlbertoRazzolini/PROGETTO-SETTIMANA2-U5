package com.example.be.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Legge il JWT dall'header Authorization e, se valido, popola il
 * SecurityContext. Sulle richieste senza token (o con token non valido) non
 * blocca: lascia decidere alle regole di autorizzazione piu' avanti.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwt;
	private final CustomUserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtService jwt, CustomUserDetailsService userDetailsService) {
		this.jwt = jwt;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);

			if (jwt.valido(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
				String username = jwt.estraiUsername(token);
				UserDetails utente = userDetailsService.loadUserByUsername(username);

				var autenticazione = new UsernamePasswordAuthenticationToken(
						utente, null, utente.getAuthorities());
				autenticazione.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(autenticazione);
			}
		}

		filterChain.doFilter(request, response);
	}
}
