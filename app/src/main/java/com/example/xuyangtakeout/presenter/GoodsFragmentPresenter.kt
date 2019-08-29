package com.example.xuyangtakeout.presenter


import android.util.Log
import com.example.xuyangtakeout.model.bean.GoodsInfo
import com.example.xuyangtakeout.model.bean.GoodsTypeInfo
import com.example.xuyangtakeout.ui.activity.BusinessActivity
import com.example.xuyangtakeout.ui.fragment.GoodsFragment
import com.example.xuyangtakeout.utils.TakeoutApp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.lang.reflect.Type

/**
 * Created by Li Xuyang
 * on 2019/5/20 21:32.
 */
class GoodsFragmentPresenter(val goodsFragment: GoodsFragment) : NetPresenter() {
    val allTypeGoodsList: ArrayList<GoodsInfo> = arrayListOf()
    var goodsTypeList: List<GoodsTypeInfo> = arrayListOf()
    //拿到此商家所有的商品
    fun getBusinessInfo(sellerId: String) {
        val buinessCall = takeoutService.getBuinessInfo(sellerId)
        buinessCall.enqueue(callback)

    }

    override fun parserJson(json: String) {


        val gson = Gson()
        val jsonObj = JSONObject(json)
        val allStr = jsonObj.getString("list")
        //是否有点餐记录
        val hasSelectInfo = (goodsFragment.activity as BusinessActivity).hasSelectInfo

        //list<GoodsTypeInfo>
        goodsTypeList = gson.fromJson(allStr, object : TypeToken<List<GoodsTypeInfo>>() {
        }.type)

        Log.e("business", "一共有${goodsTypeList.size}个类别商品")

        for (i in 0 until goodsTypeList.size) {
            val goodsTypeInfo = goodsTypeList.get(i)
            var aTypeCount = 0
            if (hasSelectInfo) {

                aTypeCount = TakeoutApp.sInstance.queryCacheSelectedInfoByTypeId(goodsTypeInfo.id)

                goodsTypeInfo.redDOTCount = aTypeCount   //一个类别的记录
            }


            val aTypeList: List<GoodsInfo> = goodsTypeInfo.list
            for (j in 0 until aTypeList.size) {
                val goodsInfo = aTypeList.get(j)
                if (aTypeCount > 0) {
                    val count = TakeoutApp.sInstance.queryCacheSelectedInfoByGoodsId(goodsInfo.id)
                    goodsInfo.count = count   //具体商品的记录个数
                }
                //建立双向绑定关系
                goodsInfo.typeName = goodsTypeInfo.name
                goodsInfo.typeId = goodsTypeInfo.id

            }



            allTypeGoodsList.addAll(goodsTypeInfo.list)

        }
        //更新购物车的UI
        (goodsFragment.activity as BusinessActivity).updateCarUi()

        goodsFragment.onLoadBusinessSucess(goodsTypeList, allTypeGoodsList)


    }

    //根据商品类别id找到此类别第一个商品的位置
    fun getGoodsPositionByTypeId(typeId: Int): Int {
        var position = -1  //-1表示为找到
        for (j in 0 until allTypeGoodsList.size) {

            val goodsInfo = allTypeGoodsList.get(j)

            if (goodsInfo.typeId == typeId) {
                position = j
                break
            }
        }
        return position
    }

    //根据类别id找到其在左侧的id
    fun getTypePositionByTypeId(newTypeId: Int): Int {

        var position = -1  //-1表示为找到
        for (i in 0 until goodsTypeList.size) {

            val goodsTypeInfo = goodsTypeList.get(i)

            if (goodsTypeInfo.id == newTypeId) {
                position = i
                break
            }
        }
        return position

    }

    fun getCartList(): ArrayList<GoodsInfo> {

        val cartList = arrayListOf<GoodsInfo>()
        //count >0
        for (j in 0 until allTypeGoodsList.size) {

            val goodsInfo = allTypeGoodsList.get(j)

            if (goodsInfo.count > 0) {
                cartList.add(goodsInfo)
            }
        }

        return cartList
    }

    fun clearCart() {
        val cartList = getCartList()
        for (j in 0 until cartList.size) {
            val goodsInfo = cartList.get(j)

            goodsInfo.count = 0

        }
    }
}