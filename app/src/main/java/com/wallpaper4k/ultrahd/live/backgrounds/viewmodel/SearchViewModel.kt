package com.wallpaper4k.ultrahd.live.backgrounds.viewmodel

import androidx.lifecycle.MediatorLiveData
import com.wallpaper4k.ultrahd.live.backgrounds.adapter.WallpaperAdapter


open class SearchViewModel : BaseViewModel() {


    var wallpaperAdapter = WallpaperAdapter()

    var searchWord = ""

    var filterType = MediatorLiveData(2) //2==all, 0=primium,1=locked

}