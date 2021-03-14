package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TenmoAccount {
	private int accountId;
	private int userId;
	private BigDecimal balance;

	

	public TenmoAccount(int accountId, int userId, BigDecimal balance) {
		this.accountId = accountId;
		this.userId = userId;
		this.balance = balance;
	}

	public int getAccountId() {
		return accountId;
	}

	public int getUserId() {
		return userId;
	}

	public BigDecimal getBalance() {
		return balance;
	}
	
	
	
	
	
	
	
	
	
}
