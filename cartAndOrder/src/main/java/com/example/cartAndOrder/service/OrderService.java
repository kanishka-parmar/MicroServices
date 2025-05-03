package com.example.cartAndOrder.service;

import com.example.cartAndOrder.Dto.CartProductDto;
import com.example.cartAndOrder.Dto.OrderDto;
import com.example.cartAndOrder.Dto.OrderedProducts;

import java.util.List;

public interface OrderService {
    public String createOrder(List<OrderedProducts> productList, String cartId, Double total);
    public List<OrderDto> getAllOrders(String uId);
    public List<CartProductDto> getOrderDetails(String orderId);


    }
