package com.example.xuyangtakeout.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.model.bean.GoodsInfo
import com.example.xuyangtakeout.ui.activity.BusinessActivity
import com.example.xuyangtakeout.ui.fragment.GoodsFragment
import com.example.xuyangtakeout.utils.Constants
import com.example.xuyangtakeout.utils.PriceFormater
import com.example.xuyangtakeout.utils.TakeoutApp

/**
 * Created by Mloong
 * on 2019/5/22 15:06
 */

class CartRvAdapter(val context: Context)  :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val goodsFragment :GoodsFragment
    init {
        goodsFragment= (context as BusinessActivity).fragments.get(0) as GoodsFragment
    }
    var cartList:ArrayList<GoodsInfo> = arrayListOf()

    fun setCart(cartList: ArrayList<GoodsInfo>){
        this.cartList = cartList
        notifyDataSetChanged()

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as CartItemHolder).binData(cartList.get(position))

    }

    override fun getItemCount(): Int {
       return  cartList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false)
        return CartItemHolder(itemView)
    }

    inner class  CartItemHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            var  isAdd =false
            when(v?.id){
                R.id.ib_add ->{

                    isAdd = true
                    doAddOperation()
                }
                R.id.ib_minus ->doMinusOperation()
            }

            processRedDotCount(isAdd) //红点处理
            (context as BusinessActivity).updateCarUi() //下方购物栏刷新


        }

        private fun processRedDotCount(isAdd: Boolean) {
            //找到此商品属于的类别
            val TypeId = goodsInfo.typeId

            //找到此类别在左侧的位置
            val typePosition = goodsFragment.goodsFragmentPresenter.getTypePositionByTypeId(TypeId)

            //最后找出tvRedDotCount

            val goodsTypeInfo = goodsFragment.goodsFragmentPresenter.goodsTypeList.get(typePosition)
            var redDotCount = goodsTypeInfo.redDOTCount
            if (isAdd){
                redDotCount++

            }else{
                redDotCount--
            }
            goodsTypeInfo.redDOTCount = redDotCount

            //刷新左侧列表
            goodsFragment.goodsTypeAdapter.notifyDataSetChanged()


        }
        private fun doMinusOperation() {

            //数据层count
            var  count = goodsInfo.count

            if (count ==1){
                //数量为1再减需要移除
                cartList.remove(goodsInfo)

                //最后一个类别移除后， 关闭购物车
                if(cartList.size ==0){
                    (context as BusinessActivity).showOrHideCart()
                }

                //删除缓存
                TakeoutApp.sInstance.deleteCacheSelectedInfo(goodsInfo.id)
            } else{

                //更新缓存
                TakeoutApp.sInstance.updateCacheSelectedInfo(goodsInfo.id, Constants.MINUS)


            }

            count--


            goodsInfo.count =count



            //购物车内部数量与价格

            notifyDataSetChanged()

            //左侧红点

            //右侧列表
            goodsFragment.goodsAdapter.notifyDataSetChanged()


        }

        private fun doAddOperation() {
            //数据层count
            var  count = goodsInfo.count
            //更新缓存
            TakeoutApp.sInstance.updateCacheSelectedInfo(goodsInfo.id, Constants.ADD)

            count++
            goodsInfo.count =count

            //购物车内部数量与价格

            notifyDataSetChanged()

            //左侧红点

            //右侧列表
            goodsFragment.goodsAdapter.notifyDataSetChanged()


            //下方购物栏刷新

        }

        fun binData(goodsInfo: GoodsInfo) {

            this.goodsInfo = goodsInfo
            tvName.text = goodsInfo.name
            tvAllPrice.text = PriceFormater.format(goodsInfo.newPrice.toFloat() * goodsInfo.count)

            tvCount.text = goodsInfo.count.toString()

        }

        val tvName:TextView
        val tvAllPrice:TextView
        val tvCount:TextView
        val ibAdd :ImageButton
        val ibMinus :ImageButton
        lateinit var goodsInfo: GoodsInfo

        init {

            tvName = itemView.findViewById(R.id.tv_name)  as TextView

            tvAllPrice = itemView.findViewById(R.id.tv_type_all_price)  as TextView
            tvCount = itemView.findViewById(R.id.tv_count)  as TextView

            ibAdd = itemView.findViewById(R.id.ib_add) as ImageButton
            ibMinus = itemView.findViewById(R.id.ib_minus) as ImageButton
            ibAdd.setOnClickListener(this)
            ibMinus.setOnClickListener(this)
        }

    }






}