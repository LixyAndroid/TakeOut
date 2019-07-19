package com.example.xuyangtakeout.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.model.bean.CacheSelectedInfo
import com.example.xuyangtakeout.ui.activity.BusinessActivity
import com.example.xuyangtakeout.ui.fragment.GoodsFragment
import com.example.xuyangtakeout.utils.Constants
import com.example.xuyangtakeout.utils.PriceFormater
import com.example.xuyangtakeout.utils.TakeoutApp
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter
import com.example.xuyangtakeout.model.bean.GoodsInfo as goodsInfo

/**
 * Created by Mloong
 * on 2019/5/20 22:52
 */

class GoodsAdapter(val  context: Context,val goodsFragment: GoodsFragment) :BaseAdapter(),StickyListHeadersAdapter{
    companion object val DURATION:Long = 1000

    inner  class GoodsItemHolder(itemView: View) : View.OnClickListener {
        lateinit  var goodsInfo: goodsInfo

        override fun onClick(v: View?) {
            var  isAdd:Boolean = false

            when(v?.id){
                R.id.ib_add ->{
                    isAdd = true
                    doAppOperation()}
                R.id.ib_minus ->doMinusOperation()
            }

            processRedDotCount(isAdd)

            (goodsFragment.activity as  BusinessActivity).updateCarUi()
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
            //改变count值
            var count = goodsInfo.count
            if (count ==1){
                //最后一次点击加号执行动画级

                val hideAnimationSet:AnimationSet = getHideAnimation()
                tvCount.startAnimation(hideAnimationSet)
                btnMinus.startAnimation(hideAnimationSet)
                //删除缓存
                TakeoutApp.sInstance.deleteCacheSelectedInfo(goodsInfo.id)
            }else{
                //更新缓存
                TakeoutApp.sInstance.updateCacheSelectedInfo(goodsInfo.id,Constants.MINUS)

            }




            count--
            //改变数据层
            goodsInfo.count = count
            notifyDataSetChanged()





        }



        private fun doAppOperation() {
            //改变count值
            var count = goodsInfo.count
            if (count ==0){
                //第一次点击加号执行动画级

                val showAnimationSet:AnimationSet = getShowAnimation()
                tvCount.startAnimation(showAnimationSet)
                btnMinus.startAnimation(showAnimationSet)
                //添加缓存
                TakeoutApp.sInstance.addCacheSelectedInfo(CacheSelectedInfo(goodsInfo.sellerId,goodsInfo.typeId,goodsInfo.id,1))
            } else{
                //更新缓存
                TakeoutApp.sInstance.updateCacheSelectedInfo(goodsInfo.id,Constants.ADD)
            }


            count++
            //改变数据层
            goodsInfo.count = count
            notifyDataSetChanged()



            /**
             * 抛物线
             * 1,克隆+ 号，并且添加到activity上
             * 2,执行抛物线动画（水平位移，垂直加速位移
             * 3，动画完成后回收克隆的+号
             *
             */

            // 克隆+ 号，并且添加到activity上
            var ib = ImageButton(context)
            //大小，位置，背景全部相同
            ib.setBackgroundResource(R.drawable.button_add)

            val  srcLocation = IntArray(2)
            btnAdd.getLocationInWindow(srcLocation)

           // Log.e("location",srcLocation[0].toString()+":"+srcLocation[1])

            ib.x = srcLocation[0].toFloat()
            ib.y = srcLocation[1].toFloat()
            (goodsFragment .activity as BusinessActivity).addImageButton(ib,btnAdd.width,btnAdd.height)


            //执行抛物线动画（水平位移，垂直加速位移

            val destLocation = (goodsFragment.activity as BusinessActivity).getCartLocation()
            val parabolaAnim:AnimationSet =  getParabolaAnimation(ib,srcLocation,destLocation)
            ib.startAnimation(parabolaAnim)
            //动画完成后回收克隆的+号


        }


        private fun getParabolaAnimation(ib:ImageButton,srcLocation:IntArray,destLocation:IntArray): AnimationSet {



            val parabolaAnim:AnimationSet = AnimationSet(false)

            parabolaAnim.duration = DURATION
            val translateX = TranslateAnimation(
                Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, destLocation[0].toFloat() - srcLocation[0].toFloat(), Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f)

            translateX.duration =DURATION
            parabolaAnim.addAnimation(translateX)

            val translateY = TranslateAnimation(
                Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, destLocation[1].toFloat() - srcLocation[1].toFloat())

            translateY.setInterpolator(AccelerateInterpolator())
            translateY.duration =DURATION
            parabolaAnim.addAnimation(translateY)
            parabolaAnim.setAnimationListener(object :Animation.AnimationListener{

                override fun onAnimationEnd(animation: Animation?) {
                    val viewParent = ib.parent
                    if (viewParent != null){
                        (viewParent as ViewGroup).removeView(ib)
                    }

                }

                override fun onAnimationRepeat(animation: Animation?) {

                }

                override fun onAnimationStart(animation: Animation?) {

                }

            })
            return parabolaAnim



        }

        private fun getHideAnimation(): AnimationSet {
            var  animationSet:AnimationSet = AnimationSet(false)
            animationSet.duration = DURATION
            val alphaAnim:Animation = AlphaAnimation(1f,0.0f)
            alphaAnim.duration = DURATION
            animationSet.addAnimation(alphaAnim)
            val  rotateAnima :Animation = RotateAnimation(720.0f,0.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
            rotateAnima.duration = DURATION
            animationSet.addAnimation(rotateAnima)

            val  translateAnimation:Animation = TranslateAnimation(Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,2.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f)
            translateAnimation.duration =DURATION

            animationSet.addAnimation(translateAnimation)
            return animationSet
        }


        private fun getShowAnimation(): AnimationSet {
            var  animationSet:AnimationSet = AnimationSet(false)
            animationSet.duration = DURATION
            val alphaAnim:Animation = AlphaAnimation(0.0f,1.0f)
            alphaAnim.duration = DURATION
            animationSet.addAnimation(alphaAnim)
            val  rotateAnima :Animation = RotateAnimation(0.0f,720.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
            rotateAnima.duration = DURATION
            animationSet.addAnimation(rotateAnima)

            val  translateAnimation:Animation = TranslateAnimation(Animation.RELATIVE_TO_SELF,2.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f, Animation.RELATIVE_TO_SELF,0.0f)
            translateAnimation.duration =DURATION

            animationSet.addAnimation(translateAnimation)
            return animationSet
        }



        val ivIcon:ImageView
        val tvName:TextView
        val tvForm :TextView
        val tvMonthSale :TextView
        val tvNewPrice :TextView
        val tvOldPrice :TextView
        val btnAdd:ImageButton
        val btnMinus:ImageButton
        val tvCount:TextView

        init {
            ivIcon = itemView.find(R.id.iv_icon)
            tvName = itemView.find(R.id.tv_name)
            tvForm = itemView.find(R.id.tv_form)
            tvMonthSale = itemView.find(R.id.tv_month_sale)
            tvNewPrice = itemView.find(R.id.tv_newprice)
            tvOldPrice = itemView.find(R.id.tv_oldprice)
            tvCount = itemView.find(R.id.tv_count)
            btnAdd = itemView.find(R.id.ib_add)
            btnMinus= itemView.find(R.id.ib_minus)


            btnAdd.setOnClickListener(this)
            btnMinus.setOnClickListener(this)
        }

        
        fun binData(goodsInfo: goodsInfo) {
            this.goodsInfo = goodsInfo
            Picasso.with(context).load(goodsInfo.icon).into(ivIcon)
            tvName .text =goodsInfo.name

            tvForm.text=goodsInfo.form
            tvMonthSale.text = "月售${goodsInfo.monthSaleNum}份"
//            tvNewPrice.text= "￥${goodsInfo.newPrice}"
//            tvOldPrice.text="￥${goodsInfo.oldPrice}"
                //货币转换
            tvNewPrice.text= PriceFormater.format(goodsInfo.newPrice.toFloat())

            tvOldPrice.text = PriceFormater.format(goodsInfo.oldPrice.toFloat())
            tvOldPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            if (goodsInfo.oldPrice > 0){
                tvOldPrice.visibility = View.VISIBLE
            }else{
                tvOldPrice.visibility = View.GONE
            }
             //赋值处理
            tvCount.text = goodsInfo.count.toString()
            if (goodsInfo.count >0){

                tvCount.visibility = View.VISIBLE
                btnMinus.visibility = View.VISIBLE
            }else{
                tvCount.visibility = View.INVISIBLE
                btnMinus.visibility = View.INVISIBLE
            }


        }

    }




    var goodsList :List<goodsInfo> = ArrayList()

    fun setDatas(goodsInfoList: List<goodsInfo>){
        this.goodsList = goodsInfoList
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var  itemView:View


        val  goodsItemHolder:GoodsItemHolder

        if (convertView ==null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_goods, parent, false)

            goodsItemHolder = GoodsItemHolder(itemView)


            itemView.tag = goodsItemHolder

        }else{
            itemView = convertView
            goodsItemHolder = itemView.tag as GoodsItemHolder

        }

        goodsItemHolder.binData(goodsList.get(position))
        return  itemView

    }

    override fun getItem(position: Int): Any {

        return goodsList.get(position)

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()

    }

    override fun getCount(): Int {
        return goodsList.size
    }

    override fun getHeaderId(position: Int): Long {
        val  goodsInfo: goodsInfo = goodsList.get(position)
            return  goodsInfo.typeId.toLong()

    }

    override fun getHeaderView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val goodsInfo : goodsInfo = goodsList.get(position)
        val typeName = goodsInfo.typeName
        val textView : TextView= LayoutInflater.from(context).inflate(R.layout.item_type_header,parent,false) as TextView
        textView.text =typeName
        textView.setTextColor(Color.BLACK)
        return textView

    }


}