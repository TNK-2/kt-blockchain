package com.example.ktblockchain.adapter.infra.repository.request

data class WalletTransactionRequest(
  val senderPrivateKey: String,
  val senderPublicKey: String,
  val senderBlockChainAddress: String,
  val recipientBlockChainAddress: String,
  val value: Double
)