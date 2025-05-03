package com.example.cartAndOrder.controller;

import com.example.cartAndOrder.ApiResponse;
import com.example.cartAndOrder.Dto.CartProductDto;
import com.example.cartAndOrder.Dto.OrderDto;
import com.example.cartAndOrder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @CrossOrigin
    @GetMapping("/getAllOrders")
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrders(HttpServletRequest request){
        String userId=request.getHeader("useremail");
        List<OrderDto> orderDtoList=orderService.getAllOrders(userId);
        if(orderDtoList.isEmpty()){
            return new ResponseEntity<>(new ApiResponse<>(false,"could not find any previous orders"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(orderDtoList),HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/getOrderDetails")
    public ResponseEntity<ApiResponse<List<CartProductDto>>> getOrderDetails(@RequestParam String orderId){
        List<CartProductDto> responseList=orderService.getOrderDetails(orderId);
        if(responseList.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(false, "could not find order"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(responseList),HttpStatus.OK);
    }

}
