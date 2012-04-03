package com.nikhil.WifiChat;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

public class Preferences extends PreferenceActivity {
	
	Preference UniqueID = null;
	Preference BroadcastID = null;
	Preference AllowList = null;
	Preference BlockList = null;
	
	CharSequence[] users = new CharSequence[] {"user1", "user2", "user3", "user4"};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		
		UniqueID = this.findPreference("UniqueID");
		BroadcastID = this.findPreference("BroadcastID");
		AllowList = this.findPreference("Allow List");
		BlockList = this.findPreference("Block List");
		
		UniqueID.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
				builder.setTitle("UniqueID");
				final EditText input = new EditText(Preferences.this);
				builder.setView(input);
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				
				AlertDialog alert = builder.create();
				alert.show();
				
				return false;
			}
			
		});
		
		BroadcastID.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
				builder.setTitle("Broadcast ID");
				final EditText input = new EditText(Preferences.this);
				builder.setView(input);
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
					}
				});
				
				AlertDialog alert = builder.create();
				alert.show();
				return false;
			}
			
		});
		
		AllowList.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
				builder.setTitle("Allow List");
				builder.setItems(users, new OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						
					}
					
				});
				AlertDialog alert = builder.create();
				alert.show();
				return false;
			}
			
		});
		
		BlockList.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				AlertDialog.Builder builder = new AlertDialog.Builder(Preferences.this);
				builder.setTitle("Block List");
				builder.setItems(users, new OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
						
					}
					
				});
				AlertDialog alert = builder.create();
				alert.show();
				return false;
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
			startActivity(new Intent(Preferences.this, Preferences.class));
			break;
		}
		return false;
	}
	
}