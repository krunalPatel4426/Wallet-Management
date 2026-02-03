package com.example.productDevelopment.config.GlobalException.CustomeException;

import java.math.BigDecimal;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }
}
