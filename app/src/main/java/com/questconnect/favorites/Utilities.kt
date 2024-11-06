package com.questconnect.favorites

fun removeImageTags(htmlContent: String): String {
    val imageTagPattern = "(<img[^>]*>|\\{STEAM_CLAN_IMAGE\\}|[^\\s\"']+\\.(png|jpeg|jpg))".toRegex()
    return htmlContent.replace(imageTagPattern, "")
}