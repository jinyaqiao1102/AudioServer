package com.example.testserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.app.Service;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

/*
 * @作者 JinYQ
 * @主要功能：实现了UDP服务端功能，并且通过AudioTrack 播放录音
 * @日期2014-12-31
 */
public class AudioService extends Service {
	//端口号
	public static final int SERVERPORT = 4444;
	private AudioTrack audio;
	private String ip;
	Thread myThread;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//初始化AudioTrack
		audio = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
				AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT,
				7104, AudioTrack.MODE_STREAM);
		audio.play();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//获取ip地址并且执行线程 哒哒哒
		ip = intent.getStringExtra("ip");
		if (!TextUtils.isEmpty(ip)) {
			myThread=new Thread(new Server());
			myThread.start();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	//UDP服务端接收音频数据并且播放
	public class Server implements Runnable {
		@Override
		public void run() {
			try {
				InetAddress serverAddr = InetAddress.getByName(ip);
				DatagramSocket socket = new DatagramSocket(SERVERPORT,
						serverAddr);
				while (true) {
					byte[] buf = new byte[7104];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					socket.receive(packet);
					audio.write(packet.getData(), 0, 7104);
					Log.e("======","getData()="+packet.getData());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		myThread=null;
		super.onDestroy();
	}
	
}
