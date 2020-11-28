package com.example.ktblockchain.domain.model

data class Transaction(
  val senderBlockChainAddress: String,
  val recipientBlockChainAddress: String,
  val value: Double
)