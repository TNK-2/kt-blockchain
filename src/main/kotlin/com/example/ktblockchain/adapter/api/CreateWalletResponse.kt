package com.example.ktblockchain.adapter.api

data class CreateWalletResponse (
  val publicKey: String,
  val privateKey: String,
  val blockChainAddress: String
)