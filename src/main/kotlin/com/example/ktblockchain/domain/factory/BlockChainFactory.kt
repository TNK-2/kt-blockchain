package com.example.ktblockchain.domain.factory

import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.model.wallet.Wallet
import com.example.ktblockchain.domain.service.BlockChainDomainService
import com.example.ktblockchain.utils.KtLog
import com.example.ktblockchain.utils.ObjectSerializer
import org.springframework.stereotype.Component

@Component
class BlockChainFactory(
  private val blockChainDomainService: BlockChainDomainService
) {

  companion object {
    private val logger = KtLog.logger
  }

  fun new(): BlockChain {
    val minersWallet = Wallet()
    val blockChain = BlockChain(
      blockChainAddress = minersWallet.blockChainAddress
    )

    blockChainDomainService.createBlock(
      nonce = 0,
      previousHash = "InitHash",
      blockChain = blockChain
    )

    logger.warn("ブロックチェーンインスタンスが新規に作成されました。")
    logger.warn("private key : " + ObjectSerializer.serialize(minersWallet.privateKey))
    logger.warn("public key : " + ObjectSerializer.serialize(minersWallet.publicKey))
    logger.warn("blockchain addr : " + minersWallet.blockChainAddress)
    return blockChain
  }
}