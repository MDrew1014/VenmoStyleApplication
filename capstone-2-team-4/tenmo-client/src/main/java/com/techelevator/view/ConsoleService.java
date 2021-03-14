package com.techelevator.view;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.util.Scanner;


import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferRequest;
import com.techelevator.tenmo.models.User;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;
	

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}
	public void printTransfers(Transfer[] transfer) {
		if(transfer != null) {
	    System.out.println("--------------------------------------------");
	    System.out.println("Transfers");
	    System.out.println("ID             From/To          Amount");
	    System.out.println("--------------------------------------------");
	    for (Transfer transfers : transfer) {
	      System.out.println(transfers.getTransferId() + "\t:\t" + transfers.getUsernameFrom()+"\t\t"+ transfers.getAmount());
	    }
		}
	  }
	public void printTransferDetails(Transfer transfer) {
		if(transfer != null) {
			System.out.println("--------------------------------------------");
			System.out.println("Transfer Details");
			System.out.println("--------------------------------------------");
			System.out.println("ID: "+ transfer.getTransferId());
			System.out.println("From: "+ transfer.getUsernameFrom());
			System.out.println("To: "+ transfer.getUsernameTo());
			System.out.println("Type: "+ transfer.getTransferTypeId());
			System.out.println("Status: "+ transfer.getTransferStatusId());
			System.out.println("Amount: $"+transfer.getAmount());
		}
	}
	
	public User promptForTransfer(User[] user) {
		System.out.println("--------------------------------------------");
	    System.out.println("Users");
	    System.out.println("ID             Name");
	    System.out.println("--------------------------------------------");
	    return (User) this.getChoiceFromOptions(user);
	    
	}
			
	public TransferRequest sendMoney(Transfer transfer) {
		System.out.println("Enter ID of user you are sending to (0 to cancel):");
		String choice = null;
		choice = in.nextLine();
		if(choice.contains("0")) {
			
		}
		return null;
	}
	
	
	
}
