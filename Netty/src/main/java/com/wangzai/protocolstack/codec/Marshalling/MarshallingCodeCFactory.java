package com.wangzai.protocolstack.codec.Marshalling;


import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;


public final class MarshallingCodeCFactory {

    public static NettyMarshallingEncoder buildMarshallingEncoder() {

        //这里我们通过Marshalling工具类的getProvidedMarshallerFactory静态方法获取MarshallerFactory实例
        //参数serial表示创建的是java序列化工厂对象
        final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");

        //创建MarshallingConfiguration对象后将他的版本号设置为5
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);

        //根据MarshallerFactory和MarshallingConfiguration创建MarshallerProvider实例
        MarshallerProvider provider = new DefaultMarshallerProvider(factory, configuration);

        //通过provider作为参数创建 NettyMarshallingEncoder 实例,
        //NettyMarshallingEncoder用来将实现序列化接口的POJO对象序列化为二进制数组
        NettyMarshallingEncoder encoder = new NettyMarshallingEncoder(provider);
        return encoder;
    }

    public static NettyMarshallingDecoder buildMarshallingDecoder() {
        final MarshallerFactory factory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);

        //根据MarshallerFactory和MarshallingConfiguration创建UnmarshallerProvider实例
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, configuration);
        //最后通过构造函数创建 MarshallingDecode,其中1024是单个消息序列化后的最大长度
        NettyMarshallingDecoder decoder = new NettyMarshallingDecoder(provider, 1024);
        return decoder;
    }

}