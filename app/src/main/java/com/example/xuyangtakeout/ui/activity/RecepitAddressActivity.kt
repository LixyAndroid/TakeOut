package com.example.xuyangtakeout.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.model.bean.RecepitAddressBean
import com.example.xuyangtakeout.model.dao.AddressDao
import com.example.xuyangtakeout.ui.adapter.AddressRvAdapter
import com.example.xuyangtakeout.ui.views.RecycleViewDivider
import com.example.xuyangtakeout.utils.BottomNavigation
import kotlinx.android.synthetic.main.activity_address_list.*


/**
 * Created by Mloong
 * on 2019/5/22 21:20
 */
class RecepitAddressActivity() : AppCompatActivity() {

    lateinit var addressDao: AddressDao
    lateinit var adapter: AddressRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        ib_back.setOnClickListener { finish() }
        addressDao = AddressDao(this)
        rv_receipt_address.layoutManager = LinearLayoutManager(this)
        rv_receipt_address.addItemDecoration(RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL))
        adapter = AddressRvAdapter(this)
        rv_receipt_address.adapter = adapter
        // rv_receipt_address.adapter = AddressRvAdapter(this)


        //底部导航适配,HuaWeiP20

        if (BottomNavigation.checkDeviceHasNavigationBar(this)) {
            BottomNavigation.assistActivity(findViewById(android.R.id.content));
        }

        tv_add_address.setOnClickListener {
            val intent: Intent = Intent(this, AddOrEditAddressActivity::class.java)

            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()

        val addressList = addressDao.queryAllAddress()
        if (addressList.isNotEmpty()) {
            //          toast("一共有${addressList.size} 个地址")

            //      (rv_receipt_address.adapter as AddressRvAdapter).addressList = addressList as ArrayList<RecepitAddressBean>
            adapter.setAddList(addressList as ArrayList<RecepitAddressBean>)
        }

    }




}





