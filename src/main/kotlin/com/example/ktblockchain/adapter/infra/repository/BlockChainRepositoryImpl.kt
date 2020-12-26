package com.example.ktblockchain.adapter.infra.repository

import com.example.ktblockchain.adapter.api.CreateBlockTransactionRequest
import com.example.ktblockchain.config.AppConf
import com.example.ktblockchain.domain.model.blockchain.Block
import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.repository.BlockChainRepository
import com.example.ktblockchain.utils.KtLog
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

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
    val url = "http://" + recipientHost + AppConf.CREATE_TRANSACTION_PATH
    // TODO keyクラスをそのままシリアライズはよくないので暗号化する
    val request = CreateBlockTransactionRequest(
      senderBlockChainAddress = senderBlockChainAddress,
      recipientBlockChainAddress = recipientBlockChainAddress,
      value = value,
      senderPublicKey = senderPublicKey,
      signature = hexSignature,
    )
    KtLog.logger.info("BlockChainRepositoryImpl.putTransaction... URL : %s".format(url))
    restTemplate.put("http://" + recipientHost + AppConf.CREATE_TRANSACTION_PATH, request)
  }

  override fun deleteTransaction(recipientHost: String) {
    val url = "http://" + recipientHost + AppConf.CREATE_TRANSACTION_PATH
    KtLog.logger.info("BlockChainRepositoryImpl.deleteTransaction... URL : %s".format(url))
    restTemplate.delete("http://" + recipientHost + AppConf.CREATE_TRANSACTION_PATH)
  }

  override fun getChain(hostIp: String): List<Block> {
    val url = "http://" + hostIp + AppConf.CHAIN_PATH
    KtLog.logger.info("BlockChainRepositoryImpl.getChain... URL : %s".format(url))
    return (restTemplate.getForObject(url) as Array<Block>).map {
      Block(
        timestamp = it.timestamp,
        nonce = it.nonce,
        previousHash = it.previousHash,
        transactions = it.transactions
      )
    }
  }

  override fun consensus(hostIp: String) {
    val url = "http://" + hostIp + AppConf.CREATE_CONSENSUS_PATH
    KtLog.logger.info("BlockChainRepositoryImpl.consensus... URL : %s".format(url))
    restTemplate.put(url, null)
  }

}