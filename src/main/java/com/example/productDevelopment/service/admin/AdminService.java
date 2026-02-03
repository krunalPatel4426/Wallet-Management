package com.example.productDevelopment.service.admin;

import com.example.productDevelopment.config.GlobalException.CustomeException.UserNotFoundException;
import com.example.productDevelopment.dto.CommonResponse;
import com.example.productDevelopment.dto.admin.AdminTransactionRequest;
import com.example.productDevelopment.dto.exception.ExceptionDto;
import com.example.productDevelopment.entity.client.ClientEntity;
import com.example.productDevelopment.entity.transaction.TransactionEntity;
import com.example.productDevelopment.enums.TransactionTypeEnum;
import com.example.productDevelopment.repository.ClientRepository;
import com.example.productDevelopment.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public ResponseEntity creditWallet(AdminTransactionRequest request) {
        ClientEntity client = clientRepository.findById(request.getClientId()).orElseThrow(() -> new UserNotFoundException(request.getClientId()));
        int update = clientRepository.creditWallet(request.getClientId(), request.getAmount());
        if (update == 0) {
            transactionRepository.addTransaction(request.getClientId(), "Transaction of amount " + request.getAmount() + " was failed." , TransactionTypeEnum.CREDIT.toString());
            transactionRepository.addTransaction(request.getClientId(), request.getAmount() + " was credit by ADMIN.", TransactionTypeEnum.CREDIT.toString());
            throw new UserNotFoundException(request.getClientId());
        }
        int isAdded = transactionRepository.addTransaction(request.getClientId(), request.getAmount() + " was credit by ADMIN.", TransactionTypeEnum.CREDIT.toString());
        if(isAdded == 0){
            throw new RuntimeException("Something went wrong while adding transaction");
        }
        CommonResponse response = new CommonResponse();
        response.setMessage("Transaction of amount " + request.getAmount() + " was added successfully.");
        response.setCode(HttpStatus.OK.value());
        response.setSuccess(Boolean.TRUE);
        return ResponseEntity.ok().body(response);
    }


    public ResponseEntity debitWallet(AdminTransactionRequest request) {
        ClientEntity client = clientRepository.findById(request.getClientId()).orElseThrow(() -> new UserNotFoundException(request.getClientId()));
        Long currentVersion = clientRepository.getCurrentVersion(request.getClientId());
        if (currentVersion == null) {
            throw new UserNotFoundException(request.getClientId());
        }

        int rowUpdated = clientRepository.debitAmount(request.getClientId(), request.getAmount(), currentVersion);
        if (rowUpdated == 0) {
            BigDecimal currentBalance = clientRepository.getBalance(request.getClientId());

            if (currentBalance.compareTo(request.getAmount()) < 0) {
                transactionRepository.addTransaction(
                        request.getClientId(),
                            "Admin attempt to debit amount " + request.getAmount() + " was failed. Because of insufficient balance.",
                        TransactionTypeEnum.DEBIT.toString()
                );
                throw new RuntimeException("Insufficient wallet balance.");
            } else {
                transactionRepository.addTransaction(
                        request.getClientId(),
                        "Admin attempt to debit amount " + request.getAmount() + " was failed. Due to concurrent update.",
                        TransactionTypeEnum.DEBIT.toString()
                );
                throw new RuntimeException("Transaction failed due to concurrent update. Please try again.");
            }
        }

        transactionRepository.addTransaction(
                request.getClientId(),
                request.getAmount() + " was debited by ADMIN.",
                TransactionTypeEnum.DEBIT.toString()
        );
        CommonResponse response = new CommonResponse();
        response.setMessage("Transaction of amount " + request.getAmount() + " was debited successfully.");
        response.setCode(HttpStatus.OK.value());
        response.setSuccess(Boolean.TRUE);
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<CommonResponse> getWallet(Long clientId) {
        ClientEntity client =  clientRepository.findById(clientId).orElseThrow(() -> new UserNotFoundException(clientId));
        CommonResponse response = new CommonResponse();
        BigDecimal currentBalance = clientRepository.getBalance(clientId);
        if (currentBalance == null) {
            throw new UserNotFoundException(clientId);
        }
        response.setMessage("Current balance is " + currentBalance);
        response.setCode(HttpStatus.OK.value());
        response.setSuccess(Boolean.TRUE);
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ClientEntity> addAdmin(String name) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setUsername(name);
        clientEntity.setIsAdmin(1);
        return ResponseEntity.ok(clientRepository.save(clientEntity));
    }
}
