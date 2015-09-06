package com.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

	private boolean status = false;  //用于测试服务器的状态
	private ServerSocket ss =null;
	
	private List<Client> clients = new ArrayList<Client>();
	
	public static void main(String[] args) {
		
		new ChatServer().start();
		
	}

	private void start() {
	 
		try {
			ss = new ServerSocket(8888);
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
		status = true;
		
		try
		{
			while(status)
			{
				Socket s = ss.accept();
				System.out.println("a client connected");
				
				Client c = new Client(s);
				
				new Thread(c).start();
				
				clients.add(c);  //将连入 服务端的客户端放到集合中
			}
		}catch(IOException o)
		{
			o.printStackTrace();
		}
	}
 

class Client implements Runnable
{

	private	Socket s = null;
	private DataInputStream dis ;
	private DataOutputStream dos;
	 private boolean conf=false;
	
	
	
	public Client(Socket s) {
		
		this.s = s;
	
		try {
			
			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			conf = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public  void send(String str)  //发送给客户端
	{
		
		try {
			//str = this+":::  "+str;
			dos.writeUTF(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			clients.remove(this); //移除不存在的对象
			System.out.println("一个对象退出了");
			
			//e.printStackTrace();
		}
	}
	@Override
	public void run() {
		
		try {
			
			while(conf)
			{
				String  str = dis.readUTF();
				System.out.println(str);
				
				for(int i=0;i<clients.size();i++)
				{
					Client c = clients.get(i);
					c.send(str);
				}
				
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try{
				if(dis!=null) dis.close();
				if(dos!=null) dos.close();
				if(s !=null){
					s.close();
					s=null;
				}
				
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	
}
}
