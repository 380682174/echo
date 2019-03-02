package cn.fish.netty.server.handler;

import cn.fish.info.HostInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/3/2 13:31
 */
public class EchoServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // 当客户端连接上服务端时调用此方法，这边可以给客户端发送些明确信息
        byte[] datas = "【服务器激活信息】连接通道已激活，服务端开始响应交互".getBytes();
        //NIO是基于缓存的操作，所以Netty也提供有一系列的缓存类（封装了NIO中的Buffer）
        ByteBuf byteBuf = Unpooled.buffer(datas.length);
        //将数据写入缓存中
        byteBuf.writeBytes(datas);
        //强制性发送所有数据
        ctx.writeAndFlush(byteBuf);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            String inputData = msg.toString();
            System.err.println("{服务器}"+inputData);
            String echoData = "ECHO:"+inputData+HostInfo.SEPARATOR;
            ctx.writeAndFlush(echoData);
        } finally {
            //释放缓存
            ReferenceCountUtil.release(msg);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
