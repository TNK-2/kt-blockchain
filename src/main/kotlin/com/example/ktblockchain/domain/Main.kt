package com.example.ktblockchain.domain

import com.example.ktblockchain.domain.model.blockchain.BlockChain
import com.example.ktblockchain.domain.model.wallet.Transaction
import com.example.ktblockchain.domain.model.wallet.Wallet

fun __main(args: Array<String>) {
  val walletA = Wallet()
  val walletB = Wallet()
  val walletM = Wallet()
  val transaction = Transaction(
    senderPublicKey = walletA.publicKey,
    senderPrivateKey = walletA.privateKey,
    senderBlockChainAddress = walletA.blockChainAddress,
    recipientBlockChainAddress = walletB.blockChainAddress,
    value = 1.0
  )

  // Blockchain Node
  val blockChain = BlockChain(blockChainAddress = walletM.blockChainAddress)
  val isSuccess = blockChain.addTransaction(
    senderBlockChainAddress = walletA.blockChainAddress,
    recipientBlockChainAddress = walletB.blockChainAddress,
    value = transaction.value,
    senderPublicKey = walletA.publicKey,
    hexSignature = transaction.generateSignature()
  )
  println(isSuccess)
  println(blockChain.print())
  blockChain.mining()
  println(blockChain.print())
  println("A total amount : " + blockChain.calculateTotalAmount(walletA.blockChainAddress))
  println("B total amount : " + blockChain.calculateTotalAmount(walletB.blockChainAddress))
}

fun _main(args: Array<String>) {
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
  println("my total amount : " + blockchain.calculateTotalAmount("my_blockchain_address"))
  println("C total amount : " + blockchain.calculateTotalAmount("C"))
  println("D total amount : " + blockchain.calculateTotalAmount("D"))

  val wallet = Wallet()
  println("--------wallet---------")
  println("privatekey : " + wallet.privateKey)
  println("publickey : " + wallet.publicKey)
  println("blockchain addr : " + wallet.blockChainAddress)
  val transaction = Transaction(
    senderBlockChainAddress = wallet.blockChainAddress,
    recipientBlockChainAddress = "B",
    senderPrivateKey = wallet.privateKey,
    senderPublicKey = wallet.publicKey,
    value = 1.0
  )
  println("signature : " + transaction.generateSignature())
}