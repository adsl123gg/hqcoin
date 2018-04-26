package com.hqcoin.entity;

public class Transcation {
	private String sender;
	private String recipient;
	private double amount;
	
	public Transcation(String sender, String recipient, double amount) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
	}

	public String getSender() {
		return sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public double getAmount() {
		return amount;
	}
	
	

}
