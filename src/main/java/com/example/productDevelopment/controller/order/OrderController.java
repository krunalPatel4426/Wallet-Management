package com.example.productDevelopment.controller.order;

import com.example.productDevelopment.dto.CommonResponse;
import com.example.productDevelopment.dto.order.OrderDetailResponse;
import com.example.productDevelopment.dto.order.OrderRequest;
import com.example.productDevelopment.entity.order.OrderEntity;
import com.example.productDevelopment.service.Order.OrderService;
import org.osgi.annotation.bundle.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public ResponseEntity<CommonResponse> createOrder(@RequestHeader Long client_id, @RequestBody OrderRequest request) {
        return orderService.createOrder(client_id, request);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<OrderDetailResponse> getOrderById(@PathVariable("order_id") Long orderId, @RequestHeader("client_id") Long clientId) {
        return orderService.getOrder(orderId, clientId);
    }
}
