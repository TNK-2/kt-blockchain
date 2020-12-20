package com.example.ktblockchain.domain.service

import com.example.ktblockchain.config.AppConf
import com.example.ktblockchain.domain.model.blockchain.Block
import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.repository.BlockChainRepository
import com.example.ktblockchain.utils.KtLog
import org.springframework.stereotype.Service

@Service
class BlockChainDomainService(
  private val blockChainRepository: BlockChainRepository
) {

  fun createBlock(
    nonce: Int,
    previousHash: String,
    timestamp: Long = System.currentTimeMillis(),
    blockChain: BlockChain
  ) {

    val block = Block(
      timestamp = timestamp,
      nonce = nonce,
      previousHash = previousHash,
      transactions = blockChain.transactionPool
    )
    blockChain.chain.add(block)
    blockChain.transactionPool = mutableListOf()

    blockChain.neighbours.forEach { neighbour ->
      blockChainRepository.deleteTransaction(recipientHost = neighbour)
    }
  }

  fun mining(
    blockChain: BlockChain
  ): Boolean {
    blockChain.addTransaction(
      senderBlockChainAddress = AppConf.MINING_SENDER,
      recipientBlockChainAddress = blockChain.blockChainAddress,
      value = AppConf.MINING_REWARD
    )
    val nonce = blockChain.proofOfWork()
    this.createBlock(
      nonce = nonce,
      previousHash = blockChain.getLastBlockHash(),
      blockChain = blockChain
    )
    KtLog.logger.info("{ \"action\" : \"mining\", \"status\" : \"success\" }")
    return true
  }

  fun syncTransaction(
    blockChain: BlockChain,
    senderBlockChainAddress: String,
    recipientBlockChainAddress: String,
    value: Double,
    senderPublicKey: String,
    hexSignature: String
  ){
    blockChain.neighbours.forEach { neighbour ->
      blockChainRepository.putTransaction(
        recipientHost = neighbour,
        recipientBlockChainAddress = recipientBlockChainAddress,
        senderPublicKey = senderPublicKey,
        value = value,
        hexSignature = hexSignature,
        senderBlockChainAddress = senderBlockChainAddress
      )
    }
  }
}