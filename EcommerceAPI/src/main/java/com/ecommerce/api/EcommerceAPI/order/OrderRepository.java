package com.ecommerce.api.EcommerceAPI.order;



import com.ecommerce.api.EcommerceAPI.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    Optional<Order> findByIdAndUser(UUID id, User user);
    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId);
}
