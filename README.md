📌 Adapter cette template à un autre projet
Cette template de sécurité avec Spring Security & JWT est conçue pour être flexible et adaptable. Voici les modifications essentielles à effectuer pour l'intégrer dans un nouveau projet.

🛠️ 1. Modification des URL des Endpoints
Les routes définies dans SecurityConfig doivent être adaptées aux endpoints de votre nouveau projet.

📌 Exemple :
Si dans votre projet les routes ne sont pas sous /api/auth/** ou /api/products/**, vous devez les ajuster dans SecurityConfig.java :


http
    .authorizeRequests()
    .antMatchers("/api/auth/**").permitAll()
    .antMatchers("/api/customers/**").authenticated() // Exemple d'adaptation
    .anyRequest().authenticated();
À faire :

Modifiez les URL des endpoints pour correspondre à la structure de votre nouvelle API.
Assurez-vous que les bonnes permissions sont appliquées à chaque route.
🏗️ 2. Adapter les Entités Utilisateur et Rôle
Votre projet peut avoir un modèle utilisateur différent.

📌 Exemple :
Dans User.java, si votre nouvelle base de données stocke des emails au lieu de username, adaptez la classe :


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email; // Changement de "username" à "email"

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;
}
À faire :

Vérifiez que les entités User et Role correspondent à votre modèle de données.
Modifiez les repositories si nécessaire (ex: findByUsername → findByEmail).
🔐 3. Mise à Jour du JWT (Secret et Expiration)
Dans JwtUtil.java, la clé secrète et la durée d'expiration doivent être modifiées pour s’adapter à votre projet.

📌 Exemple (utilisation de variables d’environnement) :

java
Copier
Modifier
@Value("${jwt.secret}")
private String secret;

@Value("${jwt.expiration}")
private long expiration;
Puis, dans application.properties :


jwt.secret=nouvelle_clé_très_sécurisée
jwt.expiration=86400000  # 24 heures en millisecondes
À faire :

Remplacez la clé secrète par une nouvelle valeur sécurisée.
Déplacez les configurations dans application.properties pour plus de sécurité.
🔄 4. Vérifier les Filtres d’Authentification et d’Autorisation
Les classes JwtAuthenticationFilter et JwtAuthorizationFilter doivent correspondre aux besoins de votre projet.

Vérifications à faire :
✅ Assurez-vous que le format de la requête de connexion correspond à votre frontend.
✅ Vérifiez que les données extraites du JWT sont correctes (ex: ID utilisateur, rôles).
✅ Modifiez les erreurs retournées en JSON si nécessaire.

🔍 5. Adapter le Service Utilisateur
Dans UserService.java, la logique de chargement et d’inscription des utilisateurs peut être différente.

📌 Exemple : Si vous utilisez email au lieu de username :


public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getRoles());
}
À faire :

Vérifiez que l’inscription fonctionne bien avec votre modèle.
Ajoutez des validations supplémentaires si nécessaire (email unique, formats valides, etc.).
🎯 6. Modifier les Endpoints d’Authentification
Dans AuthController.java, adaptez les routes et la gestion des utilisateurs.

📌 Exemple :
Si vous ajoutez une vérification d’email avant inscription :


@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody Request request) {
    if (userRepository.existsByEmail(request.getEmail())) {
        return ResponseEntity.badRequest().body("Email déjà utilisé");
    }
    User user = new User();
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRoles(List.of(roleRepository.findByName("USER").orElseThrow()));

    userRepository.save(user);
    return ResponseEntity.ok("Inscription réussie !");
}
À faire :

Modifiez le format de la requête en fonction des besoins de votre frontend.
Ajoutez des validations supplémentaires si nécessaire.
✅ Résumé : Ce que vous devez modifier
✔️ SecurityConfig : Adapter les routes et les autorisations.
✔️ User et Role : Vérifier que les entités correspondent à votre modèle.
✔️ JwtUtil : Modifier la clé secrète et la durée du token.
✔️ Filtres JWT : Adapter le format des requêtes et réponses.
✔️ UserService : Vérifier l’inscription et le chargement des utilisateurs.
✔️ AuthController : Modifier la logique d’authentification et d’inscription.

💡 Une fois ces ajustements faits, votre template sera adaptée à votre nouveau projet ! 🚀
