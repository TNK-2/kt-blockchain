package com.example.ktblockchain

import com.example.ktblockchain.domain.model.BlockChain

fun main(args: Array<String>) {
  println("Hello blockchain!!")
  val blockchain = BlockChain(
    blockChainAddress = "my_blockchain_address"
  )

  blockchain.addTransaction(
    senderBlockChainAddress = "A",
    recipientBlockChainAddress = "B",
    value = 1.0
  )
  blockchain.mining()

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
  blockchain.mining()

  blockchain.print()
}