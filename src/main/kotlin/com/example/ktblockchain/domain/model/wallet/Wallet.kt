package com.example.ktblockchain.domain.model.wallet

import com.example.ktblockchain.utils.HashUtil
import com.example.ktblockchain.utils.encodeToBase58String
import org.apache.tomcat.util.buf.HexUtils
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom


class Wallet {

  val publicKey: PublicKey
  val privateKey: PrivateKey
  val blockChainAddress: String

  init {
    val keyGen = KeyPairGenerator.getInstance("EC")
    keyGen.initialize(256, SecureRandom.getInstance("SHA1PRNG"))

    val keyPair = keyGen.generateKeyPair()
    publicKey = keyPair.public
    privateKey = keyPair.private
    blockChainAddress = this.generateBlockChainAddress()
  }

  private fun generateBlockChainAddress(): String {
    val sha256BpkDigest = HashUtil.getSha256Hash(this.publicKey.toString())
    val ripemed160BpkHex = HexUtils.toHexString(HashUtil.getRipemd160Hash(sha256BpkDigest).toByteArray())
    val networkBitcoinPublicKeyBytes = HexUtils.fromHexString(HexUtils.toHexString(ByteArray(0x00)) + ripemed160BpkHex)
    val sha256Hex = HexUtils.toHexString(
      HashUtil.getSha256Hash(HashUtil.getSha256Hash(networkBitcoinPublicKeyBytes)).toByteArray()
    )
    val checkSum = sha256Hex.substring(0, 8)
    val addresshex = (networkBitcoinPublicKeyBytes.toString() + checkSum)
    return addresshex.toByteArray().encodeToBase58String()
  }
}