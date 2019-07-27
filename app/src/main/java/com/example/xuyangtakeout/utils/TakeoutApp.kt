package com.example.xuyangtakeout.utils

import cn.jpush.android.api.JPushInterface
import com.example.xuyangtakeout.model.bean.CacheSelectedInfo
import com.example.xuyangtakeout.model.bean.User
import com.mob.MobApplication
import com.mob.MobSDK
import java.util.concurrent.CopyOnWriteArrayList


/**
 * Created by Mloong
 * on 2019/5/19 16:25.
 */
class TakeoutApp : MobApplication() {

    //点餐缓存集合
    val infos: CopyOnWriteArrayList<CacheSelectedInfo> = CopyOnWriteArrayList()

    fun queryCacheSelectedInfoByGoodsId(goodsId: Int): Int {
        var count = 0
        for (i in 0..infos.size - 1) {
            val (_, _, goodsId1, count1) = infos[i]
            if (goodsId1 == goodsId) {
                count = count1
                break
            }
        }
        return count
    }

    fun queryCacheSelectedInfoByTypeId(typeId: Int): Int {
        var count = 0
        for (i in 0..infos.size - 1) {
            val (_, typeId1, _, count1) = infos[i]
            if (typeId1 == typeId) {
                count = count + count1
            }
        }
        return count
    }

    fun queryCacheSelectedInfoBySellerId(sellerId: Int): Int {
        var count = 0
        for (i in 0..infos.size - 1) {
            val (sellerId1, _, _, count1) = infos[i]
            if (sellerId1 == sellerId) {
                count = count + count1
            }
        }
        return count
    }

    fun addCacheSelectedInfo(info: CacheSelectedInfo) {
        infos.add(info)
    }

    fun clearCacheSelectedInfo(sellerId: Int) {
        //临时集合 需要移除的记录下来
        val temp = ArrayList<CacheSelectedInfo>()
        for (i in 0..infos.size - 1) {
            val info = infos[i]
            if (info.sellerId == sellerId) {
//                infos.remove(info)
                temp.add(info)
            }
        }
        infos.removeAll(temp)
    }

    fun deleteCacheSelectedInfo(goodsId: Int) {
        for (i in 0..infos.size - 1) {
            val info = infos[i]
            if (info.goodsId == goodsId) {
                infos.remove(info)
                break
            }
        }
    }

    fun updateCacheSelectedInfo(goodsId: Int, operation: Int) {
        for (i in 0..infos.size - 1) {
            var info = infos[i]
            if (info.goodsId == goodsId) {
                when (operation) {
                    Constants.ADD -> info.count = info.count + 1
                    Constants.MINUS -> info.count = info.count - 1
                }

            }
        }
    }


    //Mob的Key

    val Key: String = "2b2afb749eee8"

    val Secret: String = "068dffa68e704d6480477e6ae3f0a971"


    companion object {
        var sUser: User = User()
        lateinit var sInstance: TakeoutApp
    }


    //应用程序的入口
    override fun onCreate() {
        super.onCreate()

        sInstance = this

        MobSDK.init(this, Key, Secret)
        sUser.id = -1 //未登录用户为-1

        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)


    }
}