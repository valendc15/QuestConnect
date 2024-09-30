package com.questconnect.favorites

data class NewsResponse(val appnews: AppNews)

data class AppNews(val appid: Long, val newsitems: List<NewsItem>)

data class NewsItem(
    val title: String,
    val url: String,
    val contents: String,
    val date: Long
)
