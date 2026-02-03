package com.example.productDevelopment.controller.Client;

import com.example.productDevelopment.entity.client.ClientEntity;
import com.example.productDevelopment.repository.ClientRepository;
import com.example.productDevelopment.service.client.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping()
    public ResponseEntity<ClientEntity> createClient(@RequestParam String name){
        return clientService.createClient(name);
    }
}
