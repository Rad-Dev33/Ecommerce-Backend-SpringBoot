package com.example.ecommercebackend.Repository.Role;

import com.example.ecommercebackend.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByRolename(String roleUser);
}
