package com.example.xuyangtakeout.ui.activity

import android.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.ui.fragment.HomeFragment
import com.example.xuyangtakeout.ui.fragment.MoreFragment
import com.example.xuyangtakeout.ui.fragment.OrderFragment
import com.example.xuyangtakeout.ui.fragment.UserFragment
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Context
import com.example.xuyangtakeout.utils.CommonUtil.Companion.checkDeviceHasNavigationBar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //判断设备是否有虚拟按键，如果有增加paddingBottom = 50dp
        if (checkDeviceHasNavigationBar(this)){
            // val x = getNavigationBarHeight(this)
            ll_main_activity.setPadding(0,0,0,50.dip2px())
        }

        initBottomBar()
        changeIndex(0)
    }


    /**
     * 把转换类功能添加到Int类中作为扩展函数
     */

    fun Int.dip2px(): Int {
        val scale = resources.displayMetrics.density
        return (toFloat() * scale + 0.5f).toInt()
    }





    val fragments: List<Fragment> = listOf<Fragment>(HomeFragment(), OrderFragment(), UserFragment(), MoreFragment())


    private fun initBottomBar() {
        for (i in 0 until main_bottom_bar.childCount) {
            main_bottom_bar.getChildAt(i).setOnClickListener {
                    view ->
                changeIndex(i)
            }
        }
    }

    private fun changeIndex(index: Int) {
        for (i in 0 until main_bottom_bar.childCount) {
            val child = main_bottom_bar.getChildAt(i)
            if (i == index) {
                //选中，禁用效果i
                setEnable(child, false)
            } else {
                //没选中的，enable=true
                setEnable(child, true)
            }
        }
        fragmentManager.beginTransaction().replace(R.id.main_content, fragments[index]).commit()
    }



    private fun setEnable(child: View, isEnable: Boolean) {
        child.isEnabled = isEnable
        if (child is ViewGroup) {
            for (i in 0 until child.childCount) {
                child.getChildAt(i).isEnabled = isEnable
            }
        }
    }
}
