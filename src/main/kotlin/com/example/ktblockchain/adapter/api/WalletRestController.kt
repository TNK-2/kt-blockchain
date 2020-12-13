package com.example.ktblockchain.adapter.api

import com.example.ktblockchain.usecase.WalletService
import com.example.ktblockchain.utils.ObjectSerializer
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class WalletRestController(
  private val walletService: WalletService
) {

  @PostMapping("/wallet")
  fun createWallet(): CreateWalletResponse {
    val newMyWallet = walletService.createWallet()
    return CreateWalletResponse(
      // TODO keyクラスをそのままシリアライズはよくないので暗号化する
      privateKey = ObjectSerializer.serialize(newMyWallet.privateKey),
      publicKey = ObjectSerializer.serialize(newMyWallet.publicKey),
      blockChainAddress = newMyWallet.blockChainAddress
    )
  }

  @PostMapping("/transaction")
  fun createTransaction(
    @RequestBody createTransactionRequest: CreateWalletTransactionRequest
  ): CreateBlockTransactionResponse {
    walletService.createTransaction(
      senderBlockChainAddress = createTransactionRequest.senderBlockChainAddress,
      senderPrivateKey = createTransactionRequest.senderPrivateKey,
      senderPublicKey = createTransactionRequest.senderPublicKey,
      value = createTransactionRequest.value,
      recipientBlockChainAddress = createTransactionRequest.recipientBlockChainAddress
    )
    return CreateBlockTransactionResponse(message = "success")
  }

}