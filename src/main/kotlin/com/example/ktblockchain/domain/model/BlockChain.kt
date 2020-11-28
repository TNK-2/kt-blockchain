package com.example.ktblockchain.domain.model

import com.example.ktblockchain.config.AppConf
import com.example.ktblockchain.utils.HashUtil
import com.example.ktblockchain.utils.KtLog

class BlockChain(
  private var transactionPool: MutableList<Transaction> = mutableListOf(),
  private val chain: MutableList<Block> = mutableListOf(),
  private val blockChainAddress: String
) {

  init {
    this.createBlock(0, "init hash")
  }

  companion object {
    val logger = KtLog.logger
  }

  private fun createBlock(
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

  private fun proofOfWork(): Int {
    val transactions = this.transactionPool.toList()
    val previousHash = this.getLastBlockHash()
    val timestamp = System.currentTimeMillis()
    var nonce = 0;
    while (!this.validProof(transactions = transactions, previousHash = previousHash, nonce = nonce, timestamp = timestamp)) {
      nonce++
    }
    return nonce
  }

  fun mining(): Boolean {
    this.addTransaction(
      senderBlockChainAddress = AppConf.MINING_SENDER,
      recipientBlockChainAddress = this.blockChainAddress,
      value = AppConf.MINING_REWARD
    )
    val nonce = this.proofOfWork()
    this.createBlock(nonce = nonce)
    logger.info("{ \"action\" : \"mining\", \"status\" = \"success\" }")
    return true
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