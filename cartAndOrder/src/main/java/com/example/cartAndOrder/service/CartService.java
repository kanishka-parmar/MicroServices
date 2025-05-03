package com.example.cartAndOrder.service;

import com.example.cartAndOrder.Dto.CartProductDto;
import com.example.cartAndOrder.Dto.ProductDto;
import com.example.cartAndOrder.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
    public void createCart(String userId);
    public boolean addItem(Product product, String cartId);
    public boolean updateQuantity(String cartId,String pId,String sId,int newQuantity);
    public boolean deleteItem(String cartId,String pId,String sId);
    public List<CartProductDto> getItems(String cartId);
    public double getTotalPrice(String cartId);
    public int getTotalItemCount(String cartId);
    public boolean checkout(String cartId,String userName);


    }
