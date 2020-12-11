package com.example.ktblockchain.adapter.api

import com.example.ktblockchain.usecase.WalletService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/wallet")
class WalletRestController(
  private val walletService: WalletService
) {

  @PostMapping
  fun create(): CreateWalletResponse {
    val newMyWallet = walletService.createWallet()
    return CreateWalletResponse(
      privateKey = newMyWallet.privateKey.encoded.toString(),
      publicKey = newMyWallet.publicKey.encoded.toString(),
      blockChainAddress = newMyWallet.blockChainAddress
    )
  }
}