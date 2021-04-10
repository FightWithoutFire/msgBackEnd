package com.ebtq.msg.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import lombok.extern.java.Log;

import java.net.InetSocketAddress;


@Log
public class ChatServer {
    private final ChannelGroup channelGroup =
        new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();


    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(createInitializer(channelGroup));
        return bootstrap.bind(address).syncUninterruptibly();
    }

    protected ChannelInitializer<SocketChannel> createInitializer(
        ChannelGroup group) {
        return new ChatServerInitializer(group);
    }

    public void destroy() {
        channelGroup.close();
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    public ChannelFuture start(int port) throws Exception {
        ChannelFuture channelFuture = null;
        try {
            final ChatServer endpoint = new ChatServer();
            channelFuture = endpoint.start(
                    new InetSocketAddress(port));
            log.info("=== server start ===");
            Runtime.getRuntime().addShutdownHook(new Thread(endpoint::destroy));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return channelFuture;
    }
}
