package com.example.springsecurityjwttemplate.filter;

import com.example.springsecurityjwttemplate.security.JwtUtil; // Utilitaire pour la génération et la validation des JWT
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager; // Gestionnaire d'authentification de Spring Security
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Token d'authentification par nom d'utilisateur et mot de passe
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder; // Contexte de sécurité pour stocker les informations d'authentification
import org.springframework.security.core.userdetails.UserDetails; // Détails de l'utilisateur chargés par le service UserDetailsService
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter; // Filtre de base pour l'authentification HTTP

import java.io.IOException; // Pour gérer les exceptions liées à la lecture des données de la requête
import java.util.Collection;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private JwtUtil jwtUtil; // Utilitaire JWT injecté par Spring

    private UserDetailsService userDetailService; // Service pour charger les détails de l'utilisateur

    /**
     * Constructeur du filtre.
     *
     * @param authManager Gestionnaire d'authentification.
     * @param jwtUtil     Utilitaire JWT.
     */
    public JwtAuthorizationFilter(AuthenticationManager authManager, JwtUtil jwtUtil,UserDetailsService userDetailService) {
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
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        String username = jwtUtil.extractUsername(token);
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        if (jwtUtil.validateToken(token, userDetails)) {
            Collection<? extends GrantedAuthority> authorities = jwtUtil.extractRoles(token);
            System.out.println(authorities);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, authorities);
/*
            Cela permet de renseigner le SecurityContextHolder pour que Spring Security sache que l'utilisateur est authentifié et connaisse ses permissions.
*/

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}