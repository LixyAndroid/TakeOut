package com.example.xuyangtakeout.ui.activity


import android.annotation.SuppressLint
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.example.xuyangtakeout.presenter.LoginActivityPresenter
import com.example.xuyangtakeout.utils.SMSUtil
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast


/**
 * Created by Li Xuyang
 * on 2019/5/19 16:06.
 */


class LoginActivity : AppCompatActivity() {


    val eventHandler = object : EventHandler() {
        override fun afterEvent(event: Int, result: Int, data: Any) {
            // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
            val msg = Message()
            msg.arg1 = event
            msg.arg2 = result
            msg.obj = data
            Handler(Looper.getMainLooper(), object : Handler.Callback {
                override fun handleMessage(msg: Message): Boolean {
                    val event = msg.arg1
                    val result = msg.arg2
                    val data = msg.obj
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理成功得到验证码的结果
                            // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                            // Log.e("sms", "获取验证码成功")
                            toast("获取验证码成功")
                        } else {
                            // TODO 处理错误的结果
                            (data as Throwable).printStackTrace()
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            // TODO 处理验证码验证通过的结果
                            toast("提交验证码成功")

                            //登录外卖服务器
                            val phone = et_user_phone.text.toString().trim()
                            loginActivityPresenter.loginByPhone(phone)

                        } else {
                            // TODO 处理错误的结果
                            (data as Throwable).printStackTrace()
                            toast("验证码错误")
                        }
                    }
                    // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                    return false
                }
            }).sendMessage(msg)
        }
    }

    lateinit var loginActivityPresenter: LoginActivityPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.xuyangtakeout.R.layout.activity_login)


        loginActivityPresenter = LoginActivityPresenter(this)

        initListener()

        // 注册一个事件回调，用于处理SMSSDK接口请求的结果
        SMSSDK.registerEventHandler(eventHandler)

    }


    // 使用完EventHandler需注销，否则可能出现内存泄漏
    override fun onDestroy() {
        super.onDestroy()
        SMSSDK.unregisterEventHandler(eventHandler)
    }


    private fun initListener() {
        iv_user_back.setOnClickListener { finish() }

        tv_user_code.setOnClickListener {
            //获取验证码
            val phone = et_user_phone.text.toString().trim()
            //验证手机号

            if (SMSUtil.judgePhoneNums(this, phone)) {
                SMSSDK.getVerificationCode("86", phone)

            }

            //开启倒计时
            tv_user_code.isEnabled = false
            Thread(CutDownTask()).start()

        }

        iv_login.setOnClickListener {
            //提交验证码
            val phone = et_user_phone.text.toString().trim()
            val code = et_user_code.text.toString().trim()
            if (SMSUtil.judgePhoneNums(this, phone) && !TextUtils.isEmpty(code)) {
                SMSSDK.submitVerificationCode("86", phone, code)

            }

            //    loginActivityPresenter.loginByPhone(phone)
        }
    }

    fun onLoginSuccess() {
        finish()
        toast("登录成功")
    }

    fun onLoginFailed() {
        toast("登录失败")

    }

    companion object {
        val TIME_MINUS = -1
        val TIME_IS_OUT = 0
    }

    val handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg!!.what) {
                TIME_MINUS -> tv_user_code.text = "剩余时间(${time})秒"
                TIME_IS_OUT -> {
                    tv_user_code.isEnabled = true
                    tv_user_code.text = "点击重发"
                    time = 60
                }
            }

        }
    }

    var time = 60

    inner class CutDownTask : Runnable {
        override fun run() {
            while (time > 0) {
                //刷新剩余时间，当前子线程，使用handler
                handler.sendEmptyMessage(TIME_MINUS)
                SystemClock.sleep(999)
                time--
            }
            handler.sendEmptyMessage(TIME_IS_OUT)
        }
    }
}