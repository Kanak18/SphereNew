package com.wallpaper4k.live.hd.backgrounds.utils

import androidx.annotation.Keep
import com.wallpaper4k.live.hd.backgrounds.BuildConfig

@Keep
object Const {
    const val BASE = "https://spere.hashtagwebhub.com/"
    const val APIKEY = "123"
    const val BASE_URL = BASE + "api/"
    const val ITEM_URL = BASE + "public/storage/"
    const val TERMS_URL = BASE + "termsOfUse"
    const val PRIVACY_URL = BASE + "privacyPolicy"
    const val NOTIFICATION_TOPIC = "sphere"
    const val PAGINATION_COUNT = 20
    fun <E> List<E>?.toArrayList(): ArrayList<E> {
        val list = ArrayList<E>()
        for (data in this ?: emptyList()) {
            list.add(data)
        }
        return list
    }

    object ApiParams {
        const val fetchSettings = "fetchSettings"
        const val fetchWallpaperByCategory = "fetchWallpaperByCategory"
        const val searchWallpaper = "searchWallpaper"
        const val fetchHomePageData = "fetchHomePageData"
        const val fetchLikedWallpaper = "fetchLikedWallpaper"
        const val apikey = "apikey"
        const val category_id = "category_id"
        const val access_type = "access_type"
        const val tags = "tags"
        const val start = "start"
        const val wallpaper_ids = "wallpaper_ids"
        const val limit = "limit"

        const val publisher_id = "publisher_id"

    }

    object Key {
        const val categories = "categories"
        const val is_old = "is_old"
        const val data = "data"
        const val subscriptions = "subscriptions"
        const val admob = "admob"
        const val is_notification = "is_notification"
        const val is_premium = "is_premium"
        const val favourites = "favourites"
        const val dataList = "dataList"
        const val wallpaper = "wallpaper"
        const val position = "position"
        const val LANGUAGE = "language"
        const val PREF_NAME = BuildConfig.APPLICATION_ID
    }
}