package com.example.xuyangtakeout.model.dao

import android.content.Context
import android.util.Log
import com.example.xuyangtakeout.model.bean.RecepitAddressBean
import com.j256.ormlite.dao.Dao
import java.lang.Exception

/**
 * Created by Li Xuyang
 * on 2019/5/22 23:00
 */
class AddressDao(context: Context) {
     var addressDao: Dao<RecepitAddressBean, Int>

    init {
        val openHelper = TakeOutOpenHelper(context)
        addressDao = openHelper.getDao(RecepitAddressBean::class.java)
    }

    fun addRecepitAddressBean(bean: RecepitAddressBean) {

        try {
            addressDao.create(bean)
        } catch (e: Exception) {
            Log.e("address", e.localizedMessage)
        }


    }

    fun deleteRecepitAddressBean(bean: RecepitAddressBean) {

        try {
            addressDao.delete(bean)
        } catch (e: Exception) {
            Log.e("address", e.localizedMessage)
        }


    }

    fun updateRecepitAddressBean(bean: RecepitAddressBean) {

        try {
            addressDao.update(bean)
        } catch (e: Exception) {
            Log.e("address", e.localizedMessage)
        }


    }

    fun queryAllAddress(): List<RecepitAddressBean> {

        try {
            return addressDao.queryForAll()
        } catch (e: Exception) {
            Log.e("address", e.localizedMessage)
            return ArrayList<RecepitAddressBean>()
        }


    }

}