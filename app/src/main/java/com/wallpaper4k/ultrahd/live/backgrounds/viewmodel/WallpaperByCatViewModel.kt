package com.wallpaper4k.ultrahd.live.backgrounds.viewmodel

import com.wallpaper4k.ultrahd.live.backgrounds.adapter.WallpaperAdapter
import com.wallpaper4k.ultrahd.live.backgrounds.model.SettingData


open class WallpaperByCatViewModel : BaseViewModel() {

    var category: SettingData.CategoriesItem? = null

    var wallpaperAdapter = WallpaperAdapter()


}