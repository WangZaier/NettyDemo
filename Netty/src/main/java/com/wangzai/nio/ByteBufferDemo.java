package com.wangzai.nio;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class ByteBufferDemo {


    public static void readloadFile(String fileName) {

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
            FileChannel fileChannel = randomAccessFile.getChannel();


            //分配内存空间
            ByteBuffer byteBuffer = ByteBuffer.allocate(48);

            int size = fileChannel.read(byteBuffer);

            //从buffer中读取数据
            while (size > 0) {

                //把byteBuffer从写模式转为读模式
                byteBuffer.flip();

                //输出
                Charset charset = Charset.forName("UTF-8");
                System.out.println(charset.newDecoder().decode(byteBuffer).toString());
                byteBuffer.clear();

                //读取完成以后clear
                byteBuffer.clear();
                size = fileChannel.read(byteBuffer);

            }

            //读取完成后
            fileChannel.close();
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
