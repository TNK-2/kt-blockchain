package com.example.ktblockchain.adapter.api

data class CreateBlockTransactionRequest(
  val senderBlockChainAddress: String,
  val recipientBlockChainAddress: String,
  val value: Double,
  val senderPublicKey: String,
  val signature: String
)