package com.wallpaper4k.live.hd.backgrounds.viewmodel

import com.wallpaper4k.live.hd.backgrounds.adapter.WallpaperAdapter
import com.wallpaper4k.live.hd.backgrounds.model.SettingData


open class WallpaperByCatViewModel : BaseViewModel() {

    var category: SettingData.CategoriesItem? = null

    var wallpaperAdapter = WallpaperAdapter()


}