package com.example.cartAndOrder.repository;

import com.example.cartAndOrder.entity.Cart;
import com.example.cartAndOrder.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//import org.springframework.data.mongodb.repository.Update;
@Repository
public interface CartRepository extends MongoRepository<Cart,String> {
//    @Query("{'cartId': ?0 ")
    Cart findByCartId(String cartId);
//    Product findProductListByCartIdAndProductListProductId(String cartId, String productId);


}
