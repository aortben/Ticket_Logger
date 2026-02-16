package org.iesalixar.daw2.OrtegaAlvaro.dwese_ticket_logger_api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Inyectamos el KeyPair que configuramos en KeyConfig
    @Autowired
    private KeyPair jwtKeyPair;

    // Constante para la expiración (1 hora en milisegundos)
    private static final long JWT_EXPIRATION = 3600000;

    /**
     * Genera un token JWT para un usuario con roles específicos.
     * Se firma con la CLAVE PRIVADA (RSA).
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username) // Configura el claim "sub"
                .claim("roles", roles) // Incluye los roles
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION)) // Expira en 1 hora
                // IMPORTANTE: Firmamos con la clave PRIVADA y el algoritmo RS256
                .signWith(jwtKeyPair.getPrivate(), Jwts.SIG.RS256)
                .compact();
    }

    /**
     * Valida un token JWT verificando la firma y la expiración.
     * Utiliza la CLAVE PÚBLICA para verificar.
     */
    public boolean validateToken(String token, String username) {
        // Parseamos el token para obtener los claims verificando la firma
        Claims claims = Jwts.parser()
                .verifyWith(jwtKeyPair.getPublic()) // Verificamos con clave PÚBLICA
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Verificamos que el usuario coincida y que no haya expirado
        return username.equals(claims.getSubject()) && !isTokenExpired(claims);
    }

    /**
     * Extrae el nombre de usuario del token.
     * Utiliza extractAllClaims para obtener el cuerpo primero.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Método genérico para extraer cualquier claim.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims usando la CLAVE PÚBLICA.
     * Este método es usado por extractUsername.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtKeyPair.getPublic()) // Verificamos con clave PÚBLICA
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Verifica si el token ha expirado usando el objeto Claims ya extraído.
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}