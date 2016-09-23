package com.test.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ServerNio {

	Selector  selector ;
	ServerSocketChannel serverSocketChannel ;
	public static void main(String args[]){
		ServerNio serverNio  = new ServerNio();
		serverNio.init();
		serverNio.service();
	}

	private void init(){
		try {
			selector = Selector.open();
			serverSocketChannel	= ServerSocketChannel.open();
			serverSocketChannel.socket().setReuseAddress(true);
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(8000));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void service(){
		SelectionKey key = null ;
		try {
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			while(true){
				selector.select();
				Set readKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = readKeys.iterator();
				while(it.hasNext()){
					try{
						key = it.next();
						System.out.println(" >>>>> "+type(key.readyOps())+">>>>>" +readKeys.toString() );
						it.remove();
						if(key.isAcceptable()){
							System.out.println(" >>>>>is accept "+readKeys.size() +">>>" +readKeys.toString());
							ServerSocketChannel scc = (ServerSocketChannel)key.channel();
							SocketChannel sc = (SocketChannel) scc.accept();
							sc.configureBlocking(false);
							ByteBuffer bf = ByteBuffer.allocate(1024);
							sc.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
						}
						if(key.isConnectable()){
							System.out.println(" >>>>>is isConnectable");
						}
						if(key.isReadable()){

							SocketChannel sc = (SocketChannel)key.channel();
							int r =0 ;
							try{ByteBuffer bf = ByteBuffer.allocate(1024);
							r =sc.read(bf);
							bf.flip();
							System.out.println(" 字节数"+r+" >>>>>  "+	decode(bf) );
							}catch(Exception e){
								e.printStackTrace();;
								key.cancel();
							}
							if(r<0){
								key.cancel();
							}

						} else if(key.isWritable()){
							SocketChannel sc = (SocketChannel)key.channel();
							ByteBuffer bf =  Charset.forName("UTF-8").encode("end!");
							sc.write(bf);
							key.cancel();
//							sc.close();
						}
					}finally{

					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String decode(ByteBuffer bf){
		CharBuffer charBuffer = Charset.forName("UTF-8").decode(bf);
		return charBuffer.toString();
	}
	private ByteBuffer decode(String str){
		return   Charset.forName("UTF-8").encode(str);
	}

	private String type(int i){
		StringBuilder type = new StringBuilder() ;
		if((i&1)>0){
			type.append("read").append("|");
		}
		if((i&4)>0){
			type.append("write").append("|");
		}
		if((i&8)>0){
			type.append("CONNECT").append("|");
		}
		if((i&16)>0){
			type.append("ACCEPT").append("|");
		}
		return type.toString();
	}
}
