package com.example.xuyangtakeout.ui.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Fragment
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.ui.activity.LoginActivity
import com.example.xuyangtakeout.utils.TakeoutApp
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class MoreFragment : Fragment() {

    lateinit var exit: Button

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val moreview = View.inflate(activity, R.layout.fragment_more, null)
        exit = moreview.find(R.id.buttonExit)

        return moreview
    }


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        //展示登录成功后的ui效果
        val user = TakeoutApp.sUser
        if (user.id == -1) {
            //未登录
            toast("待开发，仅有退出登录功能")
            exit.setOnClickListener {
                toast("您还未登录呢！")
            }

        } else {
            exit.setOnClickListener {

                var builder = AlertDialog.Builder(context)

                builder.setTitle("确定要退出吗")
                builder.setPositiveButton("是，我要减肥", object : DialogInterface.OnClickListener {

                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = Intent(activity, LoginActivity::class.java)
                        activity.startActivity(intent)
                        //一行代码搞定退出功能
                        user.id = -1
                    }

                })
                builder.setNegativeButton("不，我只是点错了", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                    }
                })

                builder.show()

            }

        }
    }
}