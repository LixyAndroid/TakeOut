package com.example.xuyangtakeout.ui.fragment

import android.app.Fragment
import android.graphics.Color
import android.os.Bundle

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Mloong
 * on 2019/5/20 20:45.
 */
class SellerFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val sellerView = TextView(activity)
        sellerView.text = "商家"
        sellerView.gravity = Gravity.CENTER
        sellerView.setTextColor(Color.BLACK)
        return sellerView
    }
}