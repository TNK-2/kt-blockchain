package com.example.ktblockchain.domain.model.blockchain

import com.example.ktblockchain.config.AppConf
import com.example.ktblockchain.utils.HashUtil
import com.example.ktblockchain.utils.KtLog
import org.apache.tomcat.util.buf.HexUtils
import java.security.PublicKey
import java.security.Signature

class BlockChain(
  private var transactionPool: MutableList<Transaction> = mutableListOf(),
  val chain: MutableList<Block> = mutableListOf(),
  private val blockChainAddress: String,
  private val port: String = AppConf.PORT
) {

  init {
    this.createBlock(0, "init hash")
  }

  companion object {
    private val logger = KtLog.logger
  }

  private fun createBlock(
    nonce: Int,
    previousHash: String = this.getLastBlockHash(),
    timestamp: Long = System.currentTimeMillis()
  ) {
    val block = Block(
      timestamp = timestamp,
      nonce = nonce,
      previousHash = previousHash,
      transactions = this.transactionPool
    )
    this.chain.add(block)
    this.transactionPool = mutableListOf()
  }

  fun getBlockHash(block: Block): String =
    HashUtil.getSha256Hash(block.toString())

  fun getLastBlockHash(): String =
    this.getBlockHash(this.chain.last())

  fun addTransaction(
    senderBlockChainAddress: String,
    recipientBlockChainAddress: String,
    value: Double,
    senderPublicKey: PublicKey? = null,
    hexSignature: String? = null
  ): Boolean {
//    if (this.calculateTotalAmount(senderBlockChainAddress) < value) {
//      logger.error("残高が足りません...")
//      return false
//    }

    val transaction = Transaction(
      senderBlockChainAddress = senderBlockChainAddress,
      recipientBlockChainAddress = recipientBlockChainAddress,
      value = value
    )
    if (senderBlockChainAddress == AppConf.MINING_SENDER) {
      transactionPool.add(transaction)
      logger.info("マイニングによるトランザクションが追加されました")
      return true
    }
    if (this.verifyTransactionSignature(
        senderPublicKey = senderPublicKey!!,
        hexSignature = hexSignature!!,
        transaction = transaction)
    ) {
      transactionPool.add(transaction)
      logger.info("送金によるトランザクションが追加されました")
      return true
    }
    logger.error("トランザクションが失敗しました...")
    return false
  }

  fun calculateTotalAmount(blockChainAddress: String): Double {
    var totalAmount = 0.0
    this.chain.forEach { block ->
      block.transactions.forEach { transaction ->
        if (blockChainAddress == transaction.recipientBlockChainAddress) {
          totalAmount += transaction.value
        }
        if (blockChainAddress == transaction.senderBlockChainAddress) {
          totalAmount -= transaction.value
        }
      }
    }
    return totalAmount
  }

  private fun verifyTransactionSignature(
    senderPublicKey: PublicKey,
    hexSignature: String,
    transaction: Transaction
  ): Boolean {
    val message = HashUtil.getSha256Hash(transaction.toString())

    val sig = Signature.getInstance(AppConf.SIGNATURE_ALGO)
    sig.initVerify(senderPublicKey)
    sig.update(message.toByteArray())
    return sig.verify(HexUtils.fromHexString(hexSignature))
  }

  private fun validProof(
    transactions: List<Transaction>,
    previousHash: String,
    nonce: Int,
    difficulty: Int = AppConf.MINING_DIFFICULTY,
    timestamp: Long
  ): Boolean {
    val guessHash = this.getBlockHash(
      block = Block(
        timestamp = timestamp,
        previousHash = previousHash,
        nonce = nonce,
        transactions = transactions
      )
    )
    return guessHash.substring(0, difficulty) == "0".repeat(difficulty)
  }

  private fun proofOfWork(): Int {
    val transactions = this.transactionPool.toList()
    val previousHash = this.getLastBlockHash()
    val timestamp = System.currentTimeMillis()
    var nonce = 0;
    while (!this.validProof(transactions = transactions, previousHash = previousHash, nonce = nonce, timestamp = timestamp)) {
      nonce++
    }
    return nonce
  }

  fun mining(): Boolean {
    this.addTransaction(
      senderBlockChainAddress = AppConf.MINING_SENDER,
      recipientBlockChainAddress = this.blockChainAddress,
      value = AppConf.MINING_REWARD
    )
    val nonce = this.proofOfWork()
    this.createBlock(nonce = nonce)
    logger.info("{ \"action\" : \"mining\", \"status\" = \"success\" }")
    return true
  }

  fun print(chains: List<Block> = this.chain) {
    println("**Current Transaction*****************")
    transactionPool.forEachIndexed { i, transaction ->
      println("------print transaction : $i ------")
      println("transaction : $transaction")
    }
    println()
    println("**Blocks Info*************************")
    chains.forEachIndexed { i, block ->
      println("------print chain : $i ------")
      println("timestamp : " + block.timestamp)
      println("nonce : " + block.nonce)
      println("previous hash : " + block.previousHash)
      println("transactions : " + block.transactions)
      println("block hash : " + this.getBlockHash(block))
    }
    println()
  }
}