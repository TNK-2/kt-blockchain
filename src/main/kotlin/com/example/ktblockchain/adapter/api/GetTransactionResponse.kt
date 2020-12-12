package com.example.ktblockchain.adapter.api

import com.example.ktblockchain.domain.model.blockchain.Transaction

data class GetTransactionResponse(
  val transactions: List<Transaction>
) {
  val length: Int
    get() = this.transactions.size
}