package com.example.ktblockchain.adapter.infra.repository

import com.example.ktblockchain.adapter.api.CreateBlockTransactionRequest
import com.example.ktblockchain.config.AppConf
import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.model.wallet.Transaction
import com.example.ktblockchain.domain.repository.BlockChainRepository
import com.example.ktblockchain.utils.KtLog
import com.example.ktblockchain.utils.ObjectSerializer
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class BlockChainRepositoryImpl(
  private val restTemplate: RestTemplate
) : BlockChainRepository {

  companion object {
    private val blockChainMap = mutableMapOf<String, BlockChain>()
  }

  override fun findOne(): BlockChain? = blockChainMap["blockchain"]

  override fun store(blockChain: BlockChain): BlockChain {
    blockChainMap["blockchain"] = blockChain
    return blockChain
  }

  override fun putTransaction(
    recipientHost: String,
    senderBlockChainAddress: String,
    recipientBlockChainAddress: String,
    value: Double,
    senderPublicKey: String,
    hexSignature: String
  ) {
    // TODO keyクラスをそのままシリアライズはよくないので暗号化する
    val request = CreateBlockTransactionRequest(
      senderBlockChainAddress = senderBlockChainAddress,
      recipientBlockChainAddress = recipientBlockChainAddress,
      value = value,
      senderPublicKey = senderPublicKey,
      signature = hexSignature,
    )
    KtLog.logger.info("BlockChainRepositoryImpl.putTransaction... URL : %s".format("http://" + recipientHost + AppConf.CREATE_TRANSACTION_PATH))
    restTemplate.put("http://" + recipientHost + AppConf.CREATE_TRANSACTION_PATH, request)
  }

  override fun deleteTransaction(recipientHost: String) {
    KtLog.logger.info("BlockChainRepositoryImpl.deleteTransaction... URL : %s".format("http://" + recipientHost + AppConf.CREATE_TRANSACTION_PATH))
    restTemplate.delete("http://" + recipientHost + AppConf.CREATE_TRANSACTION_PATH)
  }

}