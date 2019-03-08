package cn.fish.netty.client;

import cn.fish.info.HostInfo;
import cn.fish.netty.client.handler.EchoClientHandler;
import cn.fish.netty.serious.MessagePackDecoder;
import cn.fish.netty.serious.MessagePackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.*;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.msgpack.packer.MessagePackPacker;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/3/2 14:45
 */
public class EchoClient {

    public void run() throws InterruptedException {

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,4));
                            socketChannel.pipeline().addLast(new MessagePackDecoder());
                            //与属性个数保持一致
                            socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                            socketChannel.pipeline().addLast(new MessagePackEncoder());
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect(HostInfo.HOST_NAME,HostInfo.PORT).sync();
            future.channel().closeFuture().sync();

        } finally {

            eventLoopGroup.shutdownGracefully();

        }

    }

}
