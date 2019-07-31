package com.example.xuyangtakeout.ui.activity

import android.app.AlertDialog
import android.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v13.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.ui.fragment.CommentsFragment
import com.example.xuyangtakeout.ui.fragment.GoodsFragment
import com.example.xuyangtakeout.ui.fragment.SellerFragment
import com.example.xuyangtakeout.utils.PriceFormater
import kotlinx.android.synthetic.main.activity_business.*
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import com.example.xuyangtakeout.model.bean.Seller
import com.example.xuyangtakeout.ui.adapter.CartRvAdapter
import com.example.xuyangtakeout.utils.BottomNavigation

import com.example.xuyangtakeout.utils.TakeoutApp


/**
 * Created by Mloong
 * on 2019/5/20 20:27.
 */

class BusinessActivity : AppCompatActivity(), View.OnClickListener {

    var hasSelectInfo = false
    lateinit var seller: Seller
    private fun processIntent() {
        if (intent.hasExtra("hasSelectInfo")) {
            hasSelectInfo = intent.getBooleanExtra("hasSelectInfo", false)
            seller = intent.getSerializableExtra("seller") as Seller
            tvDeliveryFee.text = "另需派送费" + PriceFormater.format(seller.deliveryFee.toFloat())
            tvSendPrice.text = PriceFormater.format(seller.sendPrice.toFloat()) + "起送"
        }


    }

    var bottomSheetView: View? = null
    lateinit var rvCart: RecyclerView
    lateinit var cartAdapter: CartRvAdapter


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.bottom -> showOrHideCart()
            R.id.tvSubmit -> {
                val intent: Intent = Intent(this, ConfirmOrderActivity::class.java)

                intent.putExtra("countPrice", tvCountPrice.text)

                startActivity(intent)
            }
        }
    }

    fun showOrHideCart() {
        if (bottomSheetView == null) {
            //加载要显示的布局
            bottomSheetView =
                LayoutInflater.from(this).inflate(R.layout.cart_list, window.decorView as ViewGroup, false)

            rvCart = bottomSheetView!!.findViewById(R.id.rvCart) as RecyclerView
            rvCart.layoutManager = LinearLayoutManager(this)

            cartAdapter = CartRvAdapter(this)
            rvCart.adapter = cartAdapter

            val tvClear = bottomSheetView!!.findViewById(R.id.tvClear) as TextView

            tvClear.setOnClickListener {
                var builder = AlertDialog.Builder(this)

                builder.setTitle("确定都不吃了啊")
                builder.setPositiveButton("是，我要减肥", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        //开始清空购物车,把购物车中商品数量重置为0
                        val goodsFragment: GoodsFragment = fragments.get(0) as GoodsFragment
                        goodsFragment.goodsFragmentPresenter.clearCart()

                        cartAdapter.notifyDataSetChanged()
                        //关闭购物车
                        showOrHideCart()
                        //刷新右侧
                        goodsFragment.goodsAdapter.notifyDataSetChanged()
                        //清空所有的红点
                        clearRedDot()

                        //刷新左侧列表

                        goodsFragment.goodsTypeAdapter.notifyDataSetChanged()

                        //更新下方购物篮
                        updateCarUi()
                        //清空缓存
                        TakeoutApp.sInstance.clearCacheSelectedInfo(seller.id.toInt())


                    }


                })
                builder.setNegativeButton("不，我只是点错了", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                    }
                })

                builder.show()
            }

        }
        //判断BottomSheetLayout内容是否显示
        if (bottomSheetLayout.isSheetShowing) {
            //关闭内容显示
            bottomSheetLayout.dismissSheet()


        } else {
            //显示BottomSheetLayout里面的内容
            val goodsFragment: GoodsFragment = fragments.get(0) as GoodsFragment
            val cartList = goodsFragment.goodsFragmentPresenter.getCartList()
            cartAdapter.setCart(cartList)
            if (cartList.size > 0) {
                bottomSheetLayout.showWithSheetView(bottomSheetView)
            }
        }
    }


    private fun clearRedDot() {

        val goodsFragment: GoodsFragment = fragments.get(0) as GoodsFragment
        val goodsTypeList = goodsFragment.goodsFragmentPresenter.goodsTypeList
        for (i in 0 until goodsTypeList.size) {
            val goodsTypeInfo = goodsTypeList.get(i)
            goodsTypeInfo.redDOTCount = 0

        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)
        processIntent()

//        //判断设备是否有虚拟按键，如果有增加paddingBottom = 50dp
//        if (checkDeviceHasNavigationBar(this)) {
//            // val x = getNavigationBarHeight(this)
//            fl_Container.setPadding(0, 0, 0, 48.dip2px())
//        }


        if (BottomNavigation.checkDeviceHasNavigationBar(this)) {
            BottomNavigation.assistActivity(findViewById(android.R.id.content));
        }

        vp.adapter = BusinessFragmentAdapter()

        tabs.setupWithViewPager(vp)

        bottom.setOnClickListener(this)

    }


    val titles = listOf<String>("商品", "商家", "评论")

    inner class BusinessFragmentAdapter : FragmentPagerAdapter(fragmentManager) {

        //出现标题
        override fun getPageTitle(position: Int): CharSequence? {
            return titles.get(position)
        }

        override fun getItem(position: Int): Fragment {
            return fragments.get(position)
        }


        override fun getCount(): Int {
            return titles.size
        }


    }


    val fragments = listOf<Fragment>(GoodsFragment(), SellerFragment(), CommentsFragment())


    /**
     * 把转换类功能添加到Int类中作为扩展函数
     */

    fun Int.dip2px(): Int {
        val scale = resources.displayMetrics.density
        return (toFloat() * scale + 0.5f).toInt()
    }


    fun addImageButton(ib: ImageButton, width: Int, height: Int) {

        fl_Container.addView(ib, width, height)

    }

    fun getCartLocation(): IntArray {


        val destLocation = IntArray(2)
        imgCart.getLocationInWindow(destLocation)
        return destLocation
    }


    fun updateCarUi() {
        //更新数量，更新总价
        var count = 0
        var countPrice = 0.0f

        //哪些商品属于购物车？
        val goodsFragment: GoodsFragment = fragments.get(0) as GoodsFragment
        val cartList = goodsFragment.goodsFragmentPresenter.getCartList()
        for (i in 0 until cartList.size) {
            val goodsInfo = cartList.get(i)
            count += goodsInfo.count
            countPrice += goodsInfo.count * goodsInfo.newPrice.toFloat()

        }
        tvSelectNum.text = count.toString()
        if (count > 0) {
            tvSelectNum.visibility = View.VISIBLE
        } else {

            tvSelectNum.visibility = View.GONE
        }

        tvCountPrice.text = countPrice.toString()
        if (countPrice >= seller.sendPrice.toFloat()) {
            tvSubmit.visibility = View.VISIBLE

            tvSendPrice.visibility = View.GONE
        } else {
            tvSubmit.visibility = View.GONE

            tvSendPrice.visibility = View.VISIBLE

        }

    }


}