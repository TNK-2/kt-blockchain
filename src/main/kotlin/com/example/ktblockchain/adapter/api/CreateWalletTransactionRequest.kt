package com.example.ktblockchain.adapter.api

data class CreateWalletTransactionRequest(
  val senderBlockChainAddress: String,
  val recipientBlockChainAddress: String,
  val value: Double,
  val senderPublicKey: String,
  val senderPrivateKey: String
)