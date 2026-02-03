package com.example.productDevelopment.service;

import com.example.productDevelopment.repository.OrderRepository;
import com.example.productDevelopment.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class HelperService {

    @Autowired
    private TransactionRepository  transactionRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailedTransactions(Long clientId, String message, String type){
        transactionRepository.addTransaction(
                clientId,message,type
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailedOrder(BigDecimal amount, String string, Long clientId) {
        orderRepository.saveOrder(amount, string, clientId);
    }
}
