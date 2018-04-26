package com.hqcoin.entity;

import java.util.ArrayList;

import com.hqccoin.util.Util;

public class Block {
	private int height;
	private String prevHash;
	private String merkleRoot;
	private long timeStamp;
	private ArrayList<Transcation> transcations = new ArrayList<Transcation>();
	private long nonce;
	
	public void setNonce(long nonce) {
		this.nonce = nonce;
	}
	public Block() {
		this.timeStamp = System.currentTimeMillis();
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void addTranscations(Transcation transcation) {
		transcations.add(transcation);
		setMerkleRoot();
	}
	public ArrayList<Transcation> getTranscations() {
		return transcations;
	}
	public String getPrevHash() {
		return prevHash;
	}
	public void setPrevHash(String prevHash) {
		this.prevHash = prevHash;
	}
	public String getMerkleRoot() {
		return merkleRoot;
	}
	public void setMerkleRoot() {
		this.merkleRoot = Util.hash256(getTranscations().toString());
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
}
