package com.wallpaper4k.ultrahd.live.backgrounds.viewmodel

import com.wallpaper4k.ultrahd.live.backgrounds.adapter.FeatureDotsAdapter
import com.wallpaper4k.ultrahd.live.backgrounds.adapter.FeatureImagesAdapter
import com.wallpaper4k.ultrahd.live.backgrounds.adapter.HomeCatAdapter
import com.wallpaper4k.ultrahd.live.backgrounds.adapter.WallpaperAdapter


open class HomeViewModel : BaseViewModel() {


    var featureImagesAdapter = FeatureImagesAdapter()
    var featureDotsAdapter = FeatureDotsAdapter()
    var homeCatAdapter = HomeCatAdapter()
    var wallpaperAdapter = WallpaperAdapter()

}