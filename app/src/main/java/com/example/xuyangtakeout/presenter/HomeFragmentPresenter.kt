package com.example.xuyangtakeout.presenter

import com.example.xuyangtakeout.model.bean.Seller
import com.example.xuyangtakeout.ui.fragment.HomeFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject


class HomeFragmentPresenter(val homeFragment: HomeFragment) : NetPresenter() {


    /**
     * 使用异步获取数据
     */
    fun getHomeInfo() {
        //TODO:要异步访问
        val homeCall = takeoutService.getHomeInfo()
        homeCall.enqueue(callback)


    }

    override fun parserJson(json: String) {
        //解析数据
        val gson = Gson()
        val jsonObject = JSONObject(json)
        val nearby = jsonObject.getString("nearbySellerList")
        // Log.e("home",nearby)
        val nearbySellers: List<Seller> = gson.fromJson(nearby, object : TypeToken<List<Seller>>() {}.type)
        val other = jsonObject.getString("otherSellerList")
        val otherSellers: List<Seller> = gson.fromJson(other, object : TypeToken<List<Seller>>() {}.type)

        //TODO:刷新UI
        //有数据，成功页面
        if (nearbySellers.isNotEmpty() || otherSellers.isNotEmpty()) {
            homeFragment.onHomeSuccess(nearbySellers, otherSellers)
        } else {
            //无数据，异常页面
            homeFragment.onHomeFailed()
        }
    }


}


