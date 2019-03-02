package cn.fish.netty.server.handler;

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
            //此方法将对客户端发送过来的数据进行读取
            //由于客户端发送过来的数据未定义具体类型，故而统一按照Object接收
            // 默认情况下的类型就是ByteBuf类型
            ByteBuf byteBuf = (ByteBuf) msg;
            //在进行类型转换过程中还可以进行编码指定（NIO封装）
            String inputData = byteBuf.toString(CharsetUtil.UTF_8);
            //返回响应内容
            String echoData = "ECHO:"+inputData;
            if ("exit".equalsIgnoreCase(inputData)) {
                echoData = "quit!!!";
            }
            byte[] bytes = echoData.getBytes();
            //获取缓存
            ByteBuf byteBuf1 = Unpooled.buffer(bytes.length);
            //将返回内容写入缓存
            byteBuf1.writeBytes(bytes);
            //强制刷新返回缓存中的所有内容
            ctx.writeAndFlush(byteBuf1);
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
