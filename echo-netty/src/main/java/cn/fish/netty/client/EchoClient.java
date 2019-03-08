package cn.fish.netty.client;

import cn.fish.info.HostInfo;
import cn.fish.netty.client.handler.EchoClientHandler;
import cn.fish.netty.serious.MarshallingCodeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

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
                            socketChannel.pipeline().addLast(MarshallingCodeFactory.buildMarshallingEncoder()) ;
                            socketChannel.pipeline().addLast(MarshallingCodeFactory.buildMarshallingDecoder()) ;
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
