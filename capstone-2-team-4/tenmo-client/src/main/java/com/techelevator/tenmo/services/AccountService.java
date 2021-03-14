package com.techelevator.tenmo.services;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


import com.techelevator.tenmo.models.TenmoAccount;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferRequest;
import com.techelevator.tenmo.models.User;



public class AccountService {
	  public String AUTH_TOKEN = "";
	  
	  private final String BASE_URL;
	  private final RestTemplate restTemplate = new RestTemplate();
	  public AccountService(String url) {
		  this.BASE_URL = url;
	  }
public TenmoAccount getTenmoAccount() throws AccountServiceException {
	TenmoAccount tenmoAccount = null;
	try{
		tenmoAccount = restTemplate.exchange(BASE_URL +"tenmo/account/balance",HttpMethod.GET, makeAuthEntity(), TenmoAccount.class)
				.getBody();
	} catch(RestClientResponseException ex) {
		throw new AccountServiceException(ex.getRawStatusCode()+ " : " + ex.getResponseBodyAsString());
	} 
		return tenmoAccount;
	}
	
public Transfer[] listTransfer() throws AccountServiceException {
	Transfer[] transfer = null;
	try {
		transfer = restTemplate.exchange(BASE_URL + "tenmo/account/transfer", HttpMethod.GET, makeAuthEntity(), Transfer[].class)
				.getBody();
	}catch(RestClientResponseException ex) {
		throw new AccountServiceException(ex.getRawStatusCode()+ " : " + ex.getResponseBodyAsString());
	}
	return transfer;
	
}
public User[] listUsers() throws AccountServiceException {
	User[] user = null;
	try {
		user = restTemplate.exchange(BASE_URL + "tenmo/account/users", HttpMethod.GET, makeAuthEntity(), User[].class)
				.getBody();
	}catch(RestClientResponseException ex) {
		throw new AccountServiceException(ex.getRawStatusCode()+ " : " + ex.getResponseBodyAsString());
	}
	return user;
}
public boolean startTransfer(TransferRequest transfer){
	String requestUrl = BASE_URL + "tenmo/account/transfer";
	HttpEntity<TransferRequest> entity = makeTransferRequestEntity(transfer);
	ResponseEntity<String> response =restTemplate.exchange(requestUrl, HttpMethod.POST,entity, String.class);
	return response.getStatusCodeValue()==201;
}
	
	
	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

	private HttpEntity<TransferRequest> makeTransferRequestEntity(TransferRequest transfer){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity<TransferRequest> entity = new HttpEntity<>(transfer, headers);
		return entity;
	}
	

}















