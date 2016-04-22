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


import com.example.nioFrame.ConnectionAcceptor;
import com.example.nioFrame.NIOServerSocket;
import com.example.nioFrame.NIOService;
import com.example.nioFrame.NIOSocket;
import com.example.nioFrame.PacketRW.RegularPacketReader;
import com.example.nioFrame.PacketRW.RegularPacketWriter;
import com.example.nioFrame.ServerSocketObserver;
import com.example.nioFrame.SocketObserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class TestServer implements Runnable
{
//	out_buffer = ByteBuffer.wrap(new String("�յ��ͻ���Ӧ��").getBytes());

	private static  NIOSocket[] msocket = new NIOSocket[2];
	private static byte[] content;
	private static int index=0;
	
	private boolean falg=true;

	public static void test(){
		if(msocket == null){System.out.println("msocket is null"); return;}
		System.out.println("success");
		System.out.println("error2");
	}
	
	public void run()
	{
		
		int port=9090;
		// Create a map with users and passwords.
		final Map<String, String> passwords = new HashMap<String, String>();
		passwords.put("Admin", "password");
		passwords.put("Aaron", "AAAAAAAA");
		passwords.put("Bob", "QWERTY");
		passwords.put("Lisa", "secret");
		try
		{
			NIOService service = new NIOService();
			NIOServerSocket socket = service.openServerSocket(port);
			socket.listen(new ServerSocketObserver()
			{
				@Override
				public void acceptFailed(IOException exception) {

				}

				@Override
				public void serverSocketDied(Exception exception) {

				}

				public void newConnection(NIOSocket nioSocket)
				{
					System.out.println("Received connection: " + nioSocket);
						
					msocket[index++] = nioSocket;
					
					nioSocket.setPacketReader(new RegularPacketReader(1, true));
					nioSocket.setPacketWriter(new RegularPacketWriter(1, true));

					nioSocket.listen(new SocketObserver()
					{
						@Override
						public void connectionOpened(NIOSocket nioSocket) {

						}

						@Override
						public void connectionBroken(NIOSocket nioSocket, Exception exception) {

						}

						public void packetReceived(NIOSocket socket, byte[] packet)
						{
							System.out.println("Login attempt from " + socket);
							try
							{
								DataInputStream stream = new DataInputStream(new ByteArrayInputStream(packet));

								String user = stream.readUTF();
								String password = stream.readUTF();

								ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
								DataOutputStream out = new DataOutputStream(byteArrayOutputStream);

								if (!passwords.containsKey(user))
								{
									System.out.println("Unknown user: " + user);
									out.writeUTF("NO_SUCH_USER");
								}
								else if (!passwords.get(user).equals(password))
								{
									out.writeUTF("INCORRECT_PASS");
									System.out.println("Failed login for: " + user);
								}
								else
								{
									out.writeUTF("LOGIN_OK fuck");
									System.out.println("Successful login for: " + user);
								}

								out.flush();
								socket.write(byteArrayOutputStream.toByteArray());
								if(msocket[1]==socket){
									msocket[0].write(byteArrayOutputStream.toByteArray());
									msocket[1].write(byteArrayOutputStream.toByteArray());
								}

							}
							catch (IOException e)
							{
								socket.close();
							}
						}

						@Override
						public void packetSent(NIOSocket socket, Object tag) {

						}
					});
				}
			});
			
			socket.setConnectionAcceptor(ConnectionAcceptor.ALLOW);

			while (true)
			{
				service.selectBlocking();
				
			}
		}
		catch (IOException e)
		{
		}
	}

	public static void main(String[] args){
		TestServer server = new TestServer();
		Thread thread = new Thread(server);
		server.run();
		
		/*for(int i=0;i<3;i++){			
			System.out.println("i still run");
			test();
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		
	}

}
