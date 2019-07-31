package com.example.xuyangtakeout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.example.xuyangtakeout.R
import android.view.animation.Animation.AnimationListener


/**
 * Created by Mloong
 * on 2019/5/28 12:58
 */
class SplashActivity : AppCompatActivity() {


    lateinit var alphaAnimation: AlphaAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = View.inflate(this, R.layout.activity_splash, null)
        setContentView(view)

        //渐变展示启动屏
        alphaAnimation = AlphaAnimation(0.3f, 1.0f)
        alphaAnimation.duration = 2000
        view.startAnimation(alphaAnimation)
        alphaAnimation.setAnimationListener(object : AnimationListener {
            override fun onAnimationEnd(arg0: Animation) {
                redirectTo()
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationStart(animation: Animation) {}

        })
    }


    fun redirectTo() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }




}