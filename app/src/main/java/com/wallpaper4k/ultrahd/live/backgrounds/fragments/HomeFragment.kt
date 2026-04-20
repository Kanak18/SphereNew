package com.wallpaper4k.ultrahd.live.backgrounds.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.wallpaper4k.ultrahd.live.backgrounds.R
import com.wallpaper4k.ultrahd.live.backgrounds.adapter.HomeCatAdapter
import com.wallpaper4k.ultrahd.live.backgrounds.databinding.FragmentHomeBinding
import com.wallpaper4k.ultrahd.live.backgrounds.model.SettingData
import com.wallpaper4k.ultrahd.live.backgrounds.utils.Const
import com.wallpaper4k.ultrahd.live.backgrounds.utils.Const.toArrayList
import com.wallpaper4k.ultrahd.live.backgrounds.utils.Global
import com.wallpaper4k.ultrahd.live.backgrounds.utils.RetrofitClient
import com.wallpaper4k.ultrahd.live.backgrounds.viewmodel.HomeViewModel
import com.wallpaper4k.ultrahd.live.backgrounds.viewmodel.MainViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.math.abs


class HomeFragment : BaseFragment(), Runnable {

    lateinit var binding: FragmentHomeBinding
    lateinit var viewModel: HomeViewModel
    lateinit var mainViewModel: MainViewModel
    lateinit var handler: Handler
    var reversed = false
    var scrollingPos = 0;
    var wallListMap = HashMap<Int, List<SettingData.WallpapersItem>>()
    lateinit var disposable: CompositeDisposable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        disposable = CompositeDisposable()

        initView()
        initListener()
        getHomeData()
        binding.model = viewModel




        return binding.root
    }


    var scrolledByUser = false
    var letestWall: MutableList<SettingData.WallpapersItem> = mutableListOf()

    private fun getHomeData() {
        disposable.add(
            RetrofitClient.service.fetchHomePageData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe {
                    binding.sihmmer.visibility = View.VISIBLE
                }
                .doOnTerminate {
                    binding.sihmmer.visibility = View.GONE
                }
                .doOnError { throwable -> }
                .subscribe { homedata, throwable ->
                    if (homedata != null && homedata.status!!) {

                        viewModel.featureImagesAdapter.updateData(homedata.featuredWallpapers!!)
                        val dotlist: MutableList<String> = ArrayList()
                        for (i in viewModel.featureImagesAdapter.mList.indices) {
                            dotlist.add(" ")
                        }

                        viewModel.featureDotsAdapter.updateData(dotlist)
                        letestWall = homedata.latestWallpapers!!.toMutableList()
                        wallListMap.put(0, letestWall.toMutableList())
                        viewModel.wallpaperAdapter.updateData(homedata.latestWallpapers as MutableList<SettingData.WallpapersItem>)

                        handler.postDelayed(Runnable {
                            binding.rvDots.minimumWidth = binding.rvDots.width
                        }, 2000)


                    } else {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        )
    }


    var linearLayoutManager: LinearLayoutManager? = null
    var isLoading = false
    private fun initListener() {


        binding.rvImageByCategory.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    if (viewModel.wallpaperAdapter.itemCount - 1 === linearLayoutManager?.findLastVisibleItemPosition() && !isLoading) {


                        fetchWallpaperByCat(true)

                    }
                }
            }
        })





        binding.rvFeatured.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    scrolledByUser = true;
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (scrolledByUser) {
                        handler.removeCallbacks(this@HomeFragment)
                        val layoutManager = binding.rvFeatured.layoutManager as? LinearLayoutManager
                        val itemPoition = layoutManager?.findFirstVisibleItemPosition() ?: RecyclerView.NO_POSITION

                        if (itemPoition != RecyclerView.NO_POSITION) {
                            scrollingPos = itemPoition
                            reversed = scrollingPos + 1 >= viewModel.featureImagesAdapter.itemCount
                            scrollToPos(true)
                        } else {
                            handler.postDelayed(this@HomeFragment, 3000)
                        }
                    }
                    scrolledByUser = false

                }


            }
        })


        binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            mainViewModel.cordinated_expandeed.value =
                abs(verticalOffset) - appBarLayout.totalScrollRange <= -50
        }

        viewModel.homeCatAdapter.onItemClick = object : HomeCatAdapter.OnItemClick {
            override fun onClick(item: SettingData.CategoriesItem) {

                catId = item.id!!
                if (wallListMap.contains(catId)) {

                    if (wallListMap.get(catId) != null && wallListMap.get(catId)?.isNotEmpty()!!) {
                        binding.tvNoData.visibility = View.GONE

                        if (catId == 0) {

                            var list = wallListMap.get(catId)!!
                            if (list.size > Const.PAGINATION_COUNT) {
                                viewModel.wallpaperAdapter.updateData(
                                    list.slice(0..Const.PAGINATION_COUNT - 1).toMutableList()
                                )
                            } else {
                                viewModel.wallpaperAdapter.updateData(
                                    list
                                )
                            }


                            return
                        }
                        viewModel.wallpaperAdapter.updateData(wallListMap.get(catId)!!)


                    }

                } else {


//                    if (catId == 0) {
//                        letestWall.clear()
//                        letestWall.addAll(viewModel.wallpaperAdapter.mList)
//                    }


                    noMoreData = false
                    disposable.clear()//if last category data is loading
                    fetchWallpaperByCat(false)
                }

            }
        }

    }

    var noMoreData = false

    private fun fetchWallpaperByCat(loadeMore: Boolean) {


        if (catId == 0) {

            var list = wallListMap.get(catId)!!
            if (viewModel.wallpaperAdapter.mList.size < list.size) {
                viewModel.wallpaperAdapter.loadMore(
                    list.slice(viewModel.wallpaperAdapter.mList.size..list.size - 1).toMutableList()
                )

            }

            return
        }
        if (!noMoreData) {

            binding.tvNoData.visibility = View.GONE

            if (!loadeMore) {
                viewModel.wallpaperAdapter.clear()
                binding.rvImageByCategory.adapter = null
                binding.rvImageByCategory.adapter = viewModel.wallpaperAdapter
                binding.sihmmer.visibility = View.VISIBLE
            } else {
                binding.progressLoadMore.visibility = View.VISIBLE
//                binding.viewBottom.requestFocus()
            }


            val hashMap = HashMap<String, Any>()

            hashMap[Const.ApiParams.start] = viewModel.wallpaperAdapter.itemCount
            hashMap[Const.ApiParams.limit] = Const.PAGINATION_COUNT
            if (catId != 0) {
                hashMap[Const.ApiParams.category_id] = catId
            }

            disposable.add(RetrofitClient.service.fetchWallpaperByCategory(
                hashMap
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnSubscribe {
                    isLoading = true
                }
                .doOnTerminate {
                    binding.sihmmer.visibility = View.GONE
                    binding.progressLoadMore.visibility = View.GONE
                    isLoading = false
                }
                .doOnError { throwable -> }
                .subscribe { wallByCat, throwable ->
                    if (wallByCat != null && wallByCat.status!! && wallByCat.data != null) {

                        if (wallByCat.data.isEmpty()) {
                            noMoreData = true
                            if (viewModel.wallpaperAdapter.itemCount == 0) {
                                binding.tvNoData.visibility = View.VISIBLE

                            }

                        } else {
                            if (viewModel.wallpaperAdapter.itemCount == 0) {
                                wallListMap.put(catId, wallByCat.data.toMutableList())
                                viewModel.wallpaperAdapter.updateData(wallByCat.data as MutableList<SettingData.WallpapersItem>)
                            } else {
                                viewModel.wallpaperAdapter.loadMore(wallByCat.data as MutableList<SettingData.WallpapersItem>)


                            }
                        }


                    } else {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.something_went_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }

    var catId = 0

    private fun initView() {

        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvFeatured)

        handler = Handler(Looper.getMainLooper())


        viewModel.homeCatAdapter.updateData(
            sessionManager.categories.toMutableList(),
            requireActivity().getString(R.string.new_)
        )
        binding.sihmmer.visibility = View.GONE
        binding.progressLoadMore.visibility = View.GONE

        binding.rvImageByCategory.itemAnimator = null
        viewModel.wallpaperAdapter.favList =
            Global.convertStringToLis(sessionManager.getStringValue(Const.Key.favourites))
                .toArrayList()

    }

    override fun run() {
        val itemCount = viewModel.featureImagesAdapter.itemCount
        if (itemCount <= 1) return

        if (reversed) {
            if (scrollingPos - 1 < 0) {
                scrollingPos = 1
                reversed = false
            } else {
                scrollingPos -= 1
            }
        } else {
            if (scrollingPos + 1 >= itemCount) {
                scrollingPos = itemCount - 2
                reversed = true
            } else {
                scrollingPos += 1
            }
        }

        scrollToPos(false)
    }

    private fun scrollToPos(fromUser: Boolean) {
        val itemCount = viewModel.featureImagesAdapter.itemCount
        if (itemCount == 0 || scrollingPos < 0 || scrollingPos >= itemCount) {
            if (!fromUser) {
                handler.postDelayed(this, 3000)
            }
            return
        }

        if (!fromUser) {
            binding.rvFeatured.smoothScrollToPosition(scrollingPos)
        }
        viewModel.featureDotsAdapter.scrollToPos(scrollingPos)
        binding.rvDots.scrollToPosition(scrollingPos)
        handler.postDelayed(this, 3000)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(this)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(this)

    }

    override fun onResume() {
        super.onResume()

        Handler(Looper.getMainLooper()).postDelayed(
            kotlinx.coroutines.Runnable { refreshFavList() },
            1000
        )
        if (viewModel.featureImagesAdapter.itemCount != 0) {
            handler.postDelayed(this, 3000)
        }


    }


    private fun refreshFavList() {

        Log.i("TAG", "refreshFavList: ")

        viewModel.wallpaperAdapter.refreshData(
            Global.convertStringToLis(
                sessionManager.getStringValue(
                    Const.Key.favourites
                )
            )
        )


    }


}