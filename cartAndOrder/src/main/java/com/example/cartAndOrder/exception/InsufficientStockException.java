package com.example.cartAndOrder.exception;

import lombok.Getter;

@Getter
public class InsufficientStockException extends RuntimeException {
    private String productId;

    public InsufficientStockException(String message, String productId) {
        super(message);
        this.productId = productId;
    }

}
