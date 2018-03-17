package com.pnp.network;

import org.jboss.netty.channel.Channel;

public class ChannelPool {

	private static Channel channel = null;

	public static Channel get() {
		return channel;
	}

	public static void put(Channel channel) {
		ChannelPool.channel = channel;
	}

	public static void close() {
		if (null == channel)
			return;
		channel.close();
		channel = null;
	}

}
