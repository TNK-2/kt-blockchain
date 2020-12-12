package com.example.ktblockchain.usecase

import com.example.ktblockchain.domain.factory.BlockChainFactory
import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.repository.BlockChainRepository
import com.example.ktblockchain.utils.ObjectSerializer
import org.springframework.stereotype.Service
import java.lang.IllegalStateException
import java.security.PublicKey

@Service
class BlockChainService(
  private val blockChainRepository: BlockChainRepository,
  private val blockChainFactory: BlockChainFactory
) {

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
      ?: throw IllegalStateException("ブロックチェーンが作成されていません")

    blockChain.createTransaction(
      senderBlockChainAddress = senderBlockChainAddress,
      senderPublicKey = ObjectSerializer.deSerialize(senderPublicKey) as PublicKey,
      hexSignature = hexSignature,
      value = value,
      recipientBlockChainAddress = recipientBlockChainAddress
    )
  }
}