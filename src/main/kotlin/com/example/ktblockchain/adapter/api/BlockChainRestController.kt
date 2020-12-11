package com.example.ktblockchain.adapter.api

import com.example.ktblockchain.domain.model.blockchain.Block
import com.example.ktblockchain.usecase.BlockChainService
import org.springframework.web.bind.annotation.GetMapping
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
}