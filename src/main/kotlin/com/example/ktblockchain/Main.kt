package com.example.ktblockchain

import com.example.ktblockchain.domain.model.BlockChain

fun main(args: Array<String>) {
  println("Hello blockchain!!")
  val blockchain = BlockChain()

  blockchain.addTransaction(
    senderBlockChainAddress = "A",
    recipientBlockChainAddress = "B",
    value = 1.0
  )
  blockchain.createBlock(nonce = 5)

  blockchain.addTransaction(
    senderBlockChainAddress = "C",
    recipientBlockChainAddress = "D",
    value = 2.0
  )
  blockchain.addTransaction(
    senderBlockChainAddress = "X",
    recipientBlockChainAddress = "Y",
    value = 3.5
  )
  blockchain.createBlock(nonce = 2)
  blockchain.print()
}