package com.message.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;
import io.netty.util.CharsetUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;

public class HttpjsRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> { //1


	@Override
	public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		String uri =request.uri();
		System.out.println(">>>>>"+uri);
		if(uri.indexOf(".js")>0){
			String jsName ="J:/work/pythonWorkSpace/nettyWeb/web/js/"+uri.substring(uri.lastIndexOf("/")+1);
			RandomAccessFile file1 = new RandomAccessFile(jsName, "r");//4
			//sendJavaScript(ctx, request, file1);	
			
			sendMsg(ctx, request,new File(jsName));
			
		} else if("/".equals(uri)){
			RandomAccessFile file = new RandomAccessFile("J:/work/pythonWorkSpace/nettyWeb/web/charts/test.html", "r");//4
			sendFile(ctx, request, file);
		}
	}

	private void sendFile(ChannelHandlerContext ctx, FullHttpRequest request,RandomAccessFile file ){

		HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

		boolean keepAlive = HttpUtil.isKeepAlive(request);
		try {
			if (keepAlive) {                                        //5
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
				response.headers().set(HttpHeaderNames.CACHE_CONTROL, HttpHeaderValues.NO_CACHE);
			}
			ctx.write(response);                    //6

			if (ctx.pipeline().get(SslHandler.class) == null) {     //7
				ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
			} else {
				ctx.write(new ChunkedNioFile(file.getChannel()));
			}
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);           //8
			if (!keepAlive) {
				future.addListener(ChannelFutureListener.CLOSE);        //9
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendJavaScript(ChannelHandlerContext ctx, FullHttpRequest request,RandomAccessFile file ){

		HttpResponse response = new DefaultHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/javascript; charset=UTF-8");

		boolean keepAlive = HttpUtil.isKeepAlive(request);
		try {
			if (keepAlive) {                                        //5
				response.headers().set(HttpHeaderNames.CONTENT_LENGTH, file.length());
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
				response.headers().set(HttpHeaderNames.CACHE_CONTROL, HttpHeaderValues.NO_CACHE);
			}
			ctx.write(response);                    //6

			if (ctx.pipeline().get(SslHandler.class) == null) {     //7
				ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
			} else {
				ctx.write(new ChunkedNioFile(file.getChannel()));
			}
			ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);           //8
			if (!keepAlive) {
				future.addListener(ChannelFutureListener.CLOSE);        //9
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendMsg(ChannelHandlerContext ctx, FullHttpRequest request ,File file){

		FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), HttpResponseStatus.OK);
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/javascript; charset=UTF-8");
		StringBuilder sb= new StringBuilder();
		BufferedReader br  =null;
		try {
			 br = new BufferedReader(new FileReader(file));
			String msg ;
			while((msg = br.readLine()) != null){
				sb.append(msg).append("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		boolean keepAlive = HttpUtil.isKeepAlive(request);
		
		if (keepAlive) {                                        //5
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, sb.length());
			response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			response.headers().set(HttpHeaderNames.CACHE_CONTROL, HttpHeaderValues.NO_CACHE);
		}
//		System.out.println(sb.toString());
		ByteBuf buffer = Unpooled.copiedBuffer(sb.toString(),CharsetUtil.UTF_8);  
		response.content().writeBytes(buffer);  
		//buffer.release();  
		ctx.writeAndFlush(response).addListener(new ChannelFutureListener(){

			@Override
			public void operationComplete(ChannelFuture future)
					throws Exception {
				System.out.println(">>>>>关闭  isDone 为 "+future.isDone());
				System.out.println(">>>>>关闭  isSuccess 为 "+future.isSuccess());
				System.out.println(">>>>>关闭  ChannelFuture 为 "+future.getClass());
 				future.channel().close();
			}

		}); 
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		Channel incoming = ctx.channel();
		System.out.println("Client:"+incoming.remoteAddress()+"异常");
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}