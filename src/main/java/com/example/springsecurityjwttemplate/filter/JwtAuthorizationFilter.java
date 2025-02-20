package com.example.springsecurityjwttemplate.filter;

import com.example.springsecurityjwttemplate.security.JwtUtil; // Utilitaire pour la génération et la validation des JWT
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager; // Gestionnaire d'authentification de Spring Security
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Token d'authentification par nom d'utilisateur et mot de passe
import org.springframework.security.core.context.SecurityContextHolder; // Contexte de sécurité pour stocker les informations d'authentification
import org.springframework.security.core.userdetails.UserDetails; // Détails de l'utilisateur chargés par le service UserDetailsService
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter; // Filtre de base pour l'authentification HTTP

import java.io.IOException; // Pour gérer les exceptions liées à la lecture des données de la requête

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private JwtUtil jwtUtil; // Utilitaire JWT injecté par Spring

    private UserDetailsService userDetailService; // Service pour charger les détails de l'utilisateur

    /**
     * Constructeur du filtre.
     *
     * @param authManager Gestionnaire d'authentification.
     * @param jwtUtil     Utilitaire JWT.
     */
    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtUtil jwtUtil) {
        super(authManager);
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

    /**
     * Cette méthode est appelée pour chaque requête HTTP.
     * Elle vérifie la présence d'un token JWT dans l'en-tête "Authorization",
     * valide le token, et configure l'authentification dans le contexte de sécurité si le token est valide.
     *
     * @param request  La requête HTTP.
     * @param response La réponse HTTP.
     * @param chain    La chaîne de filtres pour continuer le traitement de la requête.
     * @throws IOException      Si une erreur se produit lors de la lecture des données de la requête.
     * @throws ServletException Si une erreur se produit lors du traitement de la requête.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                        FilterChain chain) throws IOException, ServletException {
        // Récupère l'en-tête "Authorization" de la requête
        String header = request.getHeader("Authorization");

        // Vérifie si l'en-tête est absent ou ne commence pas par "Bearer "
        if (header == null || !header.startsWith("Bearer ")) {
            // Passe la requête au filtre suivant dans la chaîne
            chain.doFilter(request, response);
            return;
        }

        // Extrait le token JWT de l'en-tête (en supprimant "Bearer ")
        String token = header.substring(7);

        // Extrait le nom d'utilisateur du token JWT
        String username = jwtUtil.extractUsername(token);

        // Charge les détails de l'utilisateur à partir du service UserDetailService
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        // Valide le token JWT avec les détails de l'utilisateur
        if (jwtUtil.validateToken(token, userDetails)) {
            // Crée un token d'authentification Spring Security
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            // Configure l'authentification dans le contexte de sécurité
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Passe la requête au filtre suivant dans la chaîne
        chain.doFilter(request, response);
    }
}