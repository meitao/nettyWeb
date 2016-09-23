package com.test.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class ClientNio {

	public static void main(String args[]){ 
		
		System.out.println( 5&2);
		try {
			InetAddress ia = InetAddress.getLocalHost();
			SocketChannel sc = SocketChannel.open() ;
			InetSocketAddress ad = new InetSocketAddress(ia,8000);
			sc.connect(ad);
			sc.configureBlocking(false);
			Selector selector = Selector.open();
			sc.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			int i =0 ;
			while(selector.select()>0){
				Set  readKeys= selector.selectedKeys();
				Iterator it = readKeys.iterator();
				while(it.hasNext()){
					SelectionKey key = null;
					try{	
						key = (SelectionKey) it.next();

						if(key.isReadable()){
							System.out.println(">>>>  isReadable ");
						}
						if(key.isWritable()){
							System.out.println(">>>>  isWritable ");
							SocketChannel scc =	(SocketChannel) key.channel();
							if(i==10){
								ByteBuffer  bf =  Charset.forName("UTF-8").encode("123!");
								scc.write(bf);  
							}else{
								ByteBuffer bf =  Charset.forName("UTF-8").encode("hello !");
								scc.write(bf);
							}
							i++;
						}
						it.remove();
					} finally{
//												if(key!=null){
//													key.channel().close();
//													key.cancel();
//												}
					}

				}
			}
			//			sc.write(Charset.forName("UTF-8").encode("hello !"));
			//			BufferedWriter br = new BufferedWriter(new OutputStreamWriter(sc.socket().getOutputStream()));
			//			br.write("hello");
			//			PrintWriter pr = new PrintWriter(sc.socket());
			//			while(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
