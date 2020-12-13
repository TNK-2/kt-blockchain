package com.example.ktblockchain.adapter.infra.repository

import com.example.ktblockchain.adapter.api.CreateBlockTransactionRequest
import com.example.ktblockchain.adapter.api.CreateBlockTransactionResponse
import com.example.ktblockchain.config.AppConf
import com.example.ktblockchain.domain.model.wallet.Transaction
import com.example.ktblockchain.domain.repository.WalletTransactionRepository
import com.example.ktblockchain.utils.ObjectSerializer
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForObject

@Repository
class WalletTransactionRepositoryImpl(
  private val restTemplate: RestTemplate
): WalletTransactionRepository {

  override fun store(transaction: Transaction): Transaction {
    // TODO keyクラスをそのままシリアライズはよくないので暗号化する
    val request = CreateBlockTransactionRequest(
      senderBlockChainAddress = transaction.senderBlockChainAddress,
      recipientBlockChainAddress = transaction.recipientBlockChainAddress,
      value = transaction.value,
      senderPublicKey = ObjectSerializer.serialize(transaction.senderPublicKey),
      signature = transaction.generateSignature(),
    )
    restTemplate.postForObject<CreateBlockTransactionResponse>(AppConf.BLOCKCHAIN_SERVER_URL + AppConf.CREATE_TRANSACTION_PATH, request)
    return transaction
  }
}