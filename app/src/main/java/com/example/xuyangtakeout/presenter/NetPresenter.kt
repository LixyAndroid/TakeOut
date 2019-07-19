package com.example.xuyangtakeout.presenter

import android.support.constraint.Constraints
import android.util.Log
import com.example.xuyangtakeout.model.net.ResponseInfo
import com.example.xuyangtakeout.model.net.TakeoutService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Mloong
 * on 2019/5/19 17:14.
 */

open abstract  class NetPresenter {
    val takeoutService: TakeoutService

    init {
        Log.e(Constraints.TAG,"开始请求网络数据...")
        val retrofit = Retrofit.Builder()
            //本地
             // .baseUrl("http://10.23.15.6:8080/TakeOutService/")
            //腾讯云IP
            .baseUrl("http://203.195.245.169:8080/TakeOut/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()

        takeoutService = retrofit.create<TakeoutService>(TakeoutService::class.java)
    }

    abstract fun parserJson(json: String)



    val callback = object : Callback<ResponseInfo> {
        override fun onResponse(call: Call<ResponseInfo>, response: Response<ResponseInfo>) {
            if (response == null) {
                Log.e("home", "服务器没有成功返回")
            } else {
                Log.e("home", "成功连上服务器")
                if (response.isSuccessful()) {
                    val responseInfo = response.body()
                    if (responseInfo != null) {
                        if (responseInfo.code.equals("0")) {
                            val json = responseInfo.data
                            parserJson(json)
                        } else if (responseInfo.code.equals("-1")) {
                            //根据具体接口文档表示含义，比如定义-1为空数据
                            Log.e("home", "定义-1为空数据")
                        }
                    }
                } else {
                    Log.e("home", "服务器代码错误")
                }
            }
        }

        override fun onFailure(call: Call<ResponseInfo>?, t: Throwable?) {
            //没有连上服务器
            Log.e("home", "没有连上服务器")
        }

    }


}