package com.wallpaper4k.live.hd.backgrounds.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.wallpaper4k.live.hd.backgrounds.R
import com.wallpaper4k.live.hd.backgrounds.activity.ViewWallpapersActivity
import com.wallpaper4k.live.hd.backgrounds.databinding.ItemHomeImageByCatBinding
import com.wallpaper4k.live.hd.backgrounds.model.SettingData
import com.wallpaper4k.live.hd.backgrounds.utils.Const
import com.wallpaper4k.live.hd.backgrounds.utils.Global
import com.wallpaper4k.live.hd.backgrounds.utils.SessionManager

class WallpaperAdapter() : RecyclerView.Adapter<WallpaperAdapter.ItemHolder>() {

    var mList: MutableList<SettingData.WallpapersItem> = mutableListOf()
    var favList: ArrayList<Int> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_image_by_cat, parent, false)
        return ItemHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItemHolder, position: Int
    ) {
        holder.setModal(position)


    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun updateData(list: List<SettingData.WallpapersItem>) {

        mList.clear()

        mList = list.toMutableList()

        if (mList.isEmpty()) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeChanged(0, mList.size)
        }

    }

    fun loadMore(data: MutableList<SettingData.WallpapersItem>) {
        for (i in data.indices) {
            mList.add(data[i])
            notifyItemInserted(mList.size - 1)
        }
    }

    fun clear() {
        mList.clear()
        notifyDataSetChanged()
    }

    fun refreshData(newList: List<Int>) {

        val oldListOfFav = ArrayList<Int>()
        oldListOfFav.addAll(favList)
        favList = ArrayList()
        favList.addAll(newList)
        Log.i("TAG", "refreshData: " + oldListOfFav)
        Log.i("TAG", "refreshData: fav list " + favList)

        for (i in 0..<mList.size) {
            if (oldListOfFav.contains(mList[i].id) || favList.contains(mList[i].id)) {
                Log.i("TAG", "refreshData: " + mList[i].id)
                notifyItemChanged(i)

            }

        }
    }

    inner class ItemHolder
        (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemHomeImageByCatBinding
        var sessionManager: SessionManager

        init {
            binding = DataBindingUtil.bind(itemView)!!
            sessionManager = SessionManager(itemView.context)

        }

        fun setModal(position: Int) {


            val item = mList[position]


            binding.isFav = favList.contains(item.id)





            binding.icFavYes.setOnClickListener(View.OnClickListener {

                if (favList.contains(item.id)) {
                    favList.remove(item.id!!)
                } else {
                    favList.add(item.id!!)
                }
                sessionManager.saveStringValue(
                    Const.Key.favourites,
                    Global.listOfIntegerToString(favList)
                )
                notifyItemChanged(position)

            })


            binding.root.setOnClickListener(View.OnClickListener {

                var intent = Intent(itemView.context, ViewWallpapersActivity::class.java)

                intent.putExtra(Const.Key.dataList, Gson().toJson(mList))
                intent.putExtra(Const.Key.position, position)
                itemView.context.startActivity(intent)

            })


            binding.model = item
        }
    }
}