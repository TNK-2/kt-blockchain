package com.example.ktblockchain.usecase

import com.example.ktblockchain.domain.model.wallet.Wallet
import org.springframework.stereotype.Service

@Service
class WalletService {

  fun createWallet(): Wallet = Wallet()
}