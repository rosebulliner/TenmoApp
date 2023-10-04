package com.techelevator.services;

import com.techelevator.model.CredentialsDto;
import com.techelevator.model.TokenDto;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class AuthenticationService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();

    public String login(String username, String password) {
        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setUsername(username);
        credentialsDto.setPassword(password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CredentialsDto> entity = new HttpEntity<>(credentialsDto, headers);
        String token = null;
        try {
           TokenDto response = restTemplate.exchange(API_BASE_URL + "login", HttpMethod.POST, entity, TokenDto.class).getBody();
           token = response.getToken();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return token;
    }
    public void register(String username, String password) {
        CredentialsDto credentialsDto = new CredentialsDto();
        credentialsDto.setUsername(username);
        credentialsDto.setPassword(password);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CredentialsDto> entity = new HttpEntity<>(credentialsDto, headers);
        String token = null;
        try {
            restTemplate.exchange(API_BASE_URL + "register", HttpMethod.POST, entity, Void.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }}


}
