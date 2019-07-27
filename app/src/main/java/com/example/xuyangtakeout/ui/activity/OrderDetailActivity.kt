package com.example.xuyangtakeout.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.model.*
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.utils.OrderObservable
import com.example.xuyangtakeout.utils.OrderObservable.Companion.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL
import com.example.xuyangtakeout.utils.OrderObservable.Companion.ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE
import com.example.xuyangtakeout.utils.OrderObservable.Companion.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import com.amap.api.maps2d.model.PolylineOptions
import com.amap.api.maps2d.AMapUtils


/**
 * Created by Mloong
 * on 2019/5/27 19:25
 */
class OrderDetailActivity : AppCompatActivity(), Observer {

    override fun update(o: Observable?, data: Any?) {


        //更新UI

        val jsonObj: JSONObject = JSONObject(data as String)
        val pushOrderId = jsonObj.getString("orderId")
        val pushType = jsonObj.getString("type")



        if (orderId.equals(pushOrderId)) {

            type = pushType

        }


        val index = getIndex(type)
        (ll_order_detail_type_point_container.getChildAt(index) as ImageView).setImageResource(R.drawable.order_time_node_disabled)
        (ll_order_detail_type_container.getChildAt(index) as TextView).setTextColor(Color.BLUE)

        when (type) {
            OrderObservable.ORDERTYPE_RECEIVEORDER -> {
                //显示地图
                mMapView.visibility = View.VISIBLE
                // 南亭柠檬茶23.0366070000,113.3875080000
                aMap!!.moveCamera(CameraUpdateFactory.zoomTo(17f))
                //将地图移动到定位点
                aMap!!.moveCamera(
                    CameraUpdateFactory.changeLatLng(
                        LatLng(
                            23.0366070000,
                            113.3875080000
                        )
                    )
                )
                //标志买卖家  23.0366220000,113.3874620000   23.0359020000,113.3985620000


                val sellerMarker = aMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            23.0366220000,
                            113.3874620000
                        )
                    ).icon(BitmapDescriptorFactory.fromResource(R.drawable.order_seller_icon)).title("柠檬茶").snippet("我是渺小的卖家")
                )
                aMap!!.moveCamera(CameraUpdateFactory.zoomTo(17f))
                //将地图移动到定位点
                aMap!!.moveCamera(
                    CameraUpdateFactory.changeLatLng(
                        LatLng(
                            23.0366220000, 113.3874620000
                        )
                    )
                )
                var imageView = ImageView(this)
                imageView.setImageResource(R.drawable.order_buyer_icon)

                val buyerMarker = aMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            23.0359020000,
                            113.3985620000
                        )
                    ).icon(BitmapDescriptorFactory.fromView(imageView)).title("工学二号馆").snippet("一个霸气的卖家")
                )


            }
            OrderObservable.ORDERTYPE_DISTRIBUTION -> {

                points.add(LatLng(23.0358320000, 113.3881110000))
                //骑士登场
                var imageView = ImageView(this)
                imageView.setImageResource(R.drawable.order_rider_icon)
                riderMarker = aMap.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            23.0358320000,
                            113.3881110000
                        )
                    ).icon(BitmapDescriptorFactory.fromView(imageView)).title("我是骑手").snippet("我是骑手")
                )

                riderMarker.showInfoWindow()

                aMap!!.moveCamera(CameraUpdateFactory.zoomTo(17f))
                //将地图移动到定位点
                aMap!!.moveCamera(
                    CameraUpdateFactory.changeLatLng(
                        LatLng(
                            23.0358320000, 113.3881110000
                        )
                    )
                )
            }

            OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL -> {

            }
            OrderObservable.ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL -> {

                if (jsonObj.has("lat")) {
                    val lat = jsonObj.getString("lat")
                    val lng = jsonObj.getString("lng")

                    //移动骑手      23.0412540000,113.3771810000
                    //更新骑手位置就是用新位置重新标记骑手
                    riderMarker.hideInfoWindow()
                    riderMarker.position = LatLng(lat.toDouble(), lng.toDouble())

                    aMap!!.moveCamera(CameraUpdateFactory.zoomTo(17f))
                    //将地图移动到定位点
                    aMap!!.moveCamera(
                        CameraUpdateFactory.changeLatLng(
                            LatLng(lat.toDouble(), lng.toDouble())
                        )
                    )

                    //测距功能
                    val distance = AMapUtils.calculateLineDistance(
                        LatLng(lat.toDouble(), lng.toDouble()),
                        LatLng(23.0359020000, 113.3985620000)
                    )
                    riderMarker.title = "距离您还有" + Math.abs(distance) + "米"
                    riderMarker.showInfoWindow()

                    //绘制轨迹
                    points.add(LatLng(lat.toDouble(), lng.toDouble()))

                    val polyline = aMap.addPolyline(
                        PolylineOptions().add(
                            points.get(points.size - 1),
                            points.get(points.size - 2)
                        ).width(10f).color(Color.RED)
                    )
                }


            }
        }


    }


    var points: ArrayList<LatLng> = ArrayList()
    lateinit var riderMarker: Marker
    lateinit var aMap: AMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        processIntent()
        OrderObservable.instance.addObserver(this)
        mMapView.onCreate(savedInstanceState)  //此方法必须重写
        aMap = mMapView.getMap()


    }

    override fun onDestroy() {
        super.onDestroy()
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState)
    }

    lateinit var orderId: String
    lateinit var type: String
    private fun processIntent() {
        if (intent.hasExtra("orderId")) {
            orderId = intent.getStringExtra("orderId")
            type = intent.getStringExtra("type")
            val index = getIndex(type)
            (ll_order_detail_type_point_container.getChildAt(index) as ImageView).setImageResource(R.drawable.order_time_node_disabled)

            (ll_order_detail_type_container.getChildAt(index) as TextView).setTextColor(Color.BLUE)
        }
    }

    private fun getIndex(type: String): Int {
        var index = -1
        //        String typeInfo = "";
        when (type) {
            OrderObservable.ORDERTYPE_UNPAYMENT -> {
            }
            OrderObservable.ORDERTYPE_SUBMIT ->
                //                typeInfo = "已提交订单";
                index = 0
            OrderObservable.ORDERTYPE_RECEIVEORDER ->
                //                typeInfo = "商家接单";
                index = 1
            OrderObservable.ORDERTYPE_DISTRIBUTION, ORDERTYPE_DISTRIBUTION_RIDER_GIVE_MEAL, ORDERTYPE_DISTRIBUTION_RIDER_TAKE_MEAL, ORDERTYPE_DISTRIBUTION_RIDER_RECEIVE ->
                //                typeInfo = "配送中";
                index = 2
            OrderObservable.ORDERTYPE_SERVED ->
                //                typeInfo = "已送达";
                index = 3
            OrderObservable.ORDERTYPE_CANCELLEDORDER -> {
            }
        }//                typeInfo = "未支付";
        //                typeInfo = "取消的订单";
        return index
    }

}