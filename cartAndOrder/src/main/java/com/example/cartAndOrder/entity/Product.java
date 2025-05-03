package com.example.cartAndOrder.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("pId")
    private String pId;
    @JsonProperty("sId")
    private String sId;
    private int quantity;
}
