package com.hqcoin.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hqccoin.util.Http;
import com.hqccoin.util.Util;


public class BlockChain {
	
	private List<Block> blocks;
	private final long MAX = Long.MAX_VALUE;
	private final long BLOCKINTERVALTIME = 20*1000;
	
	public BlockChain() {
		blocks = new ArrayList<>();
		blocks.add(createFirstBlock());
		Timer timer = new Timer();  
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                 newBlock();
            }
        }, 1000, BLOCKINTERVALTIME);
	}
	
	private Block createFirstBlock() {
		Block block = new Block();
		block.setHeight(0);
		block.setNonce(0);
		block.setPrevHash("000000000000000000");
		block.setMerkleRoot();
		return block;
	}

	public Block newBlock(){
		Block lastBlk = lastBlock();
		Block block = new Block();
		block.setHeight(lastBlk.getHeight()+1);
		block.setPrevHash(Util.hash256(getJson(lastBlk)));
		block.setMerkleRoot();
		mineBlock(block);
//		ArrayList<Block> newBlocks = getBlockChainFromNode();
//		if (newBlocks != null) {
//			if (newBlocks.size() > this.blocks.size()) {
//				this.blocks = newBlocks;
//			}else if (lastBlk.getHash().equals(newBlocks.get(newBlocks.size()-1).getHash())) {
//				blocks.add(block);
//			}
//		}else {
//			blocks.add(block);
//		}
		
		blocks.add(block);
		return block;
	}

	private void mineBlock(Block block) {
		long index = 0;
		String tmpHash = null;
		while (index++ < MAX) {
			block.setNonce(index);
			tmpHash = Util.hash256(getJson(block));
			if (tmpHash.startsWith("0000")) {
				return;
			}
		}
		block.setNonce(-1);
	}

	private String getJson(Block block) {
		Gson gson = new Gson();
		String json = gson.toJson(block);
		JsonObject data = new JsonParser().parse(json).getAsJsonObject();
		data.remove("transcations");
		return data.toString();
	}
	
	public Transcation newTranscation(){
		Transcation transcation = new Transcation(generateName(), generateName(), (int)(Math.random()*100)/100.0);
		lastBlock().addTranscations(transcation);
		return transcation;
	}
	
	private Block lastBlock() {
		return blocks.get(blocks.size()-1);
	}

	public List<Block> getBlocks() {
		return blocks;
	}
	
	private String generateName() {
		String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    StringBuffer sb = new StringBuffer();
	    int len = KeyString.length();
	    for (int i = 0; i < 5; i++) {
	       sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
	    }
	    return sb.toString();
	}

	public Block getBlock(int id) {
		if (id>= blocks.size()) {
			id = 0;
		}
		return blocks.get(id);
	}

	public String checkChain() {
		ArrayList<Block> newBlocks = getBlockChainFromNode();
		if (newBlocks != null) {
			if (newBlocks.size() > this.blocks.size()) {
				this.blocks = newBlocks;
				return "replaced with longer blockchain";
			}else {
				return "no more new blocks";
			}
		}else {
			return "check chain fail";
		}
	}

	private ArrayList<Block> getBlockChainFromNode() {
		String url = "http://localhost:8080/blocks";
		String json = Http.sendGet(url);
		if (json != null && !json.contains("fail")) {
			JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();
			ArrayList<Block> newBlocks = parseJsonArray(jsonArray);
			if (validBlockChain(newBlocks)) {
				return newBlocks;
			}
		}
		return null;
	}

	private ArrayList<Block> parseJsonArray(JsonArray jsonArray) {
		ArrayList<Block> blocks = new ArrayList<Block>();
		Gson gson = new Gson();
		for (int i = 0; i < jsonArray.size(); i++) {
			blocks.add(gson.fromJson(jsonArray.get(i).toString(), Block.class));
		}
		return blocks;
	}

	private boolean validBlockChain(List<Block> newBlocks) {
		if (newBlocks.size() > 1) {
			Block prevBlock = null;
			Block block = null;
			for (int i = 1; i < newBlocks.size(); i++) {
				prevBlock = newBlocks.get(i-1);
				block = newBlocks.get(i);
				if (prevBlock.getHeight() + 1 != block.getHeight()) {
					return false;
				}
//				else if (!block.getPrev_hash().equals(hash256(getJson(prevBlock)))) {
//					return false;
//				}else if (!block.getHash().equals(hash256(getJson(block)))) {
//					return false;
//				}
			}
		}
		return true;
	}

}
