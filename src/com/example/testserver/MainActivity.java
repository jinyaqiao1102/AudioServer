package com.example.testserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/*
 * @作者 JinYQ
 * @主要功能 启动Service，判断wifi状态
 * @日期 2014-12-31
 */
public class MainActivity extends Activity {
	private String ip;
	//端口号
	 public static final int SERVERPORT = 4444; 
	 private Button btn_start;
	 private Button btn_stop;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btn_start=(Button) this.findViewById(R.id.udp_btn);
		btn_stop=(Button) this.findViewById(R.id.udp_btn_stop);
		btn_stop.setEnabled(false);
		btn_start.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//开启服务
				Intent intent=new Intent(MainActivity.this,AudioService.class);
				intent.putExtra("ip", ip);
				startService(intent);
				btn_start.setEnabled(false);
				btn_stop.setEnabled(true);
			}
			
		});
		btn_stop.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//停止服务
				Intent intent=new Intent(MainActivity.this,AudioService.class);
				intent.putExtra("ip", ip);
				stopService(intent);
				btn_start.setEnabled(true);
				btn_stop.setEnabled(false);
			}
			
		});
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// 判断wifi是否开启
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		//获得ip地址
		int ipAddress = wifiInfo.getIpAddress();
		ip = intToIp(ipAddress);
		
		
	     
	}
	//转化ip地址
	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Intent intent=new Intent(MainActivity.this,AudioService.class);
		intent.putExtra("ip", ip);
		stopService(intent);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			new AlertDialog.Builder(this)
			.setTitle("提示")
			.setMessage("确定要退出吗？")
			.setNegativeButton("取消", null)
			.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							finish();
						}
					}).show();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
