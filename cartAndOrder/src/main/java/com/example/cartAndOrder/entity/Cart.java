package com.example.cartAndOrder.entity;

import com.example.cartAndOrder.Dto.ProductDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
@Document(collection = "Cart")
public class Cart {
    @Id
    private String id;
    private String cartId;
    private List<Product> productList;
}
