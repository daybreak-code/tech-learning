package com.llm.netty.young;

import com.llm.netty.young.handler.IProtocalHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

public class Server {

    private static void start(int port) throws InterruptedException {
        NioEventLoopGroup boss = null;
        NioEventLoopGroup workers = null;
        try {
            boss = new NioEventLoopGroup();
            workers = new NioEventLoopGroup();

            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, workers);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer() {

                @Override
                protected void initChannel(Channel channel) throws Exception {
                    channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 2, 2, -2, 0));
                    channel.pipeline().addLast(new DefaultEventExecutorGroup(16), new IProtocalHandler());
                    channel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
                }
            });

            ChannelFuture feature = b.bind(port).sync();
            feature.channel().closeFuture().sync();
        } finally {
            if (boss != null || workers != null) {
                boss.shutdownGracefully();
                workers.shutdownGracefully();
            }

        }
    }

    public static void main( String[] args ) throws InterruptedException{
        start(8084);
    }
}
