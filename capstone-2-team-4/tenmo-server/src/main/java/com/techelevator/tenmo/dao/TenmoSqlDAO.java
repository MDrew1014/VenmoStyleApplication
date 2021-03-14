package com.techelevator.tenmo.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Connection;

import java.util.ArrayList;



import com.techelevator.tenmo.model.TenmoAccount;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferRequest;

@Component
public class TenmoSqlDAO implements TenmoDAO{
	private JdbcTemplate jdbcTemplate;
	private DataSource dataSource;
	
	public TenmoSqlDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

	@Override
	public TenmoAccount getBalance(String username) {
	SqlRowSet result =	jdbcTemplate.queryForRowSet("Select accounts.* FROM accounts " + 
				"JOIN users ON users.user_id = accounts.user_id " + 
				"WHERE username = ?", username); 
	if(result.next()) {
		return new TenmoAccount(result.getInt("account_id"), result.getInt("user_id"), result.getBigDecimal("balance"));
	}		
		
		return null;
		
	}

	

	@Override
	public void transfer(TransferRequest request) throws Exception {
		//this.dataSource.getConnection().setAutoCommit(false);
		Connection conn = jdbcTemplate.getDataSource().getConnection();
		try{
			conn.setAutoCommit(false);
			
			BigDecimal balance = this.jdbcTemplate.queryForObject("SELECT balance FROM accounts WHERE user_id = ?",BigDecimal.class, request.getUserIdFrom());
			//TODO condition logic if request amount is greater than balance throw 400 error if request is greater than balance throw
			if(request.getAmount().compareTo(balance)>0) {
				throw new Exception("400 Error");
			}else {
				//TODO change user_id to account_id. use a subquery
				String query = "UPDATE accounts SET balance = balance - ? WHERE user_id = ?;"
						+ "UPDATE accounts SET balance = balance + ? WHERE user_id = ?;"
						+ "INSERT INTO transfers(transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2,2,?,?,?)"; 
				jdbcTemplate.update(query, request.getAmount(), request.getUserIdFrom(),request.getAmount(),
						request.getUserIdTo(), request.getUserIdFrom(), request.getUserIdTo(), request.getAmount());
				conn.commit();
			}			
			//TODO Decrease the balance on the from user_id
		
			//TODO increase the balance on the to user_id
			//TODO insert the transfer record
			
		}
		catch(Exception e) {
			if(!conn.getAutoCommit()) {
				conn.rollback();
			}
			throw e;
		}finally {
			conn.setAutoCommit(true);
		}
	}
	 


	@Override
	public List<Transfer> listTransfers(int userId) {
		List<Transfer> transfer = new ArrayList<>();
		String query = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfers t " + 
				"JOIN accounts a ON t.account_from = a.account_id " + 
				"JOIN accounts b ON t.account_to = b.account_id " + 
				"JOIN users u ON a.user_id = u.user_id " + 
				"JOIN users v ON b.user_id = v.user_id " + 
				"WHERE a.user_id = ? OR b.user_id = ?";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(query, userId, userId);
		while(results.next()) {
			Transfer resultTransfer = this.mapRowToTransfer(results);
			transfer.add(resultTransfer);
		}
		return transfer;
	}

	@Override
	public Transfer transferById(int transferId) {
		// TODO Auto-generated method stub
		SqlRowSet results = jdbcTemplate.queryForRowSet("SELECT * FROM transfer WHERE transfer_id = ?", transferId);
		if(results.next()) {
			return mapRowToTransfer(results);
		}return null;
	}
	
	private Transfer mapRowToTransfer(SqlRowSet rs) {
       Transfer transfer = new Transfer();
       transfer.setTransferId(rs.getInt("transfer_id"));
       transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
       transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setUsernameFrom(rs.getString("userFrom"));
        transfer.setUsernameTo(rs.getString("userTo"));
        return transfer;
        //TODO UPDATE WITH ALL FIELDS 
    }

	
	
}
