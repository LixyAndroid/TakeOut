package com.example.xuyangtakeout.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.widget.Toast

import java.util.ArrayList

/**
 * 判断手机号码是否合理
 *
 */
object SMSUtil {

    fun judgePhoneNums(activity: Activity, phoneNums: String): Boolean {
        if (isMatchLength(phoneNums, 11) && isMobileNO(phoneNums)) {
            return true
        }
        Toast.makeText(activity, "手机号码输入有误！", Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * 验证手机格式
     */
    fun isMobileNO(mobileNums: String): Boolean {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、178、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或7或8，其他位置的可以为0-9
		 */
        val telRegex = "[1][3578]\\d{9}"// "[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return if (TextUtils.isEmpty(mobileNums))
            false
        else
            mobileNums.matches(telRegex.toRegex())
    }


    /**
     * 判断一个字符串的位数
     *
     * @param str
     * @param length
     * @return
     */
    fun isMatchLength(str: String, length: Int): Boolean {
        return if (str.isEmpty()) {
            false
        } else {
            if (str.length == length) true else false
        }
    }

    /**
     * 权限校验
     * @param activity
     */
    fun checkPermission(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            val readPhone = activity.checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
            val receiveSms = activity.checkSelfPermission(Manifest.permission.RECEIVE_SMS)
            val readSms = activity.checkSelfPermission(Manifest.permission.READ_SMS)
            val readContacts = activity.checkSelfPermission(Manifest.permission.READ_CONTACTS)
            val readSdcard = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)

            var requestCode = 0
            val permissions = ArrayList<String>()
            if (readPhone != PackageManager.PERMISSION_GRANTED) {
                requestCode = requestCode or (1 shl 0)
                permissions.add(Manifest.permission.READ_PHONE_STATE)
            }
            if (receiveSms != PackageManager.PERMISSION_GRANTED) {
                requestCode = requestCode or (1 shl 1)
                permissions.add(Manifest.permission.RECEIVE_SMS)
            }
            if (readSms != PackageManager.PERMISSION_GRANTED) {
                requestCode = requestCode or (1 shl 2)
                permissions.add(Manifest.permission.READ_SMS)
            }
            if (readContacts != PackageManager.PERMISSION_GRANTED) {
                requestCode = requestCode or (1 shl 3)
                permissions.add(Manifest.permission.READ_CONTACTS)
            }
            if (readSdcard != PackageManager.PERMISSION_GRANTED) {
                requestCode = requestCode or (1 shl 4)
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (requestCode > 0) {
                val permission = arrayOfNulls<String>(permissions.size)
                activity.requestPermissions(permissions.toTypedArray(), requestCode)
                return
            }
        }
    }
}
