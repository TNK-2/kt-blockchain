package com.example.ktblockchain.adapter.infra.repository

import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.repository.BlockChainRepository
import com.example.ktblockchain.utils.KtLog
import org.springframework.stereotype.Repository

@Repository
class BlockChainRepositoryImpl : BlockChainRepository {

  companion object {
    private val blockChainMap = mutableMapOf<String, BlockChain>()
  }

  override fun findOne(): BlockChain? = blockChainMap["blockchain"]

  override fun store(blockChain: BlockChain): BlockChain {
    blockChainMap["blockchain"] = blockChain
    return blockChain
  }

}