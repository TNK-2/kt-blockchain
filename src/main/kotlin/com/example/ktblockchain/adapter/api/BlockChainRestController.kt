package com.example.ktblockchain.adapter.api

import com.example.ktblockchain.domain.model.blockchain.Block
import com.example.ktblockchain.usecase.BlockChainService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BlockChainRestController(
  private val blockChainService: BlockChainService
){

  @GetMapping("/chain")
  fun getChain(): List<Block> {
    val blockChain = blockChainService.getBlockChain()
    return blockChain.chain
  }

  @GetMapping("/transactions")
  fun getTransaction(): GetTransactionResponse {
    val blockChain = blockChainService.getBlockChain()
    return GetTransactionResponse(
      transactions = blockChain.transactionPool
    )
  }

  @PostMapping("/transactions")
  fun createTransaction(
    @RequestBody createTransactionRequest: CreateBlockTransactionRequest
  ): CreateBlockTransactionResponse {
    blockChainService.createTransaction(
      senderBlockChainAddress = createTransactionRequest.senderBlockChainAddress,
      recipientBlockChainAddress = createTransactionRequest.recipientBlockChainAddress,
      senderPublicKey = createTransactionRequest.senderPublicKey,
      value = createTransactionRequest.value,
      hexSignature = createTransactionRequest.signature
    )
    return CreateBlockTransactionResponse(message = "Success")
  }
}