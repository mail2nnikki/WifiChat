package com.nikhil.WifiChat;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	Button profile;
	Button scan;
	Button chat;
	
	static String receiverAddr = null;
	
	Intent intent, serverIntent;
	
	AddressUtility au = new AddressUtility();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		profile = (Button) findViewById(R.id.Button01);
		scan = (Button) findViewById(R.id.Button02);
		chat = (Button) findViewById(R.id.Button03);
		
		intent = new Intent(this, WifiChatActivity.class);
		serverIntent = new Intent(this, ChatService.class);
		
        serverIntent = new Intent(this, ChatService.class);
		registerReceiver(broadcastReceiver, new IntentFilter(ChatService.BROADCAST_ACTION));
        startService(new Intent(getApplicationContext(), ChatService.class));
		
		profile.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog();
			}
		});
		
		scan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				au.scanAddresses(MainActivity.this);
			}
		});
		
		chat.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(intent);
			}
		});
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.RoutingTable:
			break;
		case R.id.Preferences:
			startActivity(new Intent(MainActivity.this, Preferences.class));
			break;
		}
		return false;
	}
	
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        	Log.v("msg", "received");
        	String message = intent.getStringExtra("message");
        	String code = message.substring(0, 3);
        	String msg = null;
        	if(message.length()>3) msg = message.substring(3, message.length());
        	if(code.equals("idf"))
        	{
        		 if(receiverAddr != null) 
        	     {
        			 Log.v("idf", "received");
            		 WifiChatActivity.addUser(msg, intent.getStringExtra("senderAddr"));
    				 Toast toast = Toast.makeText(MainActivity.this, msg+" entered chat room", 1000);
    				 toast.show();
        			 sendMessage(receiverAddr, "irf"+WifiChatActivity.userName, false);
        	     }
        	}
        	else if(code.equals("irf"))
        	{
        		 Log.v("irf", "received");
        		 WifiChatActivity.addUser(msg, intent.getStringExtra("senderAddr"));
				 Toast toast = Toast.makeText(MainActivity.this, msg+" entered chat room", 1000);
				 toast.show();
        		 Log.v("user added with name and address", msg + " " + intent.getStringExtra("senderAddr"));
        	}
        }
    };
    
    private void sendMessage(String address, String msg, boolean bcast) {
		ClientThread cThread;
		try {
			cThread = new ClientThread(address, 5555, msg, bcast);
			cThread.start();
		} catch(IOException e) {
			e.printStackTrace();
		}
    }
	
    public void onResume() {
    	super.onResume();
		registerReceiver(broadcastReceiver, new IntentFilter(ChatService.BROADCAST_ACTION));
    }
    
    public void onPause() {
    	super.onPause();
    	unregisterReceiver(broadcastReceiver);
    	} 
	
	void showDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Profile Info");
		alert.setMessage("Nick Name");

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		WifiChatActivity.userName = input.getText().toString();
		 }
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		 public void onClick(DialogInterface dialog, int whichButton) {
		     // Canceled.
		}
		});

		 alert.show();
	}

}
