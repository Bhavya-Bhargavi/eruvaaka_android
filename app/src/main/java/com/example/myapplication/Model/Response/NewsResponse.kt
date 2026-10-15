package com.example.myapplication.Model.Response

import android.text.Html
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("source") val source: String? = null,
    @SerializedName("last_build_date") val last_build_date: String? = null,
    @SerializedName("count") val count: Int? = null,
    @SerializedName("items") val items: List<NewsItem> = emptyList()
)

data class NewsItem(
    @SerializedName("title") val title: String = "",
    @SerializedName("link") val link: String = "",
    @SerializedName("published_at") val published_at: String = "",
    @SerializedName("creator") val creator: String = "",
    @SerializedName("categories") val categories: List<String> = emptyList(),
    @SerializedName("description") val description: String = "",
    @SerializedName("content") val content: String = "",
    @SerializedName("image_url") val image_url: String? = null,
    @SerializedName("thumbnail") val thumbnail: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("enclosure") val enclosure: String? = null,
    @SerializedName("featured_image") val featured_image: String? = null
) {
    fun getEffectiveImageUrl(): String? {
        val candidates = listOf(thumbnail, image_url, image, enclosure, featured_image)
        for (c in candidates) {
            if (!c.isNullOrBlank()) {
                val cleaned = unescapeUrl(c)
                if (cleaned.startsWith("http", ignoreCase = true)) return cleaned
            }
        }

        val contentImg = extractImgSrc(content)
        if (!contentImg.isNullOrBlank()) return contentImg

        val descImg = extractImgSrc(description)
        if (!descImg.isNullOrBlank()) return descImg

        return null
    }

    fun getCleanDescription(): String {
        val raw = if (description.isNotBlank()) description else content
        return Html.fromHtml(raw, Html.FROM_HTML_MODE_COMPACT)
            .toString()
            .replace(Regex("""\s+"""), " ")
            .trim()
    }

    private fun extractImgSrc(html: String?): String? {
        if (html.isNullOrBlank()) return null

        val patterns = listOf(
            Regex("""<img[^>]+src=["']([^"']+)["']""", RegexOption.IGNORE_CASE),
            Regex("""<img[^>]+data-src=["']([^"']+)["']""", RegexOption.IGNORE_CASE),
            Regex("""<img[^>]+data-orig-file=["']([^"']+)["']""", RegexOption.IGNORE_CASE),
            Regex("""https?://[^\s"'<>]+\.(?:jpg|jpeg|png|webp)[^\s"'<>]*""", RegexOption.IGNORE_CASE),
            Regex("""/wp-content/uploads/[^\s"'<>]+\.(?:jpg|jpeg|png|webp)[^\s"'<>]*""", RegexOption.IGNORE_CASE)
        )

        for (pattern in patterns) {
            val match = pattern.find(html)
            if (match != null) {
                val found = if (match.groupValues.size > 1) match.groupValues[1] else match.value
                val cleaned = unescapeUrl(found)
                if (cleaned.startsWith("http", ignoreCase = true)) {
                    return cleaned
                }
            }
        }
        return null
    }

    private fun unescapeUrl(url: String): String {
        var cleaned = Html.fromHtml(url, Html.FROM_HTML_MODE_COMPACT)
            .toString()
            .replace("&amp;", "&")
            .trim()
        if (cleaned.startsWith("//")) {
            cleaned = "https:$cleaned"
        } else if (cleaned.startsWith("/")) {
            cleaned = "https://eruvaaka.com$cleaned"
        }
        return cleaned
    }
}
