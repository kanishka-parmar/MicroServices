package com.example.cartAndOrder.Dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
@Data
public class OrderDto {
    private String id;
    private String orderId;
    private String userId;
    private Date date;
    private double totalPrice;
}