package com.bbs.service;

import com.bbs.domain.User;
import com.bbs.exception.AccessDeniedException;
import com.bbs.exception.InvalidJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * jwt 관련 서비스
 */
@Service
public class JwtService {

	/**
	 * 랜덤 키 생성
	 */
	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

	/**
	 * 발행자 정보 (검증용)
	 */
	private static final String ISSUER = "myapp";

	/**
	 * jwt 토큰 생성
	 *
	 * @param user 사용자 정보 객체
	 * @return JWT
	 */
	public String generateAccessToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("sub", user.getAccount());
		claims.put("id", user.getId());
		return Jwts.builder()
			.setClaims(claims)
			.setIssuer(ISSUER)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // 120min
			.signWith(key)
			.compact();
	}

	/**
	 * JWT 분석
	 *
	 * @param token JWT
	 * @return Claims
	 */
	public Claims parseToken(String token) {
		try {
			Claims claims =  Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
			return claims;
		} catch (Exception e) {
			throw new InvalidJwtException();
		}
	}

	/**
	 * JWT 검증
	 *
	 * @param token JWT
	 * @return boolean
	 */
	public boolean validateToken(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			String issuer = claimsJws.getBody().getIssuer();
			if (!ISSUER.equals(issuer)) {
				throw new AccessDeniedException();
			}
			return true;
		} catch (ExpiredJwtException e) {
			throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "토큰 만료");
		} catch (Exception e) {
			throw new InvalidJwtException();
		}
	}

}
