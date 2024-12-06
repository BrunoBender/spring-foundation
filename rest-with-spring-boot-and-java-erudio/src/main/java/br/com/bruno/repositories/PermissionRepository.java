package br.com.bruno.repositories;

import br.com.bruno.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Permission findByDescription(String description);
}
