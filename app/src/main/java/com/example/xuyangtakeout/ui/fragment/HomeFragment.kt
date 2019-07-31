package com.example.xuyangtakeout.ui.fragment

import android.annotation.SuppressLint
import android.app.Fragment
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.dagger2.component.DaggerHomeFragmentComponent
import com.example.xuyangtakeout.dagger2.module.HomeFragmentModule
import com.example.xuyangtakeout.model.bean.Seller
import com.example.xuyangtakeout.presenter.HomeFragmentPresenter
import com.example.xuyangtakeout.ui.adapter.HomeRvAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import javax.inject.Inject

class HomeFragment : Fragment() {
    lateinit var homeRvAdapter: HomeRvAdapter
    lateinit var rvHome: RecyclerView

    @Inject
    lateinit var homeFragmentPresenter: HomeFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = View.inflate(activity, R.layout.fragment_home, null)
        rvHome = view.find<RecyclerView>(R.id.rv_home)
        //布局管理器
        rvHome.layoutManager = LinearLayoutManager(activity) //从上到下的列表视图
        homeRvAdapter = HomeRvAdapter(activity)
        rvHome.adapter = homeRvAdapter


        //  homeFragmentPresenter = HomeFragmentPresenter(this)
        //TODO:解耦View层和P层，通过dagger2（基于注解的依赖注入）生成HomeFragmentPresenter
        DaggerHomeFragmentComponent.builder().homeFragmentModule(HomeFragmentModule(this)).build().inject(this)

        distance = 75.dp2x()
        return view
    }

    fun Int.dp2x(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), resources.displayMetrics
        ).toInt()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }


    val datas: ArrayList<String> = ArrayList<String>()
    var sum: Int = 0
    var distance: Int = 0
    var alpha = 55

    @SuppressLint("NewApi")
    private fun initData() {
//       for (i in 0 until 100) {
//            datas.add("我是商家：" + i)
//        }
        homeFragmentPresenter.getHomeInfo()
        //    homeRvAdapter.setData(datas)


    }


    val allList: ArrayList<Seller> = ArrayList()
    fun onHomeSuccess(nearbySellers: List<Seller>, otherSellers: List<Seller>) {
        allList.clear()
        allList.addAll(nearbySellers)
        allList.addAll(otherSellers)
        homeRvAdapter.setData(allList)

        //  toast("获取首页数据成功")


        //有数据可以滚动才可以监听滚动事件
        rvHome.setOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                sum += dy
                //  Log.e("home","sum :$sum")
                if (sum > distance) {
                    alpha = 255
                } else {
                    alpha = sum * 200 / distance
                    alpha += 55
                }
                ll_title_container.setBackgroundColor(Color.argb(alpha, 0x2f, 0x67, 0x9b))

            }


        })

    }

    fun onHomeFailed() {
        toast("获取首页数据失败")
    }


}
