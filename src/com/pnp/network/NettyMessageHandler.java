package com.pnp.network;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChildChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.WriteCompletionEvent;

import android.util.Log;

import com.pnp.LoginActivity;
import com.pnp.PNPApplication;
import com.pnp.model.CommandModel;
import com.pnp.utils.CommandUtil;
import com.pnp.utils.StringUtil;

public class NettyMessageHandler extends SimpleChannelHandler {

	private String recvMessage = "";

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);
		Log.i("netty", "channelConnected:" + e.getChannel().getRemoteAddress());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		super.messageReceived(ctx, e);
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		System.out.println("6666666666666");
		if (buffer.hasArray()) {
			String recv = buffer.toString(java.nio.charset.Charset
					.defaultCharset());
			Log.i("recvmsg", "messageReceived：" + recv.length() + recv
					+ "\n\n\n");
			recvMessage += recv;
			if (StringUtil.isXmlFull(recvMessage)) {
				// Log.i("netty", "recv message:" + recvMessage + "\n\n\n");
				// Command command = CommandParser.unserialize(recvMessage);
				// Log.i("asdfg", command.getTypeName());
				// CommandDispatcher.dispatcher(command);
				// recvMessage = "";
			}
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelClosed(ctx, e);
		Log.i("netty", "channelClosed");
		e.getChannel().close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		super.exceptionCaught(ctx, e);
		Log.i("netty", "，netty error: cause：" + e.getCause().toString());

		if ((e.getCause().toString())
				.equals("java.nio.channels.ClosedChannelException")) {
			CommandModel command = CommandUtil.getNetErrorCommand();
			if (PNPApplication.getCurrentActivity().equals(
					LoginActivity.class.getName())) {
				LoginActivity.reflaction(command);
			}
		}

		ctx.getChannel().close().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				Log.i("netty", "netty error: info:"
						+ future.getChannel().isOpen());
			}
		});
	}

	@Override
	public void bindRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.bindRequested(ctx, e);
	}

	@Override
	public void channelBound(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelBound(ctx, e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void channelInterestChanged(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelInterestChanged(ctx, e);
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelOpen(ctx, e);
	}

	@Override
	public void channelUnbound(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelUnbound(ctx, e);
	}

	@Override
	public void childChannelClosed(ChannelHandlerContext ctx,
			ChildChannelStateEvent e) throws Exception {
		super.childChannelClosed(ctx, e);
	}

	@Override
	public void childChannelOpen(ChannelHandlerContext ctx,
			ChildChannelStateEvent e) throws Exception {
		super.childChannelOpen(ctx, e);
	}

	@Override
	public void closeRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.closeRequested(ctx, e);
	}

	@Override
	public void connectRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.connectRequested(ctx, e);
	}

	@Override
	public void disconnectRequested(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.disconnectRequested(ctx, e);
	}

	@Override
	public void handleDownstream(ChannelHandlerContext arg0, ChannelEvent arg1)
			throws Exception {
		super.handleDownstream(arg0, arg1);
	}

	@Override
	public void handleUpstream(ChannelHandlerContext arg0, ChannelEvent arg1)
			throws Exception {
		super.handleUpstream(arg0, arg1);
	}

	@Override
	public void setInterestOpsRequested(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.setInterestOpsRequested(ctx, e);
	}

	@Override
	public void unbindRequested(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.unbindRequested(ctx, e);
	}

	@Override
	public void writeComplete(ChannelHandlerContext ctx, WriteCompletionEvent e)
			throws Exception {
		super.writeComplete(ctx, e);
	}//788375

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		super.writeRequested(ctx, e);
	}

}
