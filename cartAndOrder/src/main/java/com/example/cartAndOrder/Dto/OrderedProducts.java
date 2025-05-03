package com.example.cartAndOrder.Dto;

import lombok.Data;

@Data
public class OrderedProducts {
        private String cartId;
        private String pId;
        private String sId;
        private String sName;
        private int quantity;
        private double price;
}
