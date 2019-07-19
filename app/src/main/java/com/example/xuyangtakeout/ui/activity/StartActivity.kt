package com.example.xuyangtakeout.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.utils.ConstantTool


/**
 * Created by Mloong
 * on 2019/5/28 12:58
 */
class StartActivity :AppCompatActivity(){
    internal var llStart: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)            //设置为全屏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_start)

        llStart = this.findViewById<View>(R.id.llStart) as ImageView
        val anim = AnimationUtils.loadAnimation(this, R.anim.anim_start) //加载动画
        llStart!!.startAnimation(anim)                    //给布局设置动画
        Thread(r).start()                        //开启一个线程 拉开动画播放和下一步动作的时间
    }

    internal var r: Runnable = Runnable {
        try {
            Thread.sleep(1500)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ConstantTool.toActivity(this@StartActivity, MainActivity::class.java)//Activity转换
    }

    override fun onStop() {
        super.onStop()
        this.finish()
    }


}