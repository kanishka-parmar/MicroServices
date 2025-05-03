package com.example.cartAndOrder.entity;

import com.example.cartAndOrder.Dto.OrderedProducts;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
@Data
public class Order {
    @Id
    private String id;
    private String orderId;
    private String userId;
    private Date date;
    private List<OrderedProducts> orderList;
    private double totalPrice;
}
