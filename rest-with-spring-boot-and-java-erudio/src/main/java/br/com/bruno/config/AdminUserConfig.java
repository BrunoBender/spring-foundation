package br.com.bruno.config;

import br.com.bruno.entities.Permission;
import br.com.bruno.entities.User;
import br.com.bruno.repositories.PermissionRepository;
import br.com.bruno.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private PermissionRepository permissionRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(
        PermissionRepository permissionRepository,
        UserRepository userRepository,
        BCryptPasswordEncoder passwordEncoder
    ) {
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var permissionAdmin = permissionRepository.findByDescription(Permission.Values.ADMIN.name());

        var userAdmin = userRepository.findByUsername("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin já existe");
                },
                () -> {
                    var user = new User();
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("admin"));
                    user.setPermissions(Set.of(permissionAdmin));
                    userRepository.save(user);
                }
        );
    }
}
