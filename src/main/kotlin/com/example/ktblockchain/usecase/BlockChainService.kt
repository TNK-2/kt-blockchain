package com.example.ktblockchain.usecase

import com.example.ktblockchain.config.AppConf
import com.example.ktblockchain.domain.factory.BlockChainFactory
import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.repository.BlockChainRepository
import com.example.ktblockchain.domain.service.BlockChainDomainService
import com.example.ktblockchain.utils.ObjectSerializer
import org.springframework.stereotype.Service
import java.lang.IllegalStateException
import java.security.PublicKey
import javax.annotation.PostConstruct
import kotlin.concurrent.thread

@Service
class BlockChainService(
  private val blockChainRepository: BlockChainRepository,
  private val blockChainDomainService: BlockChainDomainService,
  private val blockChainFactory: BlockChainFactory
) {

  @PostConstruct
  fun postConstruct() {
    val blockChain = blockChainRepository.store(blockChain = blockChainFactory.new())
    blockChain.syncNeighbours()
    blockChainDomainService.resolveConflicts(blockChain = blockChain)
    thread {
      while (true) {
        blockChainDomainService.mining(blockChain = blockChain)
        Thread.sleep(AppConf.BLOCKCHAIN_NEIGHBOURS_SYNC_TIME_SEC.toLong())
      }
    }
  }

  fun getBlockChain(): BlockChain =
    blockChainRepository.findOne()
      ?: blockChainRepository.store(blockChain = blockChainFactory.new())

  fun createTransaction(
    senderBlockChainAddress: String,
    recipientBlockChainAddress: String,
    value: Double,
    senderPublicKey: String,
    hexSignature: String
  ) {
    val blockChain = blockChainRepository.findOne()
      ?: throw IllegalStateException("ブロックチェーンが存在しません")

    blockChain.addTransaction(
      senderBlockChainAddress = senderBlockChainAddress,
      senderPublicKey = ObjectSerializer.deSerialize(senderPublicKey) as PublicKey,
      hexSignature = hexSignature,
      value = value,
      recipientBlockChainAddress = recipientBlockChainAddress
    )

    blockChainDomainService.syncTransaction(
      senderBlockChainAddress = senderBlockChainAddress,
      senderPublicKey = senderPublicKey,
      hexSignature = hexSignature,
      value = value,
      recipientBlockChainAddress = recipientBlockChainAddress,
      blockChain = blockChain
    )
  }

  fun addTransaction(
    senderBlockChainAddress: String,
    recipientBlockChainAddress: String,
    value: Double,
    senderPublicKey: String,
    hexSignature: String
  ) {
    val blockChain = blockChainRepository.findOne()
      ?: throw IllegalStateException("ブロックチェーンが存在しません")

    blockChain.addTransaction(
      senderBlockChainAddress = senderBlockChainAddress,
      senderPublicKey = ObjectSerializer.deSerialize(senderPublicKey) as PublicKey,
      hexSignature = hexSignature,
      value = value,
      recipientBlockChainAddress = recipientBlockChainAddress
    )

  }

  fun deleteTransaction() {
    val blockChain = blockChainRepository.findOne()
      ?: throw IllegalStateException("ブロックチェーンが存在しません")
    blockChain.transactionPool = mutableListOf()
  }

  fun mine(): Boolean {
    val blockChain = blockChainRepository.findOne()
      ?: throw IllegalStateException("ブロックチェーンが存在しません")
    return blockChainDomainService.mining(blockChain = blockChain)
  }

  fun consensus(): Boolean {
    val blockChain = blockChainRepository.findOne()
      ?: throw IllegalStateException("ブロックチェーンが存在しません")
    return blockChainDomainService.resolveConflicts(blockChain = blockChain)
  }

  fun totalAmount(blockChainAddress: String): Double {
    val blockChain = blockChainRepository.findOne()
      ?: throw IllegalStateException("ブロックチェーンが存在しません")
    return blockChain.calculateTotalAmount(blockChainAddress = blockChainAddress)
  }
}