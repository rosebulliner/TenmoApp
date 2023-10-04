package com.techelevator.services;

import com.techelevator.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.*;

public class AccountService {

   private RestTemplate restTemplate = new RestTemplate();
   private final String BASE_URL = "http://localhost:8080/accounts";
   private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


   //get accounts for logged-in user...
   public Account[] getAccountsForLoggedInUser(){
        try {
            ResponseEntity<Account[]> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, makeAuthEntity(), Account[].class);
            return response.getBody();
        } catch (Exception e){

        }
        return null;
    }

    //create an account for logged-in user...
    public Account[] createNewAccountForLoggedInUser(){
        try {
            restTemplate.exchange(BASE_URL, HttpMethod.POST, makeAuthEntity(), Void.class);
            ResponseEntity<Account[]> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, makeAuthEntity(), Account[].class);
            return response.getBody();
        } catch (Exception e){

        }
        return null;
    }


    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


}
