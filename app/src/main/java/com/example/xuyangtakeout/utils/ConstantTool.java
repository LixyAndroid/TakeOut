package com.example.xuyangtakeout.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ConstantTool {
	//Activity间的跳转方法 此方法用于没有数据传输的Activity中
	@SuppressWarnings("rawtypes")
	public static void toActivity(Context context,Class cla) {
		Intent intent = new Intent(context,cla);		//建立一个新的消息
		((Activity)context).startActivity(intent);		//执行Intent
		((Activity)context).finish();					//结束本界面
	}
	//Activity间的跳转方法 此方法用于有数据传输的Activity中
	@SuppressWarnings("rawtypes")
	public static void toActivity(Context context,Class cla,String[] keyArray,String[] valueArray ) {
		Intent intent = new Intent(context,cla);		//建立一个新的消息
		for(int i=0;i<keyArray.length;i++)
		{
			intent.putExtra(keyArray[i], valueArray[i]);//添加内容
		}
		((Activity)context).startActivity(intent);		//执行Intent
		((Activity)context).finish();					//结束本界面
	}
	//检查字符串是否为空
	public static boolean ifNull(String s) {
		if (s == null || s.equals("")) {
			return false;
		}
		return true;
	}
	
	//获得时间的函数
	public static String getTime(String time)
	{
		if(time.indexOf(" ")==-1)
		{
			return time;
		}
		String[] strs = time.split(" "); 
		return strs[3].substring(0,strs[3].length()-3);
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
