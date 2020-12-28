package com.example.ktblockchain.domain.model.blockchain

import com.example.ktblockchain.config.AppConf
import com.example.ktblockchain.utils.HashUtil
import com.example.ktblockchain.utils.HostSearch
import com.example.ktblockchain.utils.KtLog
import org.apache.tomcat.util.buf.HexUtils
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.security.PublicKey
import java.security.Signature
import kotlin.concurrent.thread

class BlockChain(
  var transactionPool: MutableList<Transaction> = mutableListOf(),
  var chain: MutableList<Block> = mutableListOf(),
  val blockChainAddress: String,
  private val port: Int = AppConf.PORT,
  var neighbours: List<String> = mutableListOf()
) {

  companion object {
    private val logger = KtLog.logger
  }

  fun syncNeighbours() {
    thread {
      while (true) {
        try {
          // logger.info("近隣ノードを検索します。")
          this.neighbours = HostSearch.findNeighbours(
            myHost = HostSearch.getMyHost(),
            myPort = this.port,
            searchIpRange = AppConf.NEIGHBOURS_IP_RANGE,
            portRange = AppConf.BLOCKCHAIN_PORT_RANGE
          )
          // logger.info(("近隣ノードが追加されました%s").format(this.neighbours))
        } catch (e: Exception) {
          e.printStackTrace()
          logger.error("近隣ノード検索中にエラーが発生しました。")
        } finally {
          Thread.sleep(AppConf.BLOCKCHAIN_MINING_TIME_SEC.toLong())
        }
      }
    }
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
      if (this.calculateTotalAmount(senderBlockChainAddress) < value) {
        throw IllegalArgumentException("残高が足りません...")
        return false
      }
      transactionPool.add(transaction)
      logger.info("送金によるトランザクションが追加されました")
      return true
    }

    throw IllegalStateException("トランザクションが失敗しました...")
  }

  fun calculateTotalAmount(blockChainAddress: String): Double {
    var totalAmount = 0.0
    this.chain.forEach { block ->
      block.transactions.forEach { transaction ->
        // KtLog.logger.info("%s : %s : %s".format(blockChainAddress, transaction.senderBlockChainAddress, transaction.recipientBlockChainAddress))
        if (blockChainAddress == transaction.recipientBlockChainAddress) {
          totalAmount += transaction.value
        }
        if (blockChainAddress == transaction.senderBlockChainAddress) {
          totalAmount -= transaction.value
        }
      }
    }
    KtLog.logger.info("送信者「%s」の 所持金額合計 : %s".format(blockChainAddress, totalAmount))
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

  fun proofOfWork(timestamp: Long): Int {
    val transactions = this.transactionPool.toList()
    val previousHash = this.getLastBlockHash()
    var nonce = 0;
    while (!this.validProof(transactions = transactions, previousHash = previousHash, nonce = nonce, timestamp = timestamp)) {
      nonce++
    }
    return nonce
  }

  /**
   * ブロックチェーンの検証。
   * 各ブロックのハッシュをチェックしてブロックが適正か判断する。
   */
  fun validChain(chain: List<Block>): Boolean {
    var preBlock = chain.first()
    for (i in 1 until chain.size) {
      val block = chain[i]
      if (block.previousHash != this.getBlockHash(preBlock)) {
        KtLog.logger.warn("前後ブロックのハッシュ整合性不正!! ... " + block.previousHash + " : " + this.getBlockHash(preBlock))
        return false
      }
      if (!this.validProof(
          transactions = block.transactions,
          previousHash = block.previousHash,
          nonce = block.nonce,
          timestamp = block.timestamp)) {
        KtLog.logger.warn("ブロックのハッシュ形式不正")
        return false
      }
      preBlock = block
    }
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