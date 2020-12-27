package com.example.ktblockchain.domain.repository

import com.example.ktblockchain.domain.model.wallet.Transaction

interface WalletTransactionRepository {
  fun store(transaction: Transaction): Transaction
  fun getTotalAmount(blockChainAddress: String): Double
}