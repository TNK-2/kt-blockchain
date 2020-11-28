package com.example.ktblockchain

import com.example.ktblockchain.domain.model.Block
import com.example.ktblockchain.domain.model.BlockChain
import com.example.ktblockchain.domain.model.Transaction

fun main(args: Array<String>) {
  println("Hello blockchain!!")
  val blockchain = BlockChain()

  blockchain.addTransaction(
    senderBlockChainAddress = "A",
    recipientBlockChainAddress = "B",
    value = 1.0
  )
  blockchain.createBlock(nonce = blockchain.proofOfWork())

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
  blockchain.createBlock(nonce = blockchain.proofOfWork())
  blockchain.print()
}