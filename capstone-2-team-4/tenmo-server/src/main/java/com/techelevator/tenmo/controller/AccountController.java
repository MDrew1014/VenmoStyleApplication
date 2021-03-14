package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TenmoDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferRequest;
import com.techelevator.tenmo.model.User;
@RequestMapping("/tenmo/account")
@RestController
public class AccountController {
	private TenmoDAO tDAO;
	private UserDAO uDAO;
	
	
	public AccountController(TenmoDAO tDAO, UserDAO uDAO) {
		this.tDAO = tDAO;
		this.uDAO = uDAO;
	}


	@PreAuthorize("isAuthenticated()")
	@RequestMapping(path = "/balance", method = RequestMethod.GET )
	public TenmoAccount getBalance(Principal principal) {
		String username = principal.getName();
		return tDAO.getBalance(username);

	}
	
	@PreAuthorize("isAuthenticated()")
	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "/transfer", method = RequestMethod.POST)
	public void transfer(@RequestBody TransferRequest request, Principal principal) throws Exception {
		String username = principal.getName();
		int userId = this.uDAO.findIdByUsername(username);
		if(request.getUserIdFrom()!= userId) {	
			//TODO throw exception HERE!!!!!!!!!!!! invalid user
		}tDAO.transfer(request);
	

	}
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(path = "/transfer", method = RequestMethod.GET)
	public List<Transfer> listTransfers(Principal principal){
		String username = principal.getName();
		int userId = this.uDAO.findIdByUsername(username);
		return tDAO.listTransfers(userId);
	}
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
	public Transfer transferById(@PathVariable int transferId){
		return tDAO.transferById(transferId);
				
	}
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public List<User> getAllUsers(){
	return uDAO.findAll();
	}

}
