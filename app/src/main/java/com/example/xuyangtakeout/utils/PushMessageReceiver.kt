package com.example.xuyangtakeout.utils

import android.content.Context

import android.util.Log

import cn.jpush.android.api.CmdMessage
import cn.jpush.android.api.CustomMessage

import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver

class PushMessageReceiver : JPushMessageReceiver(){

    override fun onMessage(context: Context, customMessage: CustomMessage) {
        Log.e(TAG, "[onMessage] $customMessage")
    }

    override fun onNotifyMessageOpened(context: Context, message: NotificationMessage) {
        Log.e(TAG, "[onNotifyMessageOpened] $message")

    }

    override fun onNotifyMessageArrived(context: Context, message: NotificationMessage) {
        Log.e(TAG, "[onNotifyMessageArrived] $message")
    }

    override fun onNotifyMessageDismiss(context: Context?, message: NotificationMessage?) {
        Log.e(TAG, "[onNotifyMessageDismiss] " + message!!)
    }

    override fun onRegister(context: Context?, registrationId: String?) {
        Log.e(TAG, "[onRegister] " + registrationId!!)
    }

    override fun onConnected(context: Context?, isConnected: Boolean) {
        Log.e(TAG, "[onConnected] $isConnected")
    }

    override fun onCommandResult(context: Context?, cmdMessage: CmdMessage?) {
        Log.e(TAG, "[onCommandResult] " + cmdMessage!!)
    }

    companion object {
        private val TAG = "PushMessageReceiver"
    }


}
