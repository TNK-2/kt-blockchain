package com.example.ktblockchain.utils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object ObjectSerializer {

  fun serialize(obj: Any): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
    return try {
      objectOutputStream.writeObject(obj)
      byteArrayOutputStream.toByteArray().encodeToBase58String()
    } finally {
      byteArrayOutputStream.close()
      objectOutputStream.close()
    }
  }

  fun deSerialize(txt: String): Any {
    val decoded = txt.decodeBase58()
    val objectInputStream = ObjectInputStream(ByteArrayInputStream(decoded))
    return objectInputStream.readObject()
  }
}