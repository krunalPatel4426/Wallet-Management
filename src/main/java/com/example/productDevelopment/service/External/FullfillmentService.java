package com.example.productDevelopment.service.External;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class FullfillmentService {
    private RestTemplate restTemplate = new RestTemplate();

    public Integer getFullFillmentId(Long clientId, Long orderId) throws RuntimeException {

        String url = "https://jsonplaceholder.typicode.com/posts";
        Map<String, Object> request = new HashMap<>();
        request.put("clientId", clientId);
        request.put("title", orderId);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        return (Integer) response.getBody().get("id");

    }
}
