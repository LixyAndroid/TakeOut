package com.example.xuyangtakeout.model.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.xuyangtakeout.model.bean.RecepitAddressBean
import com.example.xuyangtakeout.model.bean.User
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils

/**
 * Created by Li Xuyang
 * on 2019/5/19 18:54.
 */

/**
 *  app版本  数据库版本
 *  1.1         1       用户登录
 *  1.3         2       地址管理
 */

class TakeOutOpenHelper(val context: Context) : OrmLiteSqliteOpenHelper(context, "takeout_kotlin.db", null, 2) {
    override fun onCreate(db: SQLiteDatabase?, connectionSource: ConnectionSource?) {
        //创建user表
        TableUtils.createTable(connectionSource, User::class.java)

        //创建address表
        TableUtils.createTable(connectionSource, RecepitAddressBean::class.java)


    }

    override fun onUpgrade(db: SQLiteDatabase?, connectionSource: ConnectionSource?, oldversion: Int, newversion: Int) {
        //升级app的用户会执行此方法
        TableUtils.createTable(connectionSource, RecepitAddressBean::class.java)
    }


}