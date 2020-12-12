package com.example.ktblockchain.adapter.api

import com.example.ktblockchain.usecase.WalletService
import com.example.ktblockchain.utils.ObjectSerializer
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WalletRestController(
  private val walletService: WalletService
) {

  @PostMapping("/wallet")
  fun createWallet(): CreateWalletResponse {
    val newMyWallet = walletService.createWallet()
    return CreateWalletResponse(
      privateKey = ObjectSerializer.serialize(newMyWallet.privateKey),
      publicKey = ObjectSerializer.serialize(newMyWallet.publicKey),
      blockChainAddress = newMyWallet.blockChainAddress
    )
  }

  @PostMapping("/transaction")
  fun createTransaction(
    @RequestParam senderBlockChainAddress: String,
    @RequestParam recipientBlockChainAddress: String,
    @RequestParam value: Double,
    @RequestParam senderPublicKey: String,
    @RequestParam senderPrivateKey: String
  ): String {
    walletService.createTransaction(
      senderBlockChainAddress = senderBlockChainAddress,
      senderPrivateKey = senderPrivateKey,
      senderPublicKey = senderPublicKey,
      value = value,
      recipientBlockChainAddress = recipientBlockChainAddress
    )
    return "{ \"message\" = \"success\" }"
  }

}