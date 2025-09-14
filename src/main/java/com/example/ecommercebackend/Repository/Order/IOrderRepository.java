package com.example.ecommercebackend.Repository.Order;

import com.example.ecommercebackend.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userid);

}
