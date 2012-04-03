package com.nikhil.WifiChat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class ClientThread extends Thread {
	
	String address;
	String message;
	int portNum;
	InetAddress serverAddr;
	Socket socket;
	PrintWriter out;
	boolean broadcast;
	
	public ClientThread(String addr, int port, String msg, boolean bcast) throws IOException {
		address = addr;
		portNum = port;
		message = msg;
		broadcast = bcast;
	}

	public void run() {
		try {
			
  			ChatService.portsocket.setBroadcast(broadcast);
  			DatagramPacket packet1 = new DatagramPacket(message.getBytes(), message.length(), InetAddress.getByName(address), 5555);
  			ChatService.portsocket.send(packet1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
