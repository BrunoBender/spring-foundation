package br.com.bruno.config;

import br.com.bruno.entities.security.User;
import br.com.bruno.repositories.PermissionRepository;
import br.com.bruno.repositories.security.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@Configuration
public class AdminUserConfig implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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
    public void run(String... args) {
        var permissionAdmin = permissionRepository.findByDescription("ADMIN");

        var userAdmin = userRepository.findByUserName("admin");

        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("admin já existe");
                },
                () -> {
                    var user = new User();
                    user.setUserName("admin");
                    user.setPassword(passwordEncoder.encode("admin"));
                    user.setPermitions(List.of(permissionAdmin));
                    userRepository.save(user);
                }
        );
    }
}
