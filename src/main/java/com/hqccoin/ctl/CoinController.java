package com.hqccoin.ctl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.hqcoin.entity.Block;
import com.hqcoin.entity.BlockChain;


@RestController
public class CoinController {
	private BlockChain blockChain = new BlockChain();

    @RequestMapping(method=RequestMethod.GET, value="/blocks")
    public String chain() throws JsonProcessingException {
    	return generateJson(blockChain.getBlocks());
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/block")
    public String block(@RequestParam(value="id", defaultValue="0") int id){
    	Block block = blockChain.getBlock(id);
    	return generateJson(block);
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/transcation/new")
    public String newTranscation(){
        return generateJson(blockChain.newTranscation());
    }
    
    @RequestMapping("/chain/check")
    public String checkChain(){
    	return blockChain.checkChain();
    }
    
    private String generateJson(Object object) {
		Gson gson = new Gson();
		String json = gson.toJson(object);
		return json;
	}

}
