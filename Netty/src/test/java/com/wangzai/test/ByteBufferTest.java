package com.wangzai.test;


import com.wangzai.nio.ByteBufferDemo;
import org.junit.Test;

import java.io.IOException;


public class ByteBufferTest {

    @Test
    public void testByteBuffer() throws IOException {

        ByteBufferDemo.readloadFile("file/test.txt");


    }
}

