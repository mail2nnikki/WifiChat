package com.nikhil.WifiChat;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class WifiChatActivity extends Activity {
    /** Called when the activity is first created. */
	Button but, selectReceiver;
	TextView textBox, chatArea;
	ScrollView scrollView;
	Intent intent;
	View root;
	AddressUtility addressUtility = new AddressUtility();
	static ArrayList<CharSequence> users = new ArrayList();
	static ArrayList<String> userAddress = new ArrayList();
	static String userName = "Random";
	int receiverInd = -1;
	static String receiverAddr = null;
	static String senderAddr = null;
	String selectedReceiver = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        addressUtility.setdomain(this);
        but = (Button) findViewById(R.id.Button);
        selectReceiver = (Button) findViewById(R.id.setUser);
        textBox = (TextView) findViewById(R.id.TextBox);
        chatArea = (TextView) findViewById(R.id.ChatArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        
        chatArea.setMovementMethod(new ScrollingMovementMethod());
        
        intent = new Intent(this, ChatService.class);
        registerReceiver(broadcastReceiver, new IntentFilter(ChatService.BROADCAST_ACTION));
        startService(new Intent(getApplicationContext(), ChatService.class));
        
        but.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(!textBox.getText().equals("")) {
					if(selectedReceiver!=null)
					{
					      chatArea.append(Html.fromHtml("<font color='black' style='bold'>"+userName+": </font>"));
					      chatArea.append(Html.fromHtml("<font color='black'>"+textBox.getText()+"</font> <br/>"));
					      sendMessage(selectedReceiver, "msg"+userName+": "+textBox.getText().toString(), false);
					      textBox.setText("");
					}
					else
					{
						Toast toast = Toast.makeText(WifiChatActivity.this, "Please choose a Recepient", 1000);
						toast.show();
					}
				}
			}
        	
        });
        
        selectReceiver.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CharSequence[] temp = new CharSequence[users.size()];
	        	for(int i=0;i<users.size();i++) temp[i] = users.get(i);
				final AlertDialog.Builder builder = new AlertDialog.Builder(WifiChatActivity.this);
				builder.setTitle("Choose Recepient");
		        builder.setSingleChoiceItems(temp, receiverInd, new DialogInterface.OnClickListener(){
		            public void onClick(DialogInterface dialog, int which) {
		            	receiverInd = which;
		            	selectedReceiver = userAddress.get(which);
		            	Log.v("receiver addr set to", selectedReceiver);
		            	dialog.dismiss();
		            }
		         });
		        
		        AlertDialog alert = builder.create();
		        alert.show();
			}
        	
        });        
        
        setUsers();
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
			startActivity(new Intent(WifiChatActivity.this, Preferences.class));
			break;
		}
		return false;
	}
    
    private void setUsers() {
    	final Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(250);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				sendMessage("255.255.255.255", "idf"+userName, true);
			}
    		
    	});
    	
    	t.start();

    }
    
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
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
        			 addUser(msg, intent.getStringExtra("senderAddr"));
					 Toast toast = Toast.makeText(WifiChatActivity.this, msg+" entered chat room", 1000);
					 toast.show();
        			 sendMessage(receiverAddr, "irf"+userName, false);
        	     }
        	}
        	else if(code.equals("irf"))
        	{
        		 addUser(msg, intent.getStringExtra("senderAddr"));
				 Toast toast = Toast.makeText(WifiChatActivity.this, msg+" entered chat room", 1000);
				 toast.show();
        		 Log.v("user added with name and address", msg + " " + intent.getStringExtra("senderAddr"));
        	}
        	else if(code.equals("msg"))
        	{
        		 int temp = 0;
        		 while(msg.charAt(temp)!=' ') temp++;
        		 String sender;
        		 String message1;
        		 sender = msg.substring(0, temp);
        		 message1 = msg.substring(temp, msg.length());
        	     
        		 chatArea.append(Html.fromHtml("<font color='red'>"+sender+" </font>"));
        	     chatArea.append(Html.fromHtml("<font color='black'>"+message1+"</font> <br/>"));
        	     
        	     scrollView.post(new Runnable() {
        	    	public void run() {
        	    		scrollView.fullScroll(View.FOCUS_DOWN);
        	    	}
        	     });
        	     
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
    
    public static void addUser(String user, String addr) {
    	for(int i=0;i<userAddress.size();i++) if(userAddress.get(i).equals(addr)) return;
    	users.add(user);
    	userAddress.add(addr);
    }
    
    public void onResume() {
    	super.onResume();
    	registerReceiver(broadcastReceiver, new IntentFilter(ChatService.BROADCAST_ACTION));
    }
    
    public void onPause() {
    	super.onPause();
    	unregisterReceiver(broadcastReceiver);
    } 
    
    public void onDestroy() {
    	super.onDestroy();
    }
    
}