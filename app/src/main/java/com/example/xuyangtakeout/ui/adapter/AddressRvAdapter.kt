package com.example.xuyangtakeout.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.model.bean.RecepitAddressBean
import com.example.xuyangtakeout.ui.activity.AddOrEditAddressActivity

/**
 * Created by Li Xuyang
 * on 2019/5/23 11:03
 */
class AddressRvAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var addressList = arrayListOf<RecepitAddressBean>()
    fun setAddList(list: ArrayList<RecepitAddressBean>) {
        this.addressList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AddressItemHolder).bindData(addressList.get(position))
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_receipt_address, parent, false)
        return AddressItemHolder(itemView)
    }

    inner class AddressItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivEdit: ImageView
        val tvname: TextView
        val tv_sex: TextView
        val tv_phone: TextView
        val tv_label: TextView
        val tv_address: TextView
        lateinit var address: RecepitAddressBean

        init {
            ivEdit = itemView.findViewById(R.id.iv_edit) as ImageView
            ivEdit.setOnClickListener {
                val intent = Intent(context, AddOrEditAddressActivity::class.java)
                intent.putExtra("addressBean", address)
                context.startActivity(intent)
            }
            tvname = itemView.findViewById(R.id.tv_name) as TextView
            tv_sex = itemView.findViewById(R.id.tv_sex) as TextView
            tv_phone = itemView.findViewById(R.id.tv_phone) as TextView
            tv_label = itemView.findViewById(R.id.tv_label) as TextView
            tv_address = itemView.findViewById(R.id.tv_address) as TextView

            //给条目设置点击事件
            itemView.setOnClickListener {
                val data = Intent()
                data.putExtra("address", address)
                (context as Activity).setResult(200, data)
                (context as Activity).finish()
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindData(address: RecepitAddressBean) {
            this.address = address
            tvname.text = address.username
            tv_sex.text = address.sex
            tv_phone.text = address.phone + "," + address.phoneOther
            tv_address.text = "${address.address},${address.detailAddress}"
            tv_label.text = address.label
        }
    }


}