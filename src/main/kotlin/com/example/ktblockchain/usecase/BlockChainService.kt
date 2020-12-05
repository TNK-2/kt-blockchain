package com.example.ktblockchain.usecase

import com.example.ktblockchain.domain.factory.BlockChainFactory
import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.repository.BlockChainRepository
import org.springframework.stereotype.Service

@Service
class BlockChainService(
  private val blockChainRepository: BlockChainRepository,
  private val blockChainFactory: BlockChainFactory
) {

  fun getBlockChain(): BlockChain =
    blockChainRepository.findOne()
      ?: blockChainRepository.store(blockChain = blockChainFactory.new())
}