package com.example.xuyangtakeout.model.bean

import android.content.pm.ActivityInfo
import java.io.Serializable

import java.util.ArrayList

data class Seller(var id: Long, var pic: String, var name: String, var score: String, var sale: String,
                  var ensure: String, var invoice: String, var sendPrice: String, var deliveryFee: String,
                  var recentVisit: String, var distance: String, var time: String, var icon: String,
                  var activityList: ArrayList<ActivityInfo>):Serializable {



}