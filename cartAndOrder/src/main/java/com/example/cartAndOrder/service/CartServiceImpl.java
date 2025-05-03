package com.example.cartAndOrder.service;

import com.example.cartAndOrder.ApiResponse;
import com.example.cartAndOrder.Dto.CartProductDto;
import com.example.cartAndOrder.Dto.ProductDto;
//import com.example.cartAndOrder.EmailCheckoutInterface;
import com.example.cartAndOrder.ProductClientInterface;
import com.example.cartAndOrder.entity.Cart;
import com.example.cartAndOrder.Dto.OrderedProducts;
import com.example.cartAndOrder.entity.Product;
import com.example.cartAndOrder.entity.Seller;
import com.example.cartAndOrder.exception.InsufficientStockException;
import com.example.cartAndOrder.repository.CartRepository;
import com.example.cartAndOrder.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements  CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClientInterface productClientInterface;

    @Autowired
    private EmailServiceImpl emailService;

//
//    @Autowired
//    EmailCheckoutInterface emailCheckoutInterface;

    @Override
    public void createCart(String userId){
        Cart cart=new Cart();
        cart.setCartId(userId);
        List<Product> li =new ArrayList<>();
        cart.setProductList(li);
        cartRepository.save(cart);
    }

    @Override
    public boolean addItem(Product product, String cartId){
        try {
            Optional<Cart> optionalCart = Optional.ofNullable(cartRepository.findByCartId(cartId));
            if (!optionalCart.isPresent()) {
                createCart(cartId);
                System.out.println("new Cart created");
                optionalCart = Optional.ofNullable(cartRepository.findByCartId(cartId));
            }
            Cart cart = optionalCart.get();
            List<Product> li = new ArrayList<>();
            if (cart.getProductList() != null) {
                li = cart.getProductList();
//                if (li.contains(product)) {
                for (Product product1 : li) {
                    if (product1.getPId().equals(product.getPId()) && product1.getSId().equals(product.getSId())) {
                        product1.setQuantity(product1.getQuantity() + 1);
                        cart.setProductList(li);
                        cartRepository.save(cart);
                        return true;
                    }
                }

//                }
            }
            li.add(product);
            cart.setProductList(li);
            cartRepository.save(cart);
            System.out.println("added successfully");
            return true;
        }catch (NullPointerException e) {
            System.err.println("NullPointerException caught: " + e.getMessage());
            return false;
        } catch (RuntimeException e) {
            System.err.println("RuntimeException caught: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Unexpected exception caught: " + e.getMessage());
            return false;
        }
    }


//    @Override
//    public boolean addItem(Product product, String cartId){
//        Optional<Cart> optionalCart= Optional.ofNullable(cartRepository.findByCartId(cartId));
//        if(optionalCart.isPresent()) {
//            Cart cart = optionalCart.get();
//            List<Product> li = new ArrayList<>();
//            if (cart.getProductList() != null) {
//                li = cart.getProductList();
//            }
////            product.setQuantity(product.getQuantity()+1);
//            li.add(product);
//            cart.setProductList(li);
//            cartRepository.save(cart);
//            System.out.println("added successfully");
//            return true;
//        }
//        return false;
//    }

//    public List<CartProductDto> getItems(String cartId){
//        System.out.println("in get items");
//        Optional<Cart> optionalCart= Optional.ofNullable(cartRepository.findByCartId(cartId));
//        List<CartProductDto> cartProductDtoList=new ArrayList<>();
//        if(optionalCart.isPresent()){
//            Cart cart=optionalCart.get();
//            if(cart.getProductList()!=null) {
//                List<Product> prodList = cart.getProductList();
//                for (Product product : prodList) {
//                    System.out.println("right before deeksha");
//                    ResponseEntity<ApiResponse<ProductDto>> responseEntity = productClientInterface.findById(product.getPId());
//                    ProductDto productDto = responseEntity.getBody().getData();
//                    System.out.println("after" + productDto);
//                    CartProductDto cartProductDto = new CartProductDto();
//                    BeanUtils.copyProperties(product, cartProductDto);
//                    BeanUtils.copyProperties(productDto, cartProductDto);
//                    cartProductDto.setCartId(cartId);
//                    for (Seller seller : productDto.getSeller()) {
//                        if (seller.getSId().equals(product.getSId())) {
//                            BeanUtils.copyProperties(seller, cartProductDto);
//                        }
//                    }
//                    cartProductDtoList.add(cartProductDto);
//                }
//                //code when cart.prod list is null
//            }
//        }
//        return cartProductDtoList;
//    }


    public List<CartProductDto> getItems(String cartId) {
        Optional<Cart> optionalCart = Optional.ofNullable(cartRepository.findByCartId(cartId));
        List<CartProductDto> cartProductDtoList = new ArrayList<>();
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            List<Product> prodList = cart.getProductList();
            System.out.println(prodList);

            if(prodList!=null || !prodList.isEmpty()) {
                Iterator<Product> iterator = prodList.iterator();
                while (iterator.hasNext()) {
                    Product product = iterator.next();
                    ResponseEntity<ApiResponse<ProductDto>> responseEntity = productClientInterface.findById(product.getPId());
//                    System.out.println(responseEntity);
                    ProductDto productDto = responseEntity.getBody().getData();
//                    System.out.println(productDto);

                    CartProductDto cartProductDto = new CartProductDto();
                    BeanUtils.copyProperties(product, cartProductDto);
                    BeanUtils.copyProperties(productDto, cartProductDto);
                    cartProductDto.setCartId(cartId);
//                    System.out.println(productDto);
                    try {
                        for (Seller seller : productDto.getSeller()) {
//                            System.out.println(seller.getSId());
//                            System.out.println(product.getSId());
                            if (seller.getSId().equals(product.getSId())) {
                                BeanUtils.copyProperties(seller, cartProductDto);
                                //added line below
                                cartProductDto.setPrice(cartProductDto.getPrice()*cartProductDto.getQuantity());
                                break;
                            }
                        }
                    }catch (NullPointerException e){
                        System.out.println("seller id not available"+e);
                    }
                    if (cartProductDto.getQuantity() > cartProductDto.getStock()) {
                        iterator.remove();
                        cart.setProductList(prodList);
                        cartRepository.save(cart);
//                    throw new InsufficientStockException("Product stock is insufficient", cartProductDto.getPId());
                    } else {
                        cartProductDtoList.add(cartProductDto);
                    }
                }
            }
        }
        System.out.println("in get items"+cartProductDtoList);
        return cartProductDtoList;
    }


    @Override
    public boolean updateQuantity(String cartId,String pId,String sId,int newQuantity){
        System.out.println("in update quantity");
        Optional<Cart> optionalCart= Optional.ofNullable(cartRepository.findByCartId(cartId));
        if(optionalCart.isPresent()){
            Cart cart=optionalCart.get();
            for (Product product : cart.getProductList()) {
                if(product.getPId().equals(pId) && product.getSId().equals(sId)){
                    product.setQuantity(newQuantity);
                    cartRepository.save(cart);
                    return true;
                }
            }
        }
        return  false;
    }


//    public boolean updateQuantity(String cartId, String pId, String sId, int newQuantity) {
//        int updateResult = cartRepository.updateProductQuantity(cartId, pId, sId, newQuantity);
//        return updateResult > 0;
//    }
    @Override
    public boolean deleteItem(String cartId,String pId,String sId){
        Optional<Cart> optionalCart= Optional.ofNullable(cartRepository.findByCartId(cartId));
        if(optionalCart.isPresent()){
            Cart cart=optionalCart.get();
            for (Product product : cart.getProductList()) {
                if (product.getPId().equals(pId) && product.getSId().equals(sId)) {
                    List<Product> li=cart.getProductList();
                    li.remove(product);
                    cart.setProductList(li);
                    cartRepository.save(cart);
                    return true;
                }

            }

        }
        return false;
    }
    @Override
    public double getTotalPrice(String cartId){
        Optional<Cart> optionalCart= Optional.ofNullable(cartRepository.findByCartId(cartId));
        double total=0;
        if(optionalCart.isPresent()){
            Cart cart=optionalCart.get();
            if(!cart.getProductList().isEmpty()) {
                List<CartProductDto> li = getItems(cart.getCartId());
                for (CartProductDto cartproductDto : li) {
                    double price= cartproductDto.getPrice();
//                    total+=(price*cartproductDto.getQuantity());
                    total+=price;

                }
            }
        }
        return total;
    }


    @Override
    public int getTotalItemCount(String cartId){
        Optional<Cart> optionalCart= Optional.ofNullable(cartRepository.findByCartId(cartId));
        int totalCount=0;
        if(optionalCart.isPresent()){
            Cart cart=optionalCart.get();
            totalCount=cart.getProductList().size();
        }
        return totalCount;
    }
//    @Override
//    public boolean checkout(String cartId) {
//        Optional<Cart> optionalCart= Optional.ofNullable(cartRepository.findByCartId(cartId));
//        if (optionalCart.isPresent()) {
//            Cart cart = optionalCart.get();
//            List<CartProductDto> cartProductDtoList = getItems(cartId);
//            List<OrderedProducts> orderedProductsList = new ArrayList<>();
//            for (CartProductDto cartProductDto : cartProductDtoList) {
//                productClientInterface.updateStock(cartProductDto.getPId(), cartProductDto.getSId(), cartProductDto.getQuantity());
//                OrderedProducts orderedProducts = new OrderedProducts();
//                BeanUtils.copyProperties(cartProductDto, orderedProducts);
//                orderedProductsList.add(orderedProducts);
//            }
//            double totalPrice = getTotalPrice(cartId);
//            String orderStr=orderService.createOrder(orderedProductsList, cartId, totalPrice);
//            cart.setProductList(null);
//            cartRepository.save(cart);
//            return true;
//        }
//        return false;
//    }

    // need to check
    @Override
    public boolean checkout(String cartId,String userName) {
        Boolean orderCreated=false;
        Boolean cartEmptied=false;
        Boolean stockUpdated=false;
        List<Product> tempProductList=new ArrayList<>();
        String orderId="";
        try {
            Optional<Cart> optionalCart = Optional.ofNullable(cartRepository.findByCartId(cartId));
            System.out.println("intry");
            if (optionalCart.isPresent()) {
                System.out.println("one");
                Cart cart = optionalCart.get();
                System.out.println("two");
                List<CartProductDto> cartProductDtoList = getItems(cartId);
                List<OrderedProducts> orderedProductsList = new ArrayList<>();
                //create order
                for (CartProductDto cartProductDto : cartProductDtoList) {
                    OrderedProducts orderedProducts = new OrderedProducts();
                    BeanUtils.copyProperties(cartProductDto, orderedProducts);
                    orderedProductsList.add(orderedProducts);
                }
                System.out.println("after for");
                double totalPrice = getTotalPrice(cartId);
                orderId=orderService.createOrder(orderedProductsList, cartId, totalPrice);
                orderCreated=true;
                System.out.println("order created");
                //empty cart
                tempProductList=cart.getProductList();
                cart.setProductList(new ArrayList<>());
                cartRepository.save(cart);
                System.out.println("cart emptied");
                cartEmptied=true;
                //update stock
                for (CartProductDto cartProductDto : cartProductDtoList) {
                    productClientInterface.updateStock(cartProductDto.getPId(), cartProductDto.getSId(), cartProductDto.getQuantity());
                    stockUpdated=true;
                }
                if(orderCreated==true && cartEmptied==true){
                    Boolean reply=emailService.sendCheckoutEmail(cartId,userName);
                    System.out.println(reply);
                }
                return true;
            }
        }catch (RuntimeException ex){
            if(orderCreated){
                log.info("order will be deleted");
                System.out.println("order deleted");
                orderRepository.deleteByOrderId(orderId);
            }
            if(cartEmptied) {
                log.info("cart will be restored");
                System.out.println("cart restored");
                Optional<Cart> optionalCart = Optional.ofNullable(cartRepository.findByCartId(cartId));
                if (optionalCart.isPresent()) {
                    Cart cart = optionalCart.get();
                    cart.setProductList(tempProductList);
                    cartRepository.save(cart);
                }
            }
        }
        return false;
    }
}
