package com.example.ecommercebackend.Repository.UserRepository;

import com.example.ecommercebackend.Model.Order;
import com.example.ecommercebackend.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {



    boolean existsByEmail(String email);

    boolean existsByEmailAndCartId(String email,Long cartId);

    Optional<User> findByEmail(String email);



}
