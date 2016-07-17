package com.message.websocket;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Session {

	private  static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public  static ChannelGroup getChannels() {
		return channels;
	}

	public static synchronized void setChannels(ChannelGroup channels) {
		Session.channels = channels;

	}


}
