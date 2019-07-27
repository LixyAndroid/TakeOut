package com.example.xuyangtakeout.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.example.xuyangtakeout.R
import com.example.xuyangtakeout.utils.CommonUtil
import kotlinx.android.synthetic.main.activity_add_edit_receipt_address.*
import android.widget.Toast
import com.example.xuyangtakeout.model.bean.RecepitAddressBean
import com.example.xuyangtakeout.model.dao.AddressDao
import com.j256.ormlite.dao.Dao
import org.jetbrains.anko.toast


/**
 * Created by Mloong
 * on 2019/5/22 21:33
 */
class AddOrEditAddressActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.ib_back -> finish()
            R.id.ib_add_phone_other -> rl_phone_other.visibility = View.VISIBLE
            R.id.ib_delete_phone -> et_phone.setText("")
            R.id.ib_delete_phone_other -> et_phone_other.setText("")
            R.id.ib_select_label -> selectLabel()
            R.id.btn_ok -> {
                val isOK = checkReceiptAddressInfo()
                if (isOK) {

                    if (intent.hasExtra("addressBean")) {
                        updateAddress()


                    } else {

                        //新增地址
                        insertAddress()
                    }
                }

            }
            R.id.btn_location_address -> {
                val intent = Intent(this, MapLocationActivity::class.java)
                startActivityForResult(intent, 1001)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 200) {
            if (data != null) {
                val title = data.getStringExtra("title")
                val address = data.getStringExtra("address")
                et_receipt_address.setText(title)
                et_detail_address.setText(address)
            }
        }

    }

    private fun updateAddress() {

        val username = et_name.text.toString().trim()
        var sex = "女士"
        if (rb_man.isChecked) {
            sex = "先生"
        }
        var phone = et_phone.text.toString().trim()
        var phoneOther = et_phone_other.text.toString().trim()
        var adress = et_receipt_address.text.toString().trim()
        var datailAdress = et_detail_address.text.toString().trim()
        var label = tv_label.text.toString().trim()
        addressBean.username = username
        addressBean.sex = sex
        addressBean.phone = phone
        addressBean.phoneOther = phoneOther
        addressBean.address = adress
        addressBean.detailAddress = datailAdress
        addressBean.label = label
        addressDao.updateRecepitAddressBean(addressBean)
        toast("更新地址成功")
        finish()

    }

    private fun insertAddress() {
        val username = et_name.text.toString().trim()
        var sex = "女士"
        if (rb_man.isChecked) {
            sex = "先生"
        }
        var phone = et_phone.text.toString().trim()
        var phoneOther = et_phone_other.text.toString().trim()
        var adress = et_receipt_address.text.toString().trim()
        var datailAdress = et_detail_address.text.toString().trim()
        var label = tv_label.text.toString().trim()
        addressDao.addRecepitAddressBean(
            RecepitAddressBean(
                999,
                username,
                sex,
                phone,
                phoneOther,
                adress,
                datailAdress,
                label,
                "38"
            )
        )
        toast("新增地址成功")
        finish()
    }

    lateinit var addressDao: AddressDao
    val titles = arrayOf(" 无 ", " 家 ", " 学校 ", " 公司 ")
    val colors = arrayOf("#552273", "#11ff77", "#ff3611", "#ff1152")
    private fun selectLabel() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("请选择地址标签")
        builder.setItems(titles, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                tv_label.text = titles[which].toString()
                tv_label.setBackgroundColor(Color.parseColor(colors[which]))
                tv_label.setTextColor(Color.BLACK)

            }

        })
        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_receipt_address)
        processIntent()

        addressDao = AddressDao(this)
        //判断设备是否有虚拟按键，如果有增加paddingBottom = 50dp
        if (CommonUtil.checkDeviceHasNavigationBar(this)) {
            // val x = getNavigationBarHeight(this)
            activity_add_address.setPadding(0, 0, 0, 48.dip2px())
        }

        btn_location_address.setOnClickListener(this)

        ib_back.setOnClickListener(this)
        ib_add_phone_other.setOnClickListener(this)
        ib_delete_phone.setOnClickListener(this)
        ib_delete_phone_other.setOnClickListener(this)
        ib_select_label.setOnClickListener(this)
        btn_ok.setOnClickListener(this)

        et_phone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (!TextUtils.isEmpty(s)) {
                    ib_delete_phone.visibility = View.VISIBLE
                } else {
                    ib_delete_phone.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        et_phone_other.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (!TextUtils.isEmpty(s)) {
                    ib_delete_phone_other.visibility = View.VISIBLE
                } else {
                    ib_delete_phone_other.visibility = View.GONE
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

    }

    lateinit var addressBean: RecepitAddressBean
    private fun processIntent() {
        if (intent.hasExtra("addressBean")) {
            addressBean = intent.getSerializableExtra("addressBean") as RecepitAddressBean

            tv_title.text = "修改地址"

            ib_delete.visibility = View.VISIBLE
            ib_delete.setOnClickListener {
                addressDao.deleteRecepitAddressBean(addressBean)
                toast("删除地址成功,若需要请重新创建")
                finish()
            }
            et_name.setText(addressBean.username)
            val sex = addressBean.sex
            if ("先生".equals(sex)) {
                rb_man.isChecked = true
            } else {
                rb_women.isChecked = true
            }
            et_phone.setText(addressBean.phone)
            et_phone_other.setText(addressBean.phoneOther)
            et_receipt_address.setText(addressBean.address)
            et_detail_address.setText(addressBean.detailAddress)
            tv_label.text = addressBean.label

        }
    }

    fun Int.dip2px(): Int {
        val scale = resources.displayMetrics.density
        return (toFloat() * scale + 0.5f).toInt()
    }

    fun checkReceiptAddressInfo(): Boolean {
        val name = et_name.getText().toString().trim()
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请填写联系人", Toast.LENGTH_SHORT).show()
            return false
        }
        val phone = et_phone.getText().toString().trim()
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请填写手机号码", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!isMobileNO(phone)) {
            Toast.makeText(this, "请填写合法的手机号", Toast.LENGTH_SHORT).show()
            return false
        }
        val receiptAddress = et_receipt_address.getText().toString().trim()
        if (TextUtils.isEmpty(receiptAddress)) {
            Toast.makeText(this, "请填写收获地址", Toast.LENGTH_SHORT).show()
            return false
        }
        val address = et_detail_address.getText().toString().trim()
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "请填写详细地址", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun isMobileNO(phone: String): Boolean {
        val telRegex = "[1][3578]\\d{9}"//"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return phone.matches(telRegex.toRegex())
    }


}