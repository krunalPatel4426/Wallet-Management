package com.example.productDevelopment.dto;

import lombok.Data;

@Data
public class CommonResponse {
    private String message;
    private int code;
    private boolean success;
}
