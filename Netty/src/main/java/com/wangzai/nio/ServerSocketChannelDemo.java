package com.wangzai.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ServerSocketChannelDemo {

    public static void main(String[] args) throws IOException {

        //管道开启
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8888));
        serverSocketChannel.configureBlocking(false);


        while (true) {
            //接受数据
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                //分配空间
                ByteBuffer byteBuffer = ByteBuffer.allocate(48);
                //读取字节确定不为空
                int size = socketChannel.read(byteBuffer);

                while (size > 0) {
                    //读写切换
                    byteBuffer.flip();
                    //设置转码
                    Charset charset = Charset.forName("UTF-8");
                    //转码输出
                    System.out.println(charset.newDecoder().decode(byteBuffer).toString());
                    //读取下一个size
                    size = socketChannel.read(byteBuffer);
                }


                //设置回写信息
                ByteBuffer reponse = ByteBuffer.wrap("收到".getBytes());
                //回写
                socketChannel.write(reponse);
                //清空
                reponse.clear();
                //管道关闭
                socketChannel.close();

            }
        }


    }

}
