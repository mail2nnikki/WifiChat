package com.nikhil.WifiChat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class ChatService extends Service {
	
	public static String serverIP;
	
	public static int serverPort = 5555;
	
	private ServerSocket serverSocket;
	
	private serverThread sThread;
	
    public static final String BROADCAST_ACTION = "UpdateEvent";
    
    public static DatagramSocket portsocket;
    
    Intent intent;
	
	int i = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	public void onCreate() {
		try {
			portsocket = new DatagramSocket(5555);
			sThread = new serverThread();
			sThread.start();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class serverThread extends Thread {

		public void run() {
			try {
				  serverIP = getLocalIpAddress();
				  Log.v("addr", serverIP);
		      	  if (serverIP != null) {
		      	 	 serverSocket = new ServerSocket(serverPort);
		      	 	 Log.v("socket", "created");
		      	 	 while(true) {	        	 		 
		      	 		 /*Socket client = serverSocket.accept();
		      	 		 Log.v("client", "received");*/
		      	 		 try {
		      		 		byte[] buf = new byte[1024];
		      		 		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		      		 		ChatService.portsocket.receive(packet);
		      		 		byte[] result = new byte[packet.getLength()];
		      		 		System.arraycopy(packet.getData(), 0, result, 0, packet.getLength());
		      		 		String msg = new String(result);
		      		 		updateGui(msg, packet.getAddress());
		      		 		
		      	 		 } catch (Exception e) { }
		      	 	 }
		      	  } 
		       } catch (Exception e) {}
		}
		
	}
	
	private void updateGui(String msg, InetAddress addr) {
		
		intent = new Intent(BROADCAST_ACTION);
		intent.putExtra("message", msg);
		intent.putExtra("senderAddr", addr.toString().substring(1, addr.toString().length()));
		if(msg.substring(0, 3).equals("idf"))
		{
			WifiChatActivity.receiverAddr = addr.toString().substring(1, addr.toString().length());
			MainActivity.receiverAddr = addr.toString().substring(1, addr.toString().length());
		}
		sendBroadcast(intent);
	}
	
    private String getLocalIpAddress() {
        try {
	            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	                NetworkInterface intf = en.nextElement();
	                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                    InetAddress inetAddress = enumIpAddr.nextElement();
	                    if (!inetAddress.isLoopbackAddress()) { return inetAddress.getHostAddress().toString(); }
	                }
	            }
	        } catch (SocketException ex) {
	            Log.e("ServerActivity", ex.toString());
	        }
	        return null;
   }
    
   public void onDestroy() {
   } 

}
