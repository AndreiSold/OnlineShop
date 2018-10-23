package ro.msg.learning.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.msg.learning.shop.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByNameEquals(String name);
}
