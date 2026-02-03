package com.example.productDevelopment.controller.admin;

import ch.qos.logback.core.net.server.Client;
import com.example.productDevelopment.dto.admin.AdminTransactionRequest;
import com.example.productDevelopment.entity.client.ClientEntity;
import com.example.productDevelopment.repository.ClientRepository;
import com.example.productDevelopment.service.admin.AdminService;
import com.example.productDevelopment.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @PostMapping("")
    public ResponseEntity<ClientEntity> addAdmin(@RequestParam String name){
        return adminService.addAdmin(name);
    }

    @PostMapping("/wallet/credit")
    public ResponseEntity creditWallet(@RequestBody AdminTransactionRequest request){
        return adminService.creditWallet(request);
    }

    @PostMapping("/wallet/debit")
    public ResponseEntity debitWallet(@RequestBody AdminTransactionRequest request){
        return adminService.debitWallet(request);
    }

}
