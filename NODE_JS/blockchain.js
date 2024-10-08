const crypto = require('crypto');

class Block {
  constructor(index, timestamp, data, previousHash = '') {
    this.index = index;
    this.timestamp = timestamp;
    this.data = data;
    this.previousHash = previousHash;
    this.hash = this.calculateHash();
  }

  calculateHash() {
    return crypto.createHash('sha256').update(
      this.index + this.previousHash + this.timestamp + JSON.stringify(this.data)
    ).digest('hex');
  }
}

class Blockchain {
  constructor() {
    this.chain = [this.createGenesisBlock()];
  }

  createGenesisBlock() {
    return new Block(0, Date.now(), "Genesis Block", "0");
  }

  getLatestBlock() {
    return this.chain[this.chain.length - 1];
  }

  addBlock(newBlock) {
    newBlock.previousHash = this.getLatestBlock().hash;
    newBlock.hash = newBlock.calculateHash();
    this.chain.push(newBlock);

    console.log("New block added to the blockchain:");
    console.log(`Index: ${newBlock.index}`);
    console.log(`Timestamp: ${newBlock.timestamp}`);
    console.log(`Data: ${JSON.stringify(newBlock.data)}`);
    console.log(`Previous Hash: ${newBlock.previousHash}`);
    console.log(`Hash: ${newBlock.hash}`);
  }

  isChainValid() {
    for (let i = 1; i < this.chain.length; i++) {
      const currentBlock = this.chain[i];
      const previousBlock = this.chain[i - 1];

      if (currentBlock.hash !== currentBlock.calculateHash()) {
        return false;
      }

      if (currentBlock.previousHash !== previousBlock.hash) {
        return false;
      }
    }
    return true;
  }

  printBlockchain() {
    console.log("Blockchain:");
    this.chain.forEach((block) => {
      console.log(`Index: ${block.index}`);
      console.log(`Timestamp: ${block.timestamp}`);
      console.log(`Data: ${JSON.stringify(block.data)}`);
      console.log(`Previous Hash: ${block.previousHash}`);
      console.log(`Hash: ${block.hash}`);
      console.log("----------------------------");
    });
  }
}

module.exports = { Blockchain, Block };
