package com.wallpaper4k.live.hd.backgrounds.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.wallpaper4k.live.hd.backgrounds.R
import com.wallpaper4k.live.hd.backgrounds.adapter.CategoryAdapter
import com.wallpaper4k.live.hd.backgrounds.databinding.FragmentCategoryBinding
import com.wallpaper4k.live.hd.backgrounds.model.SettingData
import com.wallpaper4k.live.hd.backgrounds.viewmodel.CategoryViewModel

class CategoryFragment : BaseFragment() {

    lateinit var binding: FragmentCategoryBinding
    lateinit var viewModel: CategoryViewModel
    lateinit var allCat: List<SettingData.CategoriesItem>
    lateinit var liveCat: List<SettingData.CategoriesItem>
    lateinit var simpleCat: List<SettingData.CategoriesItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        viewModel = ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
        viewModel.categoryAdapter = CategoryAdapter(requireActivity())


        initView()
        initListeners()
        initObserver()


        binding.model = viewModel




        return binding.root
    }

    private fun initObserver() {
        viewModel.selected.observe(viewLifecycleOwner, Observer {
            when (it) {
                0 -> viewModel.categoryAdapter.updateData(allCat)
                1 -> viewModel.categoryAdapter.updateData(simpleCat)
                2 -> viewModel.categoryAdapter.updateData(liveCat)
            }
            binding.nestedScrollView.scrollTo(0, 0)


            binding.model = viewModel
        })
    }

    private fun initListeners() {


    }

    private fun initView() {


        allCat = sessionManager.categories
        simpleCat = sessionManager.categories.filter { categoriesItem -> categoriesItem.type == 0 }
        liveCat = sessionManager.categories.filter { categoriesItem -> categoriesItem.type == 1 }


    }


}