package com.wangzai.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class startClient {

    public static void socketClient() throws IOException, InterruptedException {


        //管道开启
        SocketChannel socketClient = SocketChannel.open();
        socketClient.connect(new InetSocketAddress("localhost", 8888));
        socketClient.configureBlocking(false);


        //设置请求内容
        String request = "hello";
        //设置编码
        ByteBuffer byteBuffer = ByteBuffer.wrap(request.getBytes("UTF-8"));
        //向服务端写
        socketClient.write(byteBuffer);

        //设置大小
        ByteBuffer readBuffer = ByteBuffer.allocate(48);
        //
        int size = socketClient.read(readBuffer);

        while (size > 0) {
            //写读切换
            readBuffer.flip();
            //编码
            Charset charset = Charset.forName("UTF-8");
            //结果编码后输出
            System.out.println(charset.newDecoder().decode(readBuffer).toString());
            //清除buffer
            readBuffer.clear();
            //重新读取
            size = socketClient.read(readBuffer);

        }


        socketClient.close();

        Thread.sleep(3000);

    }


    public static void main(String[] args) throws IOException, InterruptedException {
        socketClient();
    }

}
