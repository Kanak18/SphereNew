package com.wallpaper4k.ultrahd.live.backgrounds.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wallpaper4k.ultrahd.live.backgrounds.adapter.CategoryAdapter


open class CategoryViewModel : BaseViewModel() {


    lateinit var categoryAdapter: CategoryAdapter
    var selected: MutableLiveData<Int> = MutableLiveData(0)


    public fun setSelectedType(i: Int) {
        selected.value = i
    }
}