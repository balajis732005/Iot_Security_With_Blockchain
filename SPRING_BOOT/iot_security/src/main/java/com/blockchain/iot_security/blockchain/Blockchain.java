package com.blockchain.iot_security.blockchain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Blockchain {

    public static class Block {
        public int index;
        private final long timestamp;
        private final String data;
        private final String previousHash;
        private final String hash;

        public Block(int index, long timestamp, String data, String previousHash) {
            this.index = index;
            this.timestamp = timestamp;
            this.data = data;
            this.previousHash = previousHash;
            this.hash = calculateHash();
        }

        public String calculateHash() {
            return Integer.toHexString((index + Long.toString(timestamp) + data + previousHash).hashCode());
        }

        public String getData() {
            return data;
        }

        public String getHash() {
            return hash;
        }

        public String getPreviousHash() {
            return previousHash;
        }
    }

    private final List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        return new Block(0, new Date().getTime(), "Genesis Block", "0");
    }

    public Block getLatestBlock() {
        return chain.getLast();
    }

    public void addBlock(String data) {
        Block latestBlock = getLatestBlock();
        Block newBlock = new Block(chain.size(), new Date().getTime(), data, latestBlock.getHash());
        chain.add(newBlock);
    }

    public List<Block> getChain() {
        return chain;
    }
}
