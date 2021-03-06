package com.example.xuyangtakeout.presenter

import android.util.Log
import com.example.xuyangtakeout.model.bean.Order
import com.example.xuyangtakeout.model.net.ResponseInfo
import com.example.xuyangtakeout.ui.fragment.OrderFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * Created by Li Xuyang
 * on 2019/5/19 19:50.
 */
class OrderFragmentPresenter(val orderFragment: OrderFragment) : NetPresenter() {


    fun getOrderList(userId: String) {
        val observable: Observable<ResponseInfo> = takeoutService.getOrderListByRxjava(userId)

        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                parserJson(it.data)
            }, {
                Log.e("rxjava", it.localizedMessage)
            }, {
                Log.e("rxjava", "onComplete")
            })


    }


    override fun parserJson(json: String) {

        //此处解析，list<Order>

        val orderList: List<Order> = Gson().fromJson(json, object : TypeToken<List<Order>>() {}.type)

        if (orderList.isNotEmpty()) {
            orderFragment.onOrderSuccess(orderList)
        } else {
            orderFragment.onOrderFailed()
        }


    }
}