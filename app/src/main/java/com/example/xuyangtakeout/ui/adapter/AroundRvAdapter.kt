package com.example.xuyangtakeout.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amap.api.services.core.PoiItem
import com.example.xuyangtakeout.R

/**
 * Created by Mloong
 * on 2019/5/27 18:53
 */
class AroundRvAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var poiList = arrayListOf<PoiItem>()

    fun setPoiItemList(list : ArrayList<PoiItem>){
        this.poiList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return poiList.size
    }
    override fun onCreateViewHolder(parent: ViewGroup,viewGroup: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_around_address,parent,false)
        return AroundItemHolder(itemView)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AroundItemHolder).bindData(poiList.get(position))
    }

    inner class AroundItemHolder(itemView : View) : RecyclerView.ViewHolder (itemView){
        val tvTitle: TextView
        val tvAddress: TextView
        init {
            tvTitle= itemView.findViewById(R.id.tv_title) as TextView
            tvAddress= itemView.findViewById(R.id.tv_address) as TextView
            itemView.setOnClickListener {
                val intent = Intent()
                intent.putExtra("title",tvTitle.text)
                intent.putExtra("address",tvAddress.text)
                (context as Activity).setResult(200,intent)
                (context as Activity).finish()
            }
        }

        fun bindData(poiItem: PoiItem) {
            tvTitle.text = poiItem.title
            tvAddress.text = poiItem.snippet //摘要
        }

    }

}