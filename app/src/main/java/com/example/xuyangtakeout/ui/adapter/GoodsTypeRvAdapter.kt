package com.example.xuyangtakeout.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.model.bean.GoodsTypeInfo
import com.example.xuyangtakeout.ui.fragment.GoodsFragment
import org.jetbrains.anko.find


/**
 * Created by Mloong
 * on 2019/5/20 22:05.
 */
class GoodsTypeRvAdapter(val context: Context, val goodsFragment: GoodsFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var goodsTypeList: List<GoodsTypeInfo> = listOf()
    fun setDatas(list: List<GoodsTypeInfo>) {
        this.goodsTypeList = list
        notifyDataSetChanged()

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val goodsTypeItemHolder = holder as GoodsTypeItemHolder
        goodsTypeItemHolder.bindData(goodsTypeList.get(position), position)
    }

    override fun getItemCount(): Int {
        return goodsTypeList.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewTyPe: Int): RecyclerView.ViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.item_type, parent, false)
        return GoodsTypeItemHolder(itemView)

    }


    var selectPosition = 0 //选择的位置

    inner class GoodsTypeItemHolder(val item: View) : RecyclerView.ViewHolder(item) {

        val tvType: TextView
        val tvRedDotCount: TextView
        var mPosition: Int = 0

        lateinit var goodsTypeInfo: GoodsTypeInfo

        init {
            tvType = item.find<TextView>(R.id.type)
            tvRedDotCount = item.find<TextView>(R.id.tvRedDotCount)


            item.setOnClickListener {
                selectPosition = mPosition
                notifyDataSetChanged()

                //stpe 2:右侧跳转新点击类型的第一个商品

                val typeId = goodsTypeInfo.id
                //遍历所有商品。找到此position

                val position = goodsFragment.goodsFragmentPresenter.getGoodsPositionByTypeId(typeId)
                goodsFragment.slhlv.setSelection(position)
            }

        }


        fun bindData(goodsTypeInfo: GoodsTypeInfo, position: Int) {
            mPosition = position

            this.goodsTypeInfo = goodsTypeInfo
            //选中的为白底加粗黑字，未选中是灰色背景，普通字体
            if (position == selectPosition) {
                //选中
                item.setBackgroundColor(Color.WHITE)
                tvType.setTextColor(Color.BLACK)
                tvType.setTypeface(Typeface.DEFAULT_BOLD)

            } else {
                //未选中
                item.setBackgroundColor(Color.parseColor("#b9dedcdc"))
                tvType.setTextColor(Color.GRAY)
                tvType.setTypeface(Typeface.DEFAULT)


            }



            tvType.text = goodsTypeInfo.name

            tvRedDotCount.text = goodsTypeInfo.redDOTCount.toString()

            if (goodsTypeInfo.redDOTCount > 0) {
                tvRedDotCount.visibility = View.VISIBLE
            } else {
                tvRedDotCount.visibility = View.GONE
            }


        }

    }


}