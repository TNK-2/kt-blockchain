package com.example.ktblockchain.adapter.api

import com.example.ktblockchain.usecase.WalletService
import com.example.ktblockchain.utils.ObjectSerializer
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
      privateKey = ObjectSerializer.serialize(newMyWallet.privateKey),
      publicKey = ObjectSerializer.serialize(newMyWallet.publicKey),
      blockChainAddress = newMyWallet.blockChainAddress
    )
  }
}