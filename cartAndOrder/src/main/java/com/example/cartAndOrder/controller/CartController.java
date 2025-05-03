package com.example.cartAndOrder.controller;

import com.example.cartAndOrder.ApiResponse;
import com.example.cartAndOrder.Dto.CartProductDto;
import com.example.cartAndOrder.Dto.ProductDto;
import com.example.cartAndOrder.entity.Product;
import com.example.cartAndOrder.exception.InsufficientStockException;
import com.example.cartAndOrder.repository.CartRepository;
import com.example.cartAndOrder.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/cart")
public class CartController {
//httprequest

    @Autowired
    private CartService cartService;
    @CrossOrigin
    @PostMapping("/createCart")
    public ResponseEntity<ApiResponse<String>> createCart( @RequestParam String userId){

        cartService.createCart(userId);
        return new ResponseEntity<>(new ApiResponse<>("Cart Created"), HttpStatus.OK);
    }

   // @RequestHeader(name = "", required = true) String cartId
//    @CrossOrigin
//    @PostMapping("/addItem")
//    public ResponseEntity<ApiResponse<String>> addItem(@RequestBody Product product, @RequestParam String cartId){
//        System.out.println(product+"controller:::");
//        if(cartService.addItem(product,cartId)){
////            return new ResponseEntity<>(new ApiResponse<>(true,"Item Added"), HttpStatus.OK);
//            return new ResponseEntity<>(new ApiResponse<>("Item Added"),HttpStatus.OK);
//        }
//        return new ResponseEntity<>(new ApiResponse<>(false,"Item not Found"),HttpStatus.BAD_REQUEST);
//    }
   @CrossOrigin
   @PostMapping("/addItem")
   public ResponseEntity<ApiResponse<String>> addItem(@RequestBody Product product, HttpServletRequest request){
        String cartId=request.getHeader("useremail");
       if(cartService.addItem(product,cartId)){
//            return new ResponseEntity<>(new ApiResponse<>(true,"Item Added"), HttpStatus.OK);
           return new ResponseEntity<>(new ApiResponse<>("Item Added"),HttpStatus.OK);
       }
       return new ResponseEntity<>(new ApiResponse<>(false,"Item not Found"),HttpStatus.BAD_REQUEST);
   }


    @CrossOrigin
    @PutMapping("/updateQuantity")
    public ResponseEntity<ApiResponse<String>> updateQuantity(HttpServletRequest request ,@RequestParam String pId,@RequestParam String sId,@RequestParam int newQuantity){
        String cartId=request.getHeader("useremail");
        if(cartService.updateQuantity(cartId,pId,sId,newQuantity)){
            return new ResponseEntity<>(new ApiResponse<>("Item Quantity Updated"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse<>(false,"Could not update quantity"),HttpStatus.BAD_REQUEST);
    }
    @CrossOrigin
    @DeleteMapping("/deleteItem")
    public ResponseEntity<ApiResponse<String>> deleteItem(HttpServletRequest request,@RequestParam String pId,@RequestParam String sId){
        String cartId=request.getHeader("useremail");
        if(cartService.deleteItem(cartId,pId,sId)){
            return new ResponseEntity<>(new ApiResponse<>("Item Deleted"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse<>(false,"Item does not Exist"),HttpStatus.BAD_REQUEST);
    }
    @CrossOrigin
    @GetMapping("/getItems")
    public ResponseEntity<ApiResponse<List<CartProductDto>>> getItems(HttpServletRequest request){
        String cartId=request.getHeader("useremail");
        List<CartProductDto> cartProductDtoList=cartService.getItems(cartId);
        if(cartProductDtoList==null){
            return new ResponseEntity<>(new ApiResponse<>(false,"No Items Found"),HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(cartProductDtoList),HttpStatus.OK);
    }
//    @CrossOrigin
//    @GetMapping("/getItems")
//    public ResponseEntity<ApiResponse<List<CartProductDto>>> getCartItems(@PathVariable String cartId) {
//        try {
//            List<CartProductDto> cartProductDtoList = cartService.getItems(cartId);
//            ApiResponse<List<CartProductDto>> response = new ApiResponse<>(true, "Cart items retrieved successfully", cartProductDtoList);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (InsufficientStockException ex) {
//
//            ApiResponse<List<CartProductDto>> response = new ApiResponse<>(false, ex.getMessage(), null);
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//    }

    @CrossOrigin
    @GetMapping("/getTotalPrice")
    public ResponseEntity<ApiResponse<Double>> getTotalPrice(HttpServletRequest request){
        String cartId=request.getHeader("useremail");
        double totalPrice=cartService.getTotalPrice(cartId);
        return new ResponseEntity<>(new ApiResponse<>(totalPrice),HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/getItemCount")
    public ResponseEntity<ApiResponse<Integer>> getTotalItemCount(HttpServletRequest request){
        String cartId=request.getHeader("useremail");
        int totalCount=cartService.getTotalItemCount(cartId);
        return new ResponseEntity<>(new ApiResponse<>(totalCount),HttpStatus.OK);
    }


    @CrossOrigin
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<String>> checkoutCart(HttpServletRequest request){
        String cartId=request.getHeader("useremail");
        String userName=request.getHeader("username");
        System.out.println("in checkout controller");
        if(cartService.checkout(cartId,userName)){
            return new ResponseEntity<>(new ApiResponse<>("Successfully Checked Out"),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse<>(false,"Could Not CheckOut"),HttpStatus.OK);
    }
//    @CrossOrigin
//    @GetMapping("/getTotal")
}
