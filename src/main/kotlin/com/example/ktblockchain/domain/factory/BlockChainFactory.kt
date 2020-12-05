package com.example.ktblockchain.domain.factory

import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.model.wallet.Wallet
import com.example.ktblockchain.utils.KtLog
import org.springframework.stereotype.Component

@Component
class BlockChainFactory {

  companion object {
    private val logger = KtLog.logger
  }

  fun new(): BlockChain {
    val minersWallet = Wallet()
    logger.warn("private key : " + minersWallet.privateKey)
    logger.warn("public key : " + minersWallet.publicKey)
    logger.warn("blockchain addr : " + minersWallet.blockChainAddress)
    return BlockChain(
      blockChainAddress = minersWallet.blockChainAddress
    )
  }
}