package com.example.cartAndOrder.service;

import com.example.cartAndOrder.ApiResponse;
import com.example.cartAndOrder.Dto.CartProductDto;
import com.example.cartAndOrder.Dto.OrderDto;
import com.example.cartAndOrder.Dto.ProductDto;
import com.example.cartAndOrder.ProductClientInterface;
import com.example.cartAndOrder.RandomIdGenerator;
import com.example.cartAndOrder.entity.Order;
import com.example.cartAndOrder.Dto.OrderedProducts;
import com.example.cartAndOrder.entity.Product;
import com.example.cartAndOrder.repository.OrderRepository;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ProductClientInterface productClientInterface;
//add date
    public String createOrder(List<OrderedProducts> productList, String cartId, Double total){
        Order order=new Order();
        order.setOrderList(productList);
        String orderStr=RandomIdGenerator.generateRandomId();
        order.setOrderId(orderStr.substring(0,8));
        order.setUserId(cartId);
        order.setTotalPrice(total);
        Date date=new Date();
        order.setDate(date);
        orderRepository.save(order);
        return order.getOrderId();
    }

//    public List<ProductDescDTO> getAll(int pageNumber, int pageSize){
//        Pageable pageable = PageRequest.of(pageNumber,pageSize);
//        Page<Product> page = productRepository.findAll(pageable);
//        List<Product> productList = page.getContent();
//        List<ProductDescDTO> productDTOList = new ArrayList<>();
//        for(Product product:productList){
//            ProductDescDTO productDTO = new ProductDescDTO();
//            BeanUtils.copyProperties(product,productDTO);
//            productDTOList.add(productDTO);
//        }
//        return productDTOList;
//    }

    public List<OrderDto> getAllOrders(String userId) {
        List<Order> orderList = orderRepository.findByUserId(userId);
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (Order order : orderList) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order, orderDto);
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    public List<CartProductDto> getOrderDetails(String orderId){
        Optional<Order> orderOptional= Optional.ofNullable(orderRepository.findByOrderId(orderId));
        List<CartProductDto> response =new ArrayList<>();
        if(orderOptional.isPresent()){
            Order order=orderOptional.get();
            List<OrderedProducts> li=order.getOrderList();
            for (OrderedProducts orderedProducts : li) {
                ResponseEntity<ApiResponse<ProductDto>> responseEntity=productClientInterface.findById(orderedProducts.getPId());
                ProductDto productDto=responseEntity.getBody().getData();
                CartProductDto cartProductDto=new CartProductDto();
                BeanUtils.copyProperties(productDto,cartProductDto);
                BeanUtils.copyProperties(orderedProducts,cartProductDto);
                response.add(cartProductDto);
            }
        }
        return response;
    }

}
