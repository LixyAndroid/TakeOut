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
 * Created by Li Xuyang
 * on 2019/5/20 20:45.
 */

class CommentsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val commentsView = TextView(activity)
        commentsView.text = "评论"
        commentsView.gravity = Gravity.CENTER
        commentsView.setTextColor(Color.BLACK)
        return commentsView
    }
}