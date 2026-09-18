package com.example.be.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final SecretKey chiave;
	private final long scadenzaMillis;

	public JwtService(@Value("${app.jwt.secret}") String secret,
			@Value("${app.jwt.expiration}") long scadenzaMillis) {
		this.chiave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.scadenzaMillis = scadenzaMillis;
	}

	public String genera(String username) {
		Date ora = new Date();
		Date scadenza = new Date(ora.getTime() + scadenzaMillis);

		return Jwts.builder()
				.subject(username)
				.issuedAt(ora)
				.expiration(scadenza)
				.signWith(chiave)
				.compact();
	}

	public String estraiUsername(String token) {
		return Jwts.parser()
				.verifyWith(chiave)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

	public boolean valido(String token) {
		try {
			Jwts.parser()
					.verifyWith(chiave)
					.build()
					.parseSignedClaims(token);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}
}
