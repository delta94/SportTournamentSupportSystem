package doan2020.SportTournamentSupportSystem.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import doan2020.SportTournamentSupportSystem.entity.UserEntity;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class JwtService {

	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
	
	public static final String USERNAME = "username";
	public static final String ID = "id";
	public static final String SECRET_KEY = "JWTSuperSecretKey";
	public static final int EXPIRE_TIME = 86400000;

	public String generateTokenLogin(UserEntity user) {
		String token = null;
		try {
			
			return Jwts.builder()
					.setSubject((user.getUsername()))
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
					.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
					.compact();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
}
