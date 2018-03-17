package com.pnp.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import com.pnp.utils.PreferenceConstants;

public class NettyManager {

	private static final ChannelFactory channelFactory = new NioClientSocketChannelFactory(
			Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

	/**
	 * 连接服务
	 * <p/>
	 * param host port
	 * 
	 * @return channel
	 */

	public static Channel connectToServer(String host, int port) {
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);
		setBootstrapOptions(bootstrap);
		addPipelineLast(bootstrap.getPipeline());
		ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress(
				host, port));
		Channel channel = connectFuture.awaitUninterruptibly().getChannel();
		return channel;
	}

	/**
	 * 连接服务
	 * <p/>
	 * param host port timeout
	 * 
	 * @return channel
	 */

	public static Channel connectToServer(String host, int port, int timeout) {
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);
		setBootstrapOptions(bootstrap);
		addPipelineLast(bootstrap.getPipeline());
		ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress(
				host, port));
		if (connectFuture.awaitUninterruptibly(timeout))
			return connectFuture.getChannel();
		return null;
	}

	private static void setBootstrapOptions(ClientBootstrap bootstrap) {
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
	}

	private static void addPipelineLast(ChannelPipeline pipeline) {
		ExecutionHandler executionHandler = new ExecutionHandler(
				new OrderedMemoryAwareThreadPoolExecutor(16, 1048576, 1048576));
		NettyMessageHandler msgHandler = new NettyMessageHandler();
		pipeline.addLast("executor", executionHandler);
		pipeline.addLast("handler", msgHandler);
	}

	public static void getChannelToSend(String message) {
		Channel channel = ChannelPool.get();
		if (null == channel) {
			channel = NettyManager.connectToServer(
					PreferenceConstants.PRE_SERVER_HOST,
					PreferenceConstants.PRE_SERVER_PORT, 10000);
		}
		System.out.println("netty:(channel is connected:"+channel.isConnected()+")");
		System.out.println("netty send data:"+message);
		sendData(channel, message);
	}

	/**
	 * 根据Channel发送xml
	 * 
	 * @param channel
	 * @param data
	 */
	public static void sendData(Channel channel, String data) {
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer();
		buffer.writeBytes(data.getBytes());
		channel.write(buffer);
	}

}
