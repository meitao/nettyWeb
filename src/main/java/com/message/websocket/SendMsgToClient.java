package com.message.websocket;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.Map;
import java.util.Map.Entry;

public class SendMsgToClient implements Runnable {


	@Override
	public void run() {

		while(true){
			ChannelGroup channels = Session.getChannels();
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Map<String , Channel> param = Session.getSessions();
			for (Channel channel : channels) {
				channel.pipeline();
				System.out.println("channels 大小 》》》"+channels.size()+"  channel>>>>"+channel.toString());
				System.out.println("isActive>>"+channel.isActive()+"  isOpen>> "+channel.isOpen() +"  isWritable>> "+channel.isWritable() +"   isRegistered>> "+channel.isRegistered() );
				String data = JsonRead.getData();
				channel.writeAndFlush(new TextWebSocketFrame(data));
			}
			
			for (Entry<String , Channel> entry : param.entrySet()) {
				Channel channel = entry.getValue();
				System.out.println("channels 大小 》》》"+param.size()+"  channel>>>>"+channel.toString());
				System.out.println("isActive>>"+channel.isActive()+"  isOpen>> "+channel.isOpen() +"  isWritable>> "+channel.isWritable() +"   isRegistered>> "+channel.isRegistered() );
				String data = JsonRead.getData();
				channel.writeAndFlush(new TextWebSocketFrame(data));
			}
		}
	}

}
