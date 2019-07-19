package com.example.xuyangtakeout.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils

import cn.jpush.android.api.JPushInterface



/**
 * Created by Mloong
 * on 2019/5/19 21:49.
 */

class MyRecevier:BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

    //    Log.e("order","收到自定义消息")
        val bundle:Bundle? = intent?.extras

        if (bundle != null){
       //     val message:String = bundle.getString(JPushInterface.EXTRA_MESSAGE)
        //    Log.e("order",message) //消息内容
            if (JPushInterface.EXTRA_EXTRA !=null){
            val extras :String?= bundle.getString(JPushInterface.EXTRA_EXTRA)
//                        Log.e("order",extras)   //附加字段
            if (!TextUtils.isEmpty(extras)) {
                //方法1，通过mainactivity ->orderFragment ->第二个条目tvorderType ->改变值40

                //方法2，使用观察者模式（orderFragment是观察者，MyReceiver是被观察者
                OrderObservable.instance.newMsgComing(extras!!)

            }

            }



        }
    }
}