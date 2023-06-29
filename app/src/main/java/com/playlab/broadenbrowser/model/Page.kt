package com.playlab.broadenbrowser.model

interface Page {
    val id: Int
    val title: String
    val url: String
    val timestamp: Long
}