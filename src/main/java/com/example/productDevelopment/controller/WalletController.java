package com.example.productDevelopment.controller;

import com.example.productDevelopment.dto.CommonResponse;
import com.example.productDevelopment.repository.ClientRepository;
import com.example.productDevelopment.service.admin.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdminService adminService;
    @GetMapping()
    public ResponseEntity<CommonResponse> getWallet(@RequestHeader("client_id") Long clientId) {
        return adminService.getWallet(clientId);
    }
}
