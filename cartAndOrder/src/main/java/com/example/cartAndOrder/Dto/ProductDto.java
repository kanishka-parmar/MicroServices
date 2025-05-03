package com.example.cartAndOrder.Dto;

import com.example.cartAndOrder.entity.Seller;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class ProductDto {
    private String pId;
    private String pName;
    private String description;
    private String image;
    private List<String> usp;
    private List<String> attribute;
    private List<Seller> seller;
}