package com.techelevator.services;

import com.techelevator.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransferService {

    private RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://localhost:8080/transfers";
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    //get an array of users you can make a transfer to...
    public String[] getAvailableUsers(){
        try {
            ResponseEntity<String[]> response = restTemplate.exchange(BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(), String[].class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    //send a transfer to user...
    public Transfer sendTransfer(String user, double amount){
        HttpEntity<TransferAmountDTO> entity = makeTransferAmountDTOEntity(new TransferAmountDTO(amount));
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "/send/" + user, HttpMethod.POST, entity, Transfer.class);
            return response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }


    public Transfer requestTransfer(String user, double amount){
        HttpEntity<TransferAmountDTO> entity = makeTransferAmountDTOEntity(new TransferAmountDTO(amount));
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "/request/" + user, HttpMethod.POST, entity, Transfer.class);
            return response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    public Transfer[] listTransfersForLoggedInUser(){
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    public Transfer getTransferById(int id){
        HttpEntity<TransferIdDTO> entity = makeTransferIdDTOEntity(new TransferIdDTO(id));
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, entity, Transfer.class);
            return response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    public Transfer approveTransferById(int id){
        HttpEntity<TransferIdDTO> entity = makeTransferIdDTOEntity(new TransferIdDTO(id));
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "/approve", HttpMethod.POST, entity, Transfer.class);
            return response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    public Transfer denyTransferById(int id){
        HttpEntity<TransferIdDTO> entity = makeTransferIdDTOEntity(new TransferIdDTO(id));
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(BASE_URL + "/deny", HttpMethod.POST, entity, Transfer.class);
            return response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }

    public Transfer[] listPendingTransfersForLoggedInUser(){
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(BASE_URL + "/pending", HttpMethod.GET, makeAuthEntity(), Transfer[].class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }


    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<TransferAmountDTO> makeTransferAmountDTOEntity(TransferAmountDTO amount){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(amount, headers);
    }

    private HttpEntity<TransferIdDTO> makeTransferIdDTOEntity(TransferIdDTO id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(id, headers);
    }



    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


}
