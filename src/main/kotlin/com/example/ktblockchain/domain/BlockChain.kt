package com.example.ktblockchain.domain

import java.time.Instant
import java.time.format.DateTimeFormatter

class BlockChain(
  val transactionPool: MutableList<Transaction> = mutableListOf(),
  val chain: MutableList<Block> = mutableListOf()
) {

  init {
    this.createBlock(0, "init hash")
  }

  fun createBlock(nonce: Int, previousHash: String) {
    val block = Block(
      timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
      nonce = nonce,
      previousHash = previousHash,
      transactions = this.transactionPool
    )
    this.chain.add(block)
  }

  fun print(chains: List<Block> = this.chain) {
    println("***************************")
    chains.forEachIndexed { i, chain ->
      println("------print chain : $i ------")
      println("timestamp : " + chain.timestamp)
      println("nonce : " + chain.nonce)
      println("previous hash" + chain.previousHash)
      println("transactions : " + chain.transactions)
    }
  }
}