package com.example.ktblockchain.domain.model.wallet

import com.example.ktblockchain.domain.model.blockchain.Transaction
import com.example.ktblockchain.utils.HashUtil
import com.example.ktblockchain.utils.decodeBase58
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

class Transaction(
  val senderPrivateKey: PrivateKey,
  val senderPublicKey: PublicKey,
  val senderBlockChainAddress: String,
  val recipientBlockChainAddress: String,
  val value: Double
) {

  fun generateSignature(): String {
    val transaction = Transaction(
      senderBlockChainAddress = this.senderBlockChainAddress,
      recipientBlockChainAddress = this.recipientBlockChainAddress,
      value = this.value
    )
    val message = HashUtil.getSha256Hash(transaction.toString())
    val signature = Signature.getInstance("SHA256withECDSA")
    signature.initSign(this.senderPrivateKey)
    signature.update(message.toByteArray())
    return signature.sign().toString()
  }
}