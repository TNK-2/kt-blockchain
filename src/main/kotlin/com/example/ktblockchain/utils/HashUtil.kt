package com.example.ktblockchain.utils

import com.example.ktblockchain.config.AppConf
import java.security.MessageDigest

object HashUtil {

  fun getSha256Hash(str: String) =
    MessageDigest.getInstance(AppConf.MESSAGE_DIGEST_ALGO)
      .digest(str.toByteArray())
      .joinToString(separator = "") {
        "%02x".format(it)
      }
}