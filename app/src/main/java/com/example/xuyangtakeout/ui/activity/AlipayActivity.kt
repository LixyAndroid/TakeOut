package com.example.xuyangtakeout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import com.example.xuyangtakeout.R
import org.jetbrains.anko.find

/**
 * Created by Mloong
 * on 2019/5/28 20:54
 */
class AlipayActivity :AppCompatActivity(){

    lateinit var ibBack:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alipay)

        ibBack = find(R.id.ib_payback)
        ibBack.setOnClickListener {
            finish()
        }
    }

}