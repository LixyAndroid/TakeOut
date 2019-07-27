package com.example.xuyangtakeout.ui.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.model.bean.Seller
import com.example.xuyangtakeout.ui.activity.BusinessActivity
import com.example.xuyangtakeout.utils.TakeoutApp
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find


/**
 * Created by Android Studio.
 * User: Mloong
 * Date: 2019/5/17
 * Time: 23:17
 */

class HomeRvAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        val TYPE_TITLE = 0
        val TYPE_SELLER = 1
    }

    /**
     * 不同position对应不同类型
     */
    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_TITLE
        } else {
            return TYPE_SELLER
        }


    }

    var mDatas: ArrayList<Seller> = ArrayList()
    fun setData(data: ArrayList<Seller>) {
        this.mDatas = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            TYPE_TITLE -> (holder as TitleHolder).bindData("我是大哥--------------")
            TYPE_SELLER -> (holder as SellerHolder).binData(mDatas[position - 1])
        }

    }

    override fun getItemCount(): Int {

        if (mDatas.size > 0) {
            return mDatas.size + 1
        } else {
            return 0
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            TYPE_TITLE -> return TitleHolder(View.inflate(context, R.layout.item_title, null))
            TYPE_SELLER -> return SellerHolder(View.inflate(context, R.layout.item_seller, null))
            else -> return TitleHolder(View.inflate(context, R.layout.item_home_common, null))
        }
        //    return  SellerHolder(View.inflate(context, R.layout.item_home_common,null))

    }


    var url_maps: HashMap<String, String> = HashMap()

    inner class TitleHolder(item: View) : RecyclerView.ViewHolder(item) {
        val sliderLayout: SliderLayout


        init {

            sliderLayout = item.findViewById(R.id.slider)
        }

        fun bindData(data: String) {
            if (url_maps.size == 0) {
                url_maps.put(
                    "Marvel's The Avengers",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557475104513&di=1e18e262814f4a2259abee5592be962a&imgtype=0&src=http%3A%2F%2F08imgmini.eastday.com%2Fmobile%2F20190319%2F20190319082614_7b2724b5d733c80f78b9c5dc71fb2a9c_2_mwpm_03201609.jpg"
                )
                url_maps.put(
                    "Big Bang Theory",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557474981869&di=bd67103f4d6b329ab64aea76dad468ff&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201312%2F23%2F20131223011936_UXnWe.jpeg"
                )
                url_maps.put(
                    "Break Prison",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557476555844&di=69c76f170ef8a0d76f95c46813c2cc22&imgtype=0&src=http%3A%2F%2Fimages.fanpop.com%2Fimages%2Fimage_uploads%2FPrison-Break-prison-break-772275_1280_960.jpg"
                )
                url_maps.put(
                    "Game of Thrones",
                    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557474878055&di=43a05863851f7999a36a0ae6b27acb49&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fent%2Ftransform%2F20170801%2FaU3k-fyinvys9756735.jpg"
                )
                for ((key, value) in url_maps) {
                    var textSliderView: TextSliderView = TextSliderView(context)
                    textSliderView.description(key).image(value)
                    sliderLayout.addSlider(textSliderView)
                }

            }


        }
    }


    inner class SellerHolder(item: View) : RecyclerView.ViewHolder(item) {

        val tvTitle: TextView
        val ivLogo: ImageView
        val rbScore: RatingBar
        val tvSale: TextView
        val tvSendPrice: TextView
        val tvDistance: TextView
        lateinit var mSeller: Seller

        init {
            tvTitle = item.find(R.id.tv_title)
            ivLogo = item.find(R.id.seller_logo)
            rbScore = item.find(R.id.ratingBar)
            tvSale = item.find(R.id.tv_home_sale)
            tvSendPrice = item.find(R.id.tv_home_send_price)
            tvDistance = item.find(R.id.tv_home_distance)
            item.setOnClickListener {
                val intent: Intent = Intent(context, BusinessActivity::class.java)

                //读取谋客户在该店是否有缓存信息
                //逐层读取，首先判断该店是否有缓存

                var hasSelectInfo = false
                val count = TakeoutApp.sInstance.queryCacheSelectedInfoBySellerId(mSeller.id.toInt())
                if (count > 0) {

                    hasSelectInfo = true
                }

                intent.putExtra("seller", mSeller)
                intent.putExtra("hasSelectInfo", hasSelectInfo)
                context.startActivity(intent)

            }


        }

        fun binData(seller: Seller) {

            this.mSeller = seller
            tvTitle.text = seller.name
            Picasso.with(context).load(seller.icon).into(ivLogo)
            rbScore.rating = seller.score.toFloat()
            tvSale.text = "月售${seller.sale}单"
            tvSendPrice.text = "￥${seller.sendPrice}起送/配送费￥${seller.deliveryFee}"
            tvDistance.text = seller.distance
        }


    }

}
