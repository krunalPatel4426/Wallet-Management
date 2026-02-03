package com.example.productDevelopment.repository;

import com.example.productDevelopment.dto.projection.OrderDetailProjection;
import com.example.productDevelopment.entity.order.OrderEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(nativeQuery = true, value = """
    insert into orders(amount, status, updated_at, client_id) values (:amount, :status, CURRENT_TIMESTAMP, :clientId)
    RETURNING order_id;
""")
    public Long saveOrder(BigDecimal amount, String status, Long clientId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
    UPDATE orders set fulfillment_id = :fulfillmentId, status = :status where order_id = :id;
""")
    public int updateOrder(Long id,String status, Long fulfillmentId);

    @Query(nativeQuery = true, value = """
    SELECT 
        amount as amount,
        status as status,
        fulfillment_id as fulfillmentId
    from orders 
    where client_id = :clientId and order_id = :orderId
""")
    OrderDetailProjection getOrderDetail(Long orderId, Long clientId);
}
