package com.wallpaper4k.live.hd.backgrounds.utils

import com.wallpaper4k.live.hd.backgrounds.model.FetchWallByCat
import com.wallpaper4k.live.hd.backgrounds.model.HomeData
import com.wallpaper4k.live.hd.backgrounds.model.SettingData
import io.reactivex.Single
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RetrofitService {

    @FormUrlEncoded
    @POST(Const.ApiParams.fetchSettings)
    fun fetchSettings(
        @FieldMap hashMap: HashMap<String, Any>
    ): Single<SettingData?>

    @POST(Const.ApiParams.fetchHomePageData)
    fun fetchHomePageData(): Single<HomeData?>

    @FormUrlEncoded
    @POST(Const.ApiParams.fetchWallpaperByCategory)
    fun fetchWallpaperByCategory(
        @FieldMap hashMap: HashMap<String, Any>
    ): Single<FetchWallByCat?>

    @FormUrlEncoded
    @POST(Const.ApiParams.fetchLikedWallpaper)
    fun fetchLikedWallpaper(
        @FieldMap hashMap: HashMap<String, Any>
    ): Single<FetchWallByCat?>

    @FormUrlEncoded
    @POST(Const.ApiParams.searchWallpaper)
    fun searchWallpaper(
        @FieldMap hashMap: HashMap<String, Any>
    ): Single<FetchWallByCat?>

}