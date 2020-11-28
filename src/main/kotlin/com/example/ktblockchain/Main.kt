package com.example.ktblockchain

import com.example.ktblockchain.domain.model.BlockChain

fun main(args: Array<String>) {
  println("Hello blockchain!!")
  val blockchain = BlockChain()


  blockchain.createBlock(nonce = 5)
  blockchain.print()

  blockchain.createBlock(nonce = 2)
  blockchain.print()
}