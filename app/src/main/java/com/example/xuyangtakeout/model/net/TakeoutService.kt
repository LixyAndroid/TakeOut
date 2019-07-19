package com.example.xuyangtakeout.model.net


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable


/**
 * Created by Mloong
 * on 2019/5/18 21:42.
 */
interface TakeoutService {

  @GET("home")
  fun getHomeInfo():Call<ResponseInfo>


  @GET("login")
  fun loginByPhone(@Query("phone") phone:String):Call<ResponseInfo>


  //使用Rxjava组合的接口
  @GET("order")
  fun getOrderListByRxjava(@Query("id") userId:String):Observable<ResponseInfo>


  @GET("business")
  fun getBuinessInfo(@Query("sellerId")sellerId: String) :Call<ResponseInfo>




}