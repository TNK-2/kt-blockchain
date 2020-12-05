package com.example.ktblockchain.domain.model.blockchain

data class Block(
  val timestamp: Long,
  val transactions: List<Transaction>,
  val nonce: Int,
  val previousHash: String
)