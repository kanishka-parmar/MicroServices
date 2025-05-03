package com.example.cartAndOrder.Dto;

import lombok.Data;

@Data
public class CartProductDto {
    private String cartId;
    private String pId;
    private String pName;
    private String sName;
    private String sId;
    private int quantity;
    private String image;
    private double price;
    private int stock;
}
