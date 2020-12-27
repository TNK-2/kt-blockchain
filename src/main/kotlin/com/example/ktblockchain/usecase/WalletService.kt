package com.example.ktblockchain.usecase

import com.example.ktblockchain.domain.model.wallet.Transaction
import com.example.ktblockchain.domain.model.wallet.Wallet
import com.example.ktblockchain.domain.repository.WalletTransactionRepository
import com.example.ktblockchain.utils.ObjectSerializer
import org.springframework.stereotype.Service
import java.security.PrivateKey
import java.security.PublicKey

@Service
class WalletService(
  private val walletTransactionRepository: WalletTransactionRepository
) {

  fun createWallet(): Wallet = Wallet()

  fun createTransaction(
    senderBlockChainAddress: String,
    recipientBlockChainAddress: String,
    value: Double,
    senderPublicKey: String,
    senderPrivateKey: String,
  ) {

    walletTransactionRepository.store(
      transaction = Transaction(
        senderBlockChainAddress = senderBlockChainAddress,
        recipientBlockChainAddress = recipientBlockChainAddress,
        value = value,
        senderPublicKey = ObjectSerializer.deSerialize(senderPublicKey) as PublicKey,
        senderPrivateKey = ObjectSerializer.deSerialize(senderPrivateKey) as PrivateKey
      )
    )
  }

  fun getTotalAmount(blockChainAddress: String): Double =
    walletTransactionRepository.getTotalAmount(blockChainAddress = blockChainAddress)
}