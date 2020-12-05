package com.example.ktblockchain.domain.repository

import com.example.ktblockchain.domain.model.blockchain.BlockChain

interface BlockChainRepository {
  fun findOne(): BlockChain?
  fun store(blockChain: BlockChain): BlockChain
}