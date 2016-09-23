package com.message.websocket;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

	private final static Map<String , Channel> sessions = new ConcurrentHashMap<String , Channel>();
	
	private  static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	public  static ChannelGroup getChannels() {
		return channels;
	}

	public static synchronized void setChannels(ChannelGroup channels) {
		Session.channels = channels;

	}

	public static Map<String , Channel> getSessions() {
		return sessions;
	}
	
	public static void  addSession(String id , Channel channel) {
		sessions.put(id, channel);
	}
	
	public static void  removeSession(String id ) {
		sessions.remove(id);
	}
	
}
