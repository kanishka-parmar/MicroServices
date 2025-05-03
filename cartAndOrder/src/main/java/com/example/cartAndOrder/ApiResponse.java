package com.example.cartAndOrder;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Data
@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String error;
    private T data;

    public ApiResponse(){
        this.success = true;
    }

    public ApiResponse(boolean success,String error) {
        this.success = success;
        this.data = null;
        this.error  = error;
    }
    public ApiResponse(T data){
        this.error = null;
        this.data = data;
        this.success = true;
    }

    public ApiResponse(boolean success, String error, T data) {
        this.success = success;
        this.error = error;
        this.data = data;
    }
}
