package com.example.xuyangtakeout.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.widget.Button
import android.widget.TextView
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.utils.BottomNavigation
import org.jetbrains.anko.find

/**
 * Created by Mloong
 * on 2019/5/27 22:38
 */
class OnlinePaymentActivity : AppCompatActivity() {

    lateinit var confirmPay: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_payment)
        processIntent()

        //底部导航适配,HuaWeiP20

        if (BottomNavigation.checkDeviceHasNavigationBar(this)) {
            BottomNavigation.assistActivity(findViewById(android.R.id.content));
        }


        confirmPay = find(R.id.bt_confirm_pay)
        confirmPay.setOnClickListener {
            val intent: Intent = Intent(this, AlipayActivity::class.java)
            startActivity(intent)
        }


    }


    lateinit var mTvCountPrice: TextView
    @SuppressLint("SetTextI18n")
    private fun processIntent() {
        if (intent != null) {
            mTvCountPrice = findViewById(R.id.tv_pay_money)
            val mCountPrice = intent.getStringExtra("Price")
            mTvCountPrice.setText(mCountPrice)
        }
    }


}