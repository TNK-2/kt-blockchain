package com.example.ktblockchain.domain.model

import java.sql.Timestamp

class BlockChain(
  val transactionPool: MutableList<Transaction> = mutableListOf(),
  val chain: MutableList<Block> = mutableListOf()
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
  }

  fun getBlockHash(block: Block): String =
    block.hashCode().toString()

  fun getLastBlockHash(): String =
    this.getBlockHash(this.chain.last())

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