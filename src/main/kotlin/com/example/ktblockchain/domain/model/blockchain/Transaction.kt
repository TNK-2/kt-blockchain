package com.example.ktblockchain.domain.model.blockchain

data class Transaction(
  val senderBlockChainAddress: String,
  val recipientBlockChainAddress: String,
  val value: Double
)