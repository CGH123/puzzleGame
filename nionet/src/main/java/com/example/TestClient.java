/*
Copyright (c) 2008-2011 Christoffer Lernö

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.example;



import com.example.nioFrame.NIOService;
import com.example.nioFrame.NIOSocket;
import com.example.nioFrame.PacketRW.RegularPacketReader;
import com.example.nioFrame.PacketRW.RegularPacketWriter;
import com.example.nioFrame.SocketObserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;


public class TestClient implements Runnable {
	private static NIOSocket msocket;
	private static  byte[] mcontent;

	public static void test(){
		
		if(msocket == null)	{
			System.out.println("failed");
			return ;
		}
		System.out.println("run1");
		msocket.write(mcontent);
		System.out.println("run2");
	}
	


	public void run() {
		{
			try {

				String host = "localhost";
				String account = "Bob";
				String password = "QWERTY";

				int port = 9090;

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				DataOutputStream dataStream = new DataOutputStream(stream);
				dataStream.writeUTF(account);
				dataStream.writeUTF(password);
				dataStream.flush();
				final byte[] content = stream.toByteArray();
				dataStream.close();

				mcontent = content;

				NIOService service = new NIOService();

				NIOSocket socket = service.openSocket(host, port);
				msocket = socket;

				socket.setPacketReader(new RegularPacketReader(1, true));
				socket.setPacketWriter(new RegularPacketWriter(1, true));

				socket.listen(new SocketObserver() {
					public void connectionOpened(NIOSocket nioSocket) {
						System.out.println("Sending login...");
						nioSocket.write(content);
					}

					public void packetSent(NIOSocket socket, Object tag) {
						System.out.println("Packet sent");
					}

					public void packetReceived(NIOSocket socket, byte[] packet) {
						try {
							String reply = new DataInputStream(
									new ByteArrayInputStream(packet)).readUTF();
							System.out.println("Reply was: " + reply);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					public void connectionBroken(NIOSocket nioSocket,
							Exception exception) {
						System.out.println("Connection failed.");
					}
				});

				while (true) {
					service.selectBlocking();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		TestClient client = new TestClient();
		Thread thread = new Thread(client);
		thread.start();	
	}

}
