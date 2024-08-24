package org.sty.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service // Kjo tregon që kjo klasë është një shërbim Spring që mund të injektohet në të tjera komponente.
public class JWTService {

    private String secretKey; // Çelësi i fshehtë për nënshkrimin e JWT-ve.

    public JWTService() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256"); // Krijon një gjenerator çelësi për algoritmin HMAC-SHA256.
        SecretKey sk = keyGen.generateKey(); // Gjeneron një çelës të fshehtë.
        secretKey = Base64.getEncoder().encodeToString(sk.getEncoded()); // Kodifikon çelësin si një string Base64.
    }

    public String generateToken(String username) {
        Map<String,Object> claims = new HashMap<>(); // Krijon një hartë për pretendimet (në këtë rast bosh).

        // Krijon dhe nënshkruan një JWT me informacionin e dhënë.
        return Jwts.builder()
                .setClaims(claims) // Shton pretendimet në token.
                .setSubject(username) // Vendos subjektin e token-it si emrin e përdoruesit.
                .setIssuedAt(new Date(System.currentTimeMillis())) // Vendos datën e lëshimit të token-it.
                .setExpiration(new Date(System.currentTimeMillis()+60*60*30)) // Vendos datën e skadimit të token-it (30 orë).
                .signWith(getKey()) // Nënshkruan token-in me çelësin e fshehtë.
                .compact(); // Kthen token-in si një string.
    }

    private SecretKey getKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey); // Dekodifikon çelësin e fshehtë nga stringu Base64 në bytes.
        return Keys.hmacShaKeyFor(keyBytes); // Kthen një objekt Key për nënshkrimin.
    }

    public Claims extractUserName(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build().parseSignedClaims(token)
                .getPayload();
    }

    public boolean valideteToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token).getSubject();
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private boolean isTokenExpired(String token) {
        return extractUserName(token).getExpiration().before(new Date());
    }
}
