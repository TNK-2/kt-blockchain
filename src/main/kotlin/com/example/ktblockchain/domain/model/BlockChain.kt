package com.example.ktblockchain.domain.model

import com.example.ktblockchain.config.AppConf
import com.example.ktblockchain.utils.HashUtil
import java.security.MessageDigest

class BlockChain(
  private var transactionPool: MutableList<Transaction> = mutableListOf(),
  private val chain: MutableList<Block> = mutableListOf()
) {

  init {
    this.createBlock(0, "init hash")
  }

  fun createBlock(
    nonce: Int,
    previousHash: String = this.getLastBlockHash(),
    timestamp: Long = System.currentTimeMillis()
  ) {
    val block = Block(
      timestamp = timestamp,
      nonce = nonce,
      previousHash = previousHash,
      transactions = this.transactionPool
    )
    this.chain.add(block)
    this.transactionPool = mutableListOf()
  }

  fun getBlockHash(block: Block): String =
    HashUtil.getSha256Hash(block.toString())

  fun getLastBlockHash(): String =
    this.getBlockHash(this.chain.last())

  fun addTransaction(
    senderBlockChainAddress: String,
    recipientBlockChainAddress: String,
    value: Double
  ) {
    transactionPool.add(
      Transaction(
        senderBlockChainAddress = senderBlockChainAddress,
        recipientBlockChainAddress = recipientBlockChainAddress,
        value = value
      )
    )
  }

  private fun validProof(
    transactions: List<Transaction>,
    previousHash: String,
    nonce: Int,
    difficulty: Int = AppConf.MINING_DIFFICULTY,
    timestamp: Long
  ): Boolean {
    val guessHash = this.getBlockHash(
      block = Block(
        timestamp = timestamp,
        previousHash = previousHash,
        nonce = nonce,
        transactions = transactions
      )
    )
    return guessHash.substring(0, difficulty) == "0".repeat(difficulty)
  }

  fun proofOfWork(): Int {
    val transactions = this.transactionPool.toList()
    val previousHash = this.getLastBlockHash()
    val timestamp = System.currentTimeMillis()
    var nonce = 0;
    while (!this.validProof(transactions = transactions, previousHash = previousHash, nonce = nonce, timestamp = timestamp)) {
      nonce++
    }
    return nonce
  }

  fun print(chains: List<Block> = this.chain) {
    println("***************************")
    chains.forEachIndexed { i, block ->
      println("------print chain : $i ------")
      println("timestamp : " + block.timestamp)
      println("nonce : " + block.nonce)
      println("previous hash : " + block.previousHash)
      println("transactions : " + block.transactions)
      println("block hash : " + this.getBlockHash(block))
    }
  }
}