package com.example.demo.dto;

import com.example.demo.models.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {
    private int statusCode;
    private String errorMessage;
    private String message;
    private String token;
    private String refreshToken;
    private String issuedAt;
    private String expirationTime;
    private String username;
    private String email;
    private String phoneNum;
    private String password;
    private List<Product> products;

}
