package com.example.ktblockchain.utils

import com.example.ktblockchain.config.AppConf
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

//fun main(args: Array<String>) {
//  println(HostSearch.findNeighbours(
//    myHost = "192.168.3.2",
//    myPort = 5000,
//    startIpRange = 0,
//    endIpRange = 3,
//    startPort = 5000,
//    endPort = 5003)
//  )
//}

object HostSearch {

  private val REGEX_IP = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.)(\\d{1,3}\$)".toRegex()

  fun isHostExist(
    targetIp: String,
    targetPort: Int
  ): Boolean {
    val socket = Socket()
    return try {
      socket.connect(InetSocketAddress(targetIp, targetPort), 1000)
      KtLog.logger.info("疎通が確認できました。疎通先... %s%s%s".format(targetIp, ":", targetPort))
      true
    } catch (e: IOException) {
      // e.printStackTrace()
      KtLog.logger.error("通信に失敗しました。%s%s%sは存在しないか、すでに接続済みです。".format(targetIp, ":", targetPort))
      false
    } catch (e: Exception) {
      e.printStackTrace()
      KtLog.logger.error("予期せぬ不具合で通信に失敗しました。")
      false
    } finally {
      socket.close()
    }
  }

  fun findNeighbours(
    myHost: String,
    myPort: Int,
    ipRange: IntRange,
    portRange: IntRange
  ): List<String> {
    val matchResult = REGEX_IP.find(myHost)
      ?: throw IllegalArgumentException("不正なIPアドレス・ポートです")
    val (prefixHost, lastIp) = matchResult.destructured

    val neighbourList = mutableListOf<String>()
    for (guessPort in portRange) {
      for (ipRange in ipRange) {
        val guessHost = prefixHost + (lastIp.toInt() + ipRange)
        val guessAddress = "$guessHost:$guessPort"
        if (this.isHostExist(guessHost, guessPort) && guessAddress != "$myHost:$myPort") {
          neighbourList.add(guessAddress)
        }
      }
    }
    return neighbourList
  }

  fun getMyHost(): String {
    val hostName = InetAddress.getLocalHost().hostAddress
      ?: AppConf.DEFAULT_HOST_NAME
    KtLog.logger.info("このサーバーのホスト名は「%s」です。".format(hostName))
    return hostName
  }

}