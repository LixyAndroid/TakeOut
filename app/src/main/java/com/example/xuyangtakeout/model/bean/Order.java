package com.example.xuyangtakeout.model.bean;

import java.util.List;

public class Order {
	public String id;
	public String type;
	public Seller seller;
	public Rider rider;
	public List<GoodsInfo> goodsInfos;
	public Distribution distribution;
	public OrderDetail detail;

	public class Rider {
		public int id;
		public String name;
		public String phone;
		// public Location location;
	}

	public class OrderDetail {
		public String username;
		public String phone;
		public String address;
		public String pay;
		public String time;
	}

	public class Distribution {
		// 配送方式
		public String type;
		// 配送说明
		public String des;
	}

}
