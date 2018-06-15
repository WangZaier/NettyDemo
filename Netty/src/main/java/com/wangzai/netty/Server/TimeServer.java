package com.wangzai.netty.Server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TimeServer {

    public static void main(String[] args) throws Exception {
        new TimeServer().bind(8888);
    }

    public void bind(int port) throws Exception {


        //创建两个线程组,他们包含了一组NIO线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();  //接收客户端链接
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //进行SocketChannel网络读写

        try {
            //启动类
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //配置
            serverBootstrap.group(bossGroup, workerGroup)//将线程组加入进去
                    .channel(NioServerSocketChannel.class)//设置通道类型为NIOServerSocketChannel,这里对应JDK的ServerSocketChannel
                    .childHandler(new ChannelInitializer<SocketChannel>() {//配置处理类
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //解码器
                            socketChannel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            socketChannel.pipeline().addLast(new StringDecoder());
                            //处理类
                            socketChannel.pipeline().addLast(new TimeServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024)          //最大连接数
                    .childOption(ChannelOption.SO_KEEPALIVE, true); //心跳配置开启

            //绑定端口
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
