package com.wallpaper4k.live.hd.backgrounds.viewmodel

import com.wallpaper4k.live.hd.backgrounds.adapter.FeatureDotsAdapter
import com.wallpaper4k.live.hd.backgrounds.adapter.FeatureImagesAdapter
import com.wallpaper4k.live.hd.backgrounds.adapter.HomeCatAdapter
import com.wallpaper4k.live.hd.backgrounds.adapter.WallpaperAdapter


open class HomeViewModel : BaseViewModel() {


    var featureImagesAdapter = FeatureImagesAdapter()
    var featureDotsAdapter = FeatureDotsAdapter()
    var homeCatAdapter = HomeCatAdapter()
    var wallpaperAdapter = WallpaperAdapter()

}