package com.example.springsecurityjwttemplate.repositories;

import com.example.springsecurityjwttemplate.model.Role;
import org.springframework.data.jpa.repository.JpaRepository; // Interface de base pour les repositories JPA
import java.util.Optional; // Pour gérer les résultats optionnels (éviter les nulls)

/**
 * Cette interface est un repository Spring Data JPA pour l'entité Role.
 * Elle permet d'effectuer des opérations de base de données sur la table des rôles.
 * Elle étend JpaRepository, ce qui fournit des méthodes CRUD par défaut.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Cette méthode recherche un rôle par son nom.
     * Elle sera utilisée pour trouver un rôle spécifique dans la base de données.
     *
     * @param name Le nom du rôle à rechercher (de type RoleName, une énumération).
     * @return Optional<Role> Un objet Optionnel contenant le rôle trouvé, ou vide si aucun rôle n'est trouvé.
     */
    Optional<Role> findByName(String name);
}