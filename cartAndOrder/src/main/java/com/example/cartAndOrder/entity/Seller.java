package com.example.cartAndOrder.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Seller {
    private String sName;
    @JsonProperty("sId")
    private String sId;
    private int stock;
    private double price;
}