package com.example.ktblockchain

import com.example.ktblockchain.domain.BlockChain

fun main(args: Array<String>) {
  println("Hello blockchain!!")
  val blockchain = BlockChain()
  blockchain.createBlock(
    nonce = 5,
    previousHash = "hash 1"
  )
  blockchain.print()
  blockchain.createBlock(
    nonce = 4,
    previousHash = "hash 2"
  )
  blockchain.print()
  blockchain.createBlock(
    nonce = 3,
    previousHash = "hash 3"
  )
  blockchain.print()
}