package com.example.ktblockchain.adapter.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WalletController {

  @GetMapping("/")
  fun index():String {
    return "index"
  }
}