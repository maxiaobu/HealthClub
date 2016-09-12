package com.maxiaobu.healthclub.utils.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.maxiaobu.healthclub.ui.activity.ChatActivity;
import com.maxiaobu.healthclub.utils.storage.SPUtils;


@SuppressLint("NewApi")
public class BaseJsToAndroid {
	private Context context;

	public BaseJsToAndroid(Context context) {
		this.context = context;
	}

	/**
	 * 任何一页控制
	 * 
	 * @param fromPage
	 * @param toPage
	 * @param bottom
	 *            底栏
	 */
	@JavascriptInterface
	public void popNewWindow(String page) {
		/*Log.i("page", page);
		int endIndex=page.indexOf("?");
		String h5=page.substring(0, endIndex);
		if (page.indexOf("file:///android_asset/") < 0)
			page = "file:///android_asset/" + page;
		if (MainActivity.instance.currentPage == 601||h5.equals("dynReview.html")) {
			Intent intent = new Intent(context, FindActivity.class);
			intent.putExtra("url", page);
			context.startActivity(intent);
		} else {
			//TODO
			if (h5.equals("pay.html")) {
				MainActivity.instance.setTabSelection(505);
				SharedPreferencesUtils.setParam(context, "paypage", page);
			}else{
				MainActivity.instance.setTabWebViewSelection(page);
			}
		}*/
		Toast.makeText(context, "web任何一页控制", Toast.LENGTH_SHORT).show();
	}

	/**
	 * ?tarid=M000021&role=coach&tabpage=course&memid=M000003&memphoto=http://
	 * efithealthresource
	 * .oss-cn-beijing.aliyuncs.com/image/bmember/M000021_1461571239735_s
	 * .jpg&memnickname=羿健康男教练
	 * 
	 * 个人信息 "?tarid=" +memid +"&role="+memrole+"&memid="+memid
	 * 
	 * @param tarid
	 * @param memrole
	 * @param memid
	 */
	@JavascriptInterface
	public void memberIndex(String page) {
		/*if (page != null && page.length() > 0) {
			SharedPreferencesUtils.setParam(context, "page", page);
			Log.i("djy", page);
		}
//			Intent intent=new Intent(context,MemberCluMenubActivity.class);
//			intent.putExtra("url", page);
//			context.startActivity(intent);
		MainActivity.instance.setTabSelection(403);*/
		Toast.makeText(context, "web羿健康男教练", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 获得当前登录memid
	 * 
	 * @return
	 */
	@JavascriptInterface
	public String getmemid() {
		String memid =SPUtils.getString("memid", "");
		return memid;
	}

	/**
	 * HTML 页面提示
	 * 
	 * @param content
	 */
	@JavascriptInterface
	public void alertInfo(String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 提示信息
	 * 
	 * @param type
	 *            提示类型:success成功，error错误，warn警告, message(无文本)
	 * @param message
	 */
	@JavascriptInterface
	public void alertInfo(String type, String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 聊天
	 * 
	 * @param memid
	 * @param nickname
	 * @param imgpath
	 */
	@JavascriptInterface
	public void toChat(String memid, String nickname, String imgpath) {
		Intent intent = new Intent(context, ChatActivity.class);
		intent.putExtra("userId", memid.toLowerCase());
		intent.putExtra("nickname", nickname);
		intent.putExtra("headimg", imgpath);
		context.startActivity(intent);
	}

	/**
	 * 打电话
	 * 
	 * @param phoneNumber
	 *            电话号码
	 */
	@SuppressWarnings("deprecation")
	@JavascriptInterface
	public void callUp(final String phoneNumber) {
		/*MyAlertDiaLog builder = new MyAlertDiaLog(context, phoneNumber);
		builder.setMessage("您确定呼叫 " + phoneNumber + " 这个电话号码?\n");
		builder.setButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}
		});

		builder.setButton2("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent phoneIntent = new Intent("android.intent.action.CALL",
						Uri.parse("tel:" + phoneNumber));
				// 启动
				context.startActivity(phoneIntent);

			}
		});
		builder.show();*/
		Toast.makeText(context, "web打电话", Toast.LENGTH_SHORT).show();

	}

	/**
	 * 退出当前登录
	 * 
	 * @param str
	 */
	@JavascriptInterface
	public void logout() {
//		context.startActivity(new Intent(context, ExitConfimActivity.class));
		Toast.makeText(context, "web退出当前登录", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 生成二维码
	 * 
	 * @param memid
	 */
	@JavascriptInterface
	public void getMyQrCode() {
		/*Intent intent = new Intent();
		intent.setClass(context, QRCodeActivity.class);
		context.startActivity(intent);*/
		Toast.makeText(context, "web生成二维码", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 课程管理
	 * 
	 * @param memid
	 */
	@JavascriptInterface
	public void courseManager(String memid) {
//		MainActivity.instance.setTabSelection(100);
		Toast.makeText(context, "web.课程管理", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 订单类型
	 */
	@JavascriptInterface
	public void getOrderList() {
//		MainActivity.instance.setTabSelection(402);
		Toast.makeText(context, "web订单类型", Toast.LENGTH_SHORT).show();

	}
}
