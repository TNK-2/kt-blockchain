package com.example.ktblockchain.domain

class Block(
  val timestamp: String,
  val transactions: List<Transaction>,
  val nonce: Int,
  val previousHash: String
)