package com.example.xuyangtakeout.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout

import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.model.bean.Order
import com.example.xuyangtakeout.presenter.OrderFragmentPresenter
import com.example.xuyangtakeout.ui.adapter.OrderRvAdapter
import com.example.xuyangtakeout.utils.TakeoutApp
import org.jetbrains.anko.find
import org.jetbrains.anko.toast


class OrderFragment:Fragment(){

    lateinit var orderPresenter: OrderFragmentPresenter
    lateinit var rvOrder: RecyclerView
    lateinit var swipeLayout: SwipeRefreshLayout
    lateinit var adapter: OrderRvAdapter
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val orderview = View.inflate(activity, R.layout.fragment_order, null)
       // (view as TextView).setText("订单")

        orderPresenter = OrderFragmentPresenter(this)
        rvOrder = orderview.find<RecyclerView>(R.id.rv_order_list)
        rvOrder.layoutManager = LinearLayoutManager(activity)
        adapter = OrderRvAdapter(activity)
        rvOrder.adapter =adapter

        swipeLayout = orderview.find<SwipeRefreshLayout>(R.id.srl_order)
        swipeLayout.setOnRefreshListener (object :SwipeRefreshLayout.OnRefreshListener{

            override fun onRefresh() {
                //下拉后重新请求
                val userId = TakeoutApp.sUser.id
                if ( -1 == userId){
                    toast("必须先登录才能查看订单，请先登录")


                }else {
                    orderPresenter.getOrderList(userId.toString())
                }

            }

        })

        return  orderview
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //访问服务器，获取所有订单数据


        val userId = TakeoutApp.sUser.id
        if ( -1 == userId){
            toast("必须先登录才能查看订单，请先登录")


        }else {
            orderPresenter.getOrderList(userId.toString())
        }
    }

    fun onOrderSuccess(orderList: List<Order>) {
        //TODO: 给adapter设置数据
        adapter.setOrderData(orderList)

        swipeLayout.isRefreshing = false


    }

    fun onOrderFailed() {

        toast("服务器繁忙")
        swipeLayout.isRefreshing = false

    }

}