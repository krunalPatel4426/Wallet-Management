package com.example.productDevelopment.dto.exception;

import lombok.Data;

@Data
public class ExceptionDto {
    private String message;
    private int code;
    private boolean success;
}
