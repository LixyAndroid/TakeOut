package com.example.xuyangtakeout.model.bean

class Order {
    var id: String? = null
    var type: String? = null
    var seller: Seller? = null
    var rider: Rider? = null
    var goodsInfos: List<GoodsInfo>? = null
    var distribution: Distribution? = null
    var detail: OrderDetail? = null

    inner class Rider {
        var id: Int = 0
        var name: String? = null
        var phone: String? = null
        // public Location location;
    }

    inner class OrderDetail {
        var username: String? = null
        var phone: String? = null
        var address: String? = null
        var pay: String? = null
        var time: String? = null
    }

    inner class Distribution {
        // 配送方式
        var type: String? = null
        // 配送说明
        var des: String? = null
    }

}
