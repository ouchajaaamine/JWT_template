package com.example.springsecurityjwttemplate.security;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import io.jsonwebtoken.Jwts; // Bibliothèque pour la génération et la validation des JWT
import io.jsonwebtoken.security.Keys; // Pour générer des clés sécurisées
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; // Détails de l'utilisateur chargés par le service UserDetailsService
import org.springframework.stereotype.Component; // Annotation pour marquer cette classe comme un composant Spring

import javax.crypto.SecretKey; // Pour représenter une clé secrète cryptographique
import java.util.Collection;
import java.util.Date; // Pour gérer les dates (émission et expiration du token)
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cette classe est un utilitaire pour la gestion des JWT (JSON Web Tokens).
 * Elle permet de générer, valider et extraire des informations des tokens JWT.
 */
@Component // Marque cette classe comme un composant Spring pour l'injection de dépendances
public class JwtUtil {

    private final SecretKey secretKey = Keys.hmacShaKeyFor("mySecretKeymySecretKeymySecretKey".getBytes()); // Clé secrète pour signer les tokens JWT
    private final long expiration = 86400000; // Durée de validité du token (1 jour en millisecondes)

    /**
     * Génère un token JWT pour un utilisateur.
     *
     * @param userDetails Les détails de l'utilisateur (nom d'utilisateur, rôles, etc.).
     * @return String Le token JWT généré.
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .subject(userDetails.getUsername()) // Définit le sujet du token (nom d'utilisateur)
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .issuedAt(new Date()) // Définit la date d'émission du token
                .expiration(new Date(System.currentTimeMillis() + expiration)) // Définit la date d'expiration du token
                .signWith(secretKey) // Signe le token avec la clé secrète
                .compact(); // Convertit le token en une chaîne compacte
    }
    public Collection<? extends GrantedAuthority> extractRoles(String token) {
        var claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
    /**
     * Extrait le nom d'utilisateur à partir d'un token JWT.
     *
     * @param token Le token JWT.
     * @return String Le nom d'utilisateur extrait du token.
     */
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // Configure la clé secrète pour valider le token
                .build()
                .parseSignedClaims(token) // Parse le token et valide sa signature
                .getPayload() // Récupère le corps du token (claims)
                .getSubject(); // Extrait le sujet (nom d'utilisateur)
    }

    /**
     * Valide un token JWT en vérifiant qu'il correspond à l'utilisateur et qu'il n'est pas expiré.
     *
     * @param token       Le token JWT à valider.
     * @param userDetails Les détails de l'utilisateur.
     * @return boolean True si le token est valide, sinon False.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token); // Extrait le nom d'utilisateur du token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Vérifie que le nom d'utilisateur correspond et que le token n'est pas expiré
    }

    /**
     * Vérifie si un token JWT est expiré.
     *
     * @param token Le token JWT à vérifier.
     * @return boolean True si le token est expiré, sinon False.
     */
    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // Configure la clé secrète pour valider le token
                .build()
                .parseSignedClaims(token) // Parse le token et valide sa signature
                .getPayload() // Récupère le corps du token (claims)
                .getExpiration() // Extrait la date d'expiration du token
                .before(new Date()); // Vérifie si la date d'expiration est antérieure à la date actuelle
    }
}