package com.example.xuyangtakeout.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.model.bean.RecepitAddressBean
import com.example.xuyangtakeout.utils.CommonUtil.Companion.checkDeviceHasNavigationBar
import kotlinx.android.synthetic.main.activity_confirm_order.*
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_confirm_order.tvSubmit


/**
 * Created by Mloong
 * on 2019/5/22 20:46
 */
class ConfirmOrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)
        processIntent()


        //判断设备是否有虚拟按键，如果有增加paddingBottom = 50dp
        if (checkDeviceHasNavigationBar(this)) {
            // val x = getNavigationBarHeight(this)
            activity_confirm_order.setPadding(0, 0, 0, 48.dip2px())
        }

        rl_location.setOnClickListener {
            val intent = Intent(this, RecepitAddressActivity::class.java)
            startActivityForResult(intent, 1002)
        }

        tvSubmit.setOnClickListener {
            val intent = Intent(this, OnlinePaymentActivity::class.java)
            intent.putExtra("Price", mTvCountPrice.text)


            startActivity(intent)
        }


    }


    lateinit var mTvCountPrice: TextView
    @SuppressLint("SetTextI18n")
    private fun processIntent() {
        if (intent != null) {


            mTvCountPrice = findViewById(R.id.tv_CountPrice)
            var mCountPrice = intent.getStringExtra("countPrice")
            mCountPrice = (mCountPrice.toFloat() + 4f).toString()
            mTvCountPrice.text = "待支付$${mCountPrice}"


        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 200) {
            if (data != null) {
                val address: RecepitAddressBean = data.getSerializableExtra("address") as RecepitAddressBean
                tv_name.text = address.username
                tv_sex.text = address.sex
                tv_phone.text = address.phone
                tv_address.text = address.address
            }

        }
    }


    fun Int.dip2px(): Int {
        val scale = resources.displayMetrics.density
        return (toFloat() * scale + 0.5f).toInt()
    }


}