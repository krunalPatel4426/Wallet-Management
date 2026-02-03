package com.example.productDevelopment.service.Order;

import com.example.productDevelopment.config.GlobalException.CustomeException.UserNotFoundException;
import com.example.productDevelopment.dto.CommonResponse;
import com.example.productDevelopment.dto.order.OrderDetailResponse;
import com.example.productDevelopment.dto.order.OrderRequest;
import com.example.productDevelopment.dto.projection.OrderDetailProjection;
import com.example.productDevelopment.entity.order.OrderEntity;
import com.example.productDevelopment.enums.StatusEnum;
import com.example.productDevelopment.enums.TransactionTypeEnum;
import com.example.productDevelopment.repository.ClientRepository;
import com.example.productDevelopment.repository.OrderRepository;
import com.example.productDevelopment.repository.TransactionRepository;
import com.example.productDevelopment.service.External.FullfillmentService;
import com.example.productDevelopment.service.HelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private HelperService helperService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private FullfillmentService  fullfillmentService;

    @Transactional()
    public ResponseEntity<CommonResponse> createOrder(Long client_id, OrderRequest request) {
        Long currentVersion = clientRepository.getCurrentVersion(client_id);
        if (currentVersion == null) {
            throw new UserNotFoundException(client_id);
        }

        int updateRow = clientRepository.debitAmount(client_id, request.getAmount(), currentVersion);
        if (updateRow == 0) {
            helperService.logFailedOrder(request.getAmount(), StatusEnum.FAILED.toString(), client_id);
            BigDecimal amount = clientRepository.getBalance(client_id);
            if(amount.compareTo(request.getAmount()) < 0) {
                helperService.logFailedTransactions(
                        client_id,
                        "Order cancel due to insufficient balance.",
                        TransactionTypeEnum.DEBIT.toString()
                );

                throw new RuntimeException("insufficient Balance");
            }else{
                helperService.logFailedTransactions(
                        client_id,
                        "Order cancel due to Concurrent request.",
                        TransactionTypeEnum.DEBIT.toString()
                );
                throw new RuntimeException("Order cancel due to Concurrent request. Please try again.");
            }
        }
        Long order_id = orderRepository.saveOrder(request.getAmount(), StatusEnum.PENDING.toString(), client_id);
        Integer fulfillmentId;
        try{
            fulfillmentId = fullfillmentService.getFullFillmentId(client_id, order_id);
        }catch (RuntimeException e){
            throw new RuntimeException("Error occurred while filling fullfillment.");
        }

        int changeInRow = orderRepository.updateOrder(order_id, StatusEnum.COMPLETED.toString(), fulfillmentId.longValue());
        if(changeInRow == 0) {
            helperService.logFailedOrder(request.getAmount(), StatusEnum.FAILED.toString(), client_id);
        }
        transactionRepository.addTransaction(
                client_id,
                "Order #" + order_id + " placed. Fulfillment: " + fulfillmentId,
                TransactionTypeEnum.DEBIT.toString()
        );
        CommonResponse response = new CommonResponse();
        response.setCode(HttpStatus.OK.value());
        response.setMessage("Order #" + order_id + " Success");
        response.setSuccess(Boolean.TRUE);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<OrderDetailResponse> getOrder(Long orderId, Long clientId) {
        OrderDetailProjection detail = orderRepository.getOrderDetail(orderId, clientId);
        if(detail == null) {
            throw new UserNotFoundException(clientId);
        }
        OrderDetailResponse response =  new OrderDetailResponse();
        response.setAmount(detail.getAmount());
        response.setStatus(detail.getStatus());
        response.setFulfillmentId(detail.getFulFillmentId());
        response.setSuccess(true);
        return ResponseEntity.ok(response);

    }
}
