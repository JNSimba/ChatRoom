package com.chatroom;

import java.awt.BorderLayout;

import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ChatClient  extends Frame{
	
	 
	private static final long serialVersionUID = 1L;
	
	private Socket s = null;
	private DataOutputStream dos = null;
	private DataInputStream dis = null;
	private boolean cont = false;

	private TextField tfTxt = new TextField();
	private TextArea taContent = new TextArea();
	
	private Thread tRecv = new Thread(new RecvThread());
	
	public void launchFrame()
	{
		setLocation(400,300);
		this.setSize(300,300);
		taContent.setEditable(false);//不可编辑
		add(tfTxt,BorderLayout.SOUTH);
		add(taContent,BorderLayout.NORTH);
		pack(); //包在一起，去除中间空着的
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				 
				 disconnect();
				 System.exit(0);
			}

			

		});
		
		tfTxt.addActionListener(new TfListent());
		setVisible(true);
		connect();
		tRecv.start();
	}

	private void connect() {
		 
		try{
			s = new Socket("127.0.0.1",8888);
			System.out.println("connected.....");
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			cont = true;
		}catch(UnknownHostException e3)
		{
			e3.printStackTrace();
		}catch(IOException e3)
		{
			e3.printStackTrace();
		}
	}
	
	private void disconnect() {

		try{
		dos.close();
		dis.close();
		s.close();
		}catch(IOException e2)
		{
			e2.printStackTrace();
		}
	}
	
	private class TfListent implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) {
		 
		 
			String str = tfTxt.getText().trim();
			str = this+":::"+str;
			tfTxt.setText("");
			
			try{
				dos.writeUTF(str);
				dos.flush();
			}catch(IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}
	private class RecvThread implements Runnable
	{

		@Override
		public void run() {
		 
		try{			
				while(cont)
				{
					String str = dis.readUTF();
					taContent.setText(taContent.getText()+str+'\n');
				}
			}
			catch(SocketException e )
			{
				System.out.println("退出了");
			}catch(IOException o)
			{
				o.printStackTrace();
			}
			
		}
		
	}
	public static void main(String[] args) {
		new ChatClient().launchFrame();
	}
	
}

