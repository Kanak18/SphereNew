package com.wallpaper4k.live.hd.backgrounds.viewmodel

import androidx.lifecycle.MutableLiveData
import com.wallpaper4k.live.hd.backgrounds.adapter.CategoryAdapter


open class CategoryViewModel : BaseViewModel() {


    lateinit var categoryAdapter: CategoryAdapter
    var selected: MutableLiveData<Int> = MutableLiveData(0)


    public fun setSelectedType(i: Int) {
        selected.value = i
    }
}