package com.message.websocket;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

public class SendMsgToClient implements Runnable {


	@Override
	public void run() {

		ChannelGroup channels = Session.getChannels();

		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Channel channel : channels) {
				channel.pipeline();
				System.out.println("channels 大小 》》》"+channels.size()+"  channel>>>>"+channel.toString());
				System.out.println("isActive>>"+channel.isActive()+"  isOpen>> "+channel.isOpen() +"  isWritable>> "+channel.isWritable() +"   isRegistered>> "+channel.isRegistered() );
				String data = JsonRead.getData();
				channel.writeAndFlush(new TextWebSocketFrame(data));
			}
		}
	}

}
