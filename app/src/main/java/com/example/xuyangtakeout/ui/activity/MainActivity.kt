package com.example.xuyangtakeout.ui.activity

import android.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.ui.fragment.HomeFragment
import com.example.xuyangtakeout.ui.fragment.MoreFragment
import com.example.xuyangtakeout.ui.fragment.OrderFragment
import com.example.xuyangtakeout.ui.fragment.UserFragment
import com.example.xuyangtakeout.utils.BottomNavigation
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //底部导航适配,HuaWeiP20


        if (BottomNavigation.checkDeviceHasNavigationBar(this)) {
            BottomNavigation.assistActivity(findViewById(android.R.id.content));
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
            main_bottom_bar.getChildAt(i).setOnClickListener { view ->
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
