package com.example.ktblockchain.config

object AppConf {
  const val MINING_DIFFICULTY = 3
  const val MESSAGE_DIGEST_ALGO = "SHA-256"
  const val MINING_SENDER = "THE BLOCKCHAIN"
  const val MINING_REWARD = 1.0
  const val SIGNATURE_ALGO = "SHA256withECDSA"
  const val PORT = "8080"
  const val BLOCKCHAIN_SERVER_URL = "http://localhost:8080"
  const val CREATE_TRANSACTION_PATH = "/transactions"
}