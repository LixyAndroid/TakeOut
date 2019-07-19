package com.example.xuyangtakeout.ui.fragment

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.model.bean.GoodsInfo
import com.example.xuyangtakeout.model.bean.GoodsTypeInfo
import com.example.xuyangtakeout.presenter.GoodsFragmentPresenter
import com.example.xuyangtakeout.ui.activity.BusinessActivity
import com.example.xuyangtakeout.ui.adapter.GoodsAdapter
import com.example.xuyangtakeout.ui.adapter.GoodsTypeRvAdapter
import org.jetbrains.anko.find
import se.emilsjolander.stickylistheaders.StickyListHeadersListView
import javax.inject.Inject





/**
 * Created by Mloong
 * on 2019/5/20 20:45.
 */

class GoodsFragment : Fragment(){

    lateinit var rvGoodsType:RecyclerView



    lateinit var slhlv :StickyListHeadersListView

    lateinit var goodsAdapter: GoodsAdapter

    lateinit var goodsTypeAdapter: GoodsTypeRvAdapter

    lateinit var goodsFragmentPresenter: GoodsFragmentPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val goodsView = TextView(activity)
//        goodsView.text = "商品"
//        goodsView.gravity = Gravity.CENTER
//        goodsView.setTextColor(Color.BLACK)

        val goodsView = LayoutInflater.from(activity).inflate(R.layout.fragment_goods,container,false)
        rvGoodsType = goodsView.find(R.id.rv_goods_type)

        slhlv = goodsView.find<StickyListHeadersListView>(R.id.slhlv)
        goodsAdapter = GoodsAdapter(activity,this)
        slhlv.adapter = goodsAdapter
      //  goodsTypeAdapter =GoodsTypeRvAdapter(activity,this)
        rvGoodsType.layoutManager = LinearLayoutManager(activity) as RecyclerView.LayoutManager
        goodsTypeAdapter =GoodsTypeRvAdapter(activity,this)
        rvGoodsType.adapter  = goodsTypeAdapter
        goodsFragmentPresenter= GoodsFragmentPresenter(this)

        return goodsView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        goodsFragmentPresenter.getBusinessInfo((activity as BusinessActivity).seller.id.toString())

    }

    fun onLoadBusinessSucess(goodsTypeList: List<GoodsTypeInfo>,allTypeGoodsList: ArrayList<GoodsInfo>) {
        goodsTypeAdapter.setDatas(goodsTypeList) //左侧刷新

        //右侧列表
        goodsAdapter.setDatas(allTypeGoodsList)

        slhlv.setOnScrollListener(object :AbsListView.OnScrollListener{

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int,
                                  visibleItemCount: Int, totalItemCount: Int) {

                //现在出旧类别
                val oldPosition = goodsTypeAdapter.selectPosition


                val newTypeId = goodsFragmentPresenter.allTypeGoodsList.get(firstVisibleItem).typeId
                //把新的id找到它对应的position
                val newPosition = goodsFragmentPresenter.getTypePositionByTypeId(newTypeId)

                //当newTypeId与旧的不同时，就要切换类别了
                if (newPosition != oldPosition){

                    goodsTypeAdapter.selectPosition = newPosition
                    goodsTypeAdapter.notifyDataSetChanged()
                }

            }

            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {

            }

        })


    }
}