package com.example.productDevelopment.service.client;

import com.example.productDevelopment.entity.client.ClientEntity;
import com.example.productDevelopment.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ResponseEntity<ClientEntity> createClient(String name) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setUsername(name);
        clientEntity.setIsAdmin(0);
        return ResponseEntity.ok(clientRepository.save(clientEntity));
    }
}
