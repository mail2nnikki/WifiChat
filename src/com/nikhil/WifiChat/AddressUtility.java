package com.nikhil.WifiChat;

import java.net.InetAddress;
import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;
import android.net.wifi.*;

public class AddressUtility {
	
	public static String domain = "192.168.2.";
	
	public static ArrayList<String> addresses = new ArrayList();
	
	public static String myAddress;
	
	WifiManager wm;
	
	WifiInfo wi;
	
	ProgressDialog progressDialog;
	
	int progress;
	
	private Handler progressbarHandler = new Handler();
	
	String getdomain() {
		return this.domain;
	}
	
	void setdomain(Context context) {
		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wi = (WifiInfo) wm.getConnectionInfo();
		int addr = wi.getIpAddress();
  	    domain = String.format("%d.%d.%d.",
			  (addr & 0xff),
			  (addr >> 8 & 0xff),
			  (addr >> 16 & 0xff));
  	    myAddress = String.format("%d.%d.%d.%d",
			  (addr & 0xff),
			  (addr >> 8 & 0xff),
			  (addr >> 16 & 0xff),
			  (addr >> 24 & 0xff));
  	    Log.v("address is", domain);
	}
	
	ArrayList<String> scanAddresses(Context context) {
		
        WifiChatActivity.users.clear();
        WifiChatActivity.userAddress.clear();
		
		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Scanning Users");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setProgress(0);
		progressDialog.setMax(255);
		progressDialog.show();
		
    	new Thread(new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
			InetAddress in;
	    	try {
	    		for(int i=1;i<256;i++)
	    		{
	    			in = InetAddress.getByName(domain + Integer.toString(i));
	    			if(in.isReachable(50))
	    			{
	    				if(!addresses.contains(domain + Integer.toString(i))) { 
	    					addresses.add(domain + Integer.toString(i));
	    					Log.v("address found", domain + Integer.toString(i));
	    				}
	    			}
	    			
	    			progress = i;
	    			
	    			progressbarHandler.post(new Runnable() {

						public void run() {
							progressDialog.setProgress(progress);
						}
	    				
	    			});
	    		}
	    		progressDialog.dismiss();
	    	} catch (Exception e) {}
		}
    	}).start();
    	
		return addresses;
	}

}
