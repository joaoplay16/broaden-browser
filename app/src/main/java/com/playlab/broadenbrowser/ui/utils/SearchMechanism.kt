package com.playlab.broadenbrowser.ui.utils

enum class SearchMechanism {
    GOOGLE, BING, YAHOO, DUCK_DUCK_GO, BAIDU
}

val SearchMechanisms = mapOf(
    SearchMechanism.GOOGLE to "Google",
    SearchMechanism.BING to "Bing",
    SearchMechanism.YAHOO to "Yahoo",
    SearchMechanism.DUCK_DUCK_GO to "Duck Duck Go",
    SearchMechanism.BAIDU to "Baidu",
)