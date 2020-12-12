package com.example.ktblockchain.adapter.api

import com.example.ktblockchain.domain.model.blockchain.Block
import com.example.ktblockchain.usecase.BlockChainService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
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
    @RequestParam senderBlockChainAddress: String,
    @RequestParam recipientBlockChainAddress: String,
    @RequestParam value: Double,
    @RequestParam senderPublicKey: String,
    @RequestParam signature: String
  ): String {
    blockChainService.createTransaction(
      senderBlockChainAddress = senderBlockChainAddress,
      recipientBlockChainAddress = recipientBlockChainAddress,
      senderPublicKey = senderPublicKey,
      value = value,
      hexSignature = signature
    )
    return "{ \"message\" = \"success\" }"
  }
}