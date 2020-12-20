package com.example.ktblockchain.domain.repository

import com.example.ktblockchain.domain.model.blockchain.BlockChain

interface BlockChainRepository {
  fun findOne(): BlockChain?
  fun store(blockChain: BlockChain): BlockChain
  fun putTransaction(
    recipientHost: String,
    senderBlockChainAddress: String,
    recipientBlockChainAddress: String,
    value: Double,
    senderPublicKey: String,
    hexSignature: String
  )
  fun deleteTransaction(recipientHost: String)
}