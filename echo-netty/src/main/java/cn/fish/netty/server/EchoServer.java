package cn.fish.netty.server;

import cn.fish.info.HostInfo;
import cn.fish.netty.serious.JSONDecoder;
import cn.fish.netty.serious.JSONEncoder;
import cn.fish.netty.server.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/3/2 14:11
 */
public class EchoServer {

    /**
     * 服务器启动方法
     */
    public void run() {

        //线程池是保证服务端性能的重要手段，固定线程数可以保证有效的核心线程数
        //1、Netty中包含了两类线程池：主线程池（用于接收客户端连接），工作线程池（用于处理客户端连接）
        EventLoopGroup boss = new NioEventLoopGroup(10);
        EventLoopGroup worker = new NioEventLoopGroup(20);
        try {
            System.out.println("服务器已启动，监听端口为："+HostInfo.PORT);
            //2、创建一个服务端启动类，并设置channel
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,worker).channel(NioServerSocketChannel.class);
            //3、接收到消息后需进行处理，于是定义子处理器类
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(65536, 0, 4, 0, 4));
                    socketChannel.pipeline().addLast(new JSONDecoder());
                    socketChannel.pipeline().addLast(new LengthFieldPrepender(4));
                    socketChannel.pipeline().addLast(new JSONEncoder());
                    socketChannel.pipeline().addLast(new EchoServerHandler());
                }
            });
            //可以利用常量配置进行TCP配置
            serverBootstrap.option(ChannelOption.SO_BACKLOG,128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);
            //ChannelFuture描述的是异步回调时的处理操作
            ChannelFuture future = serverBootstrap.bind(HostInfo.PORT).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }

    }

}
