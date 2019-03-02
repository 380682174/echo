package cn.fish.netty.client.handler;

import cn.fish.info.HostInfo;
import cn.fish.info.InputUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * @Description: 需要对服务端处理返回的数据进行读取操作
 * @Author devin.jiang
 * @CreateDate 2019/3/2 14:37
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    /***
     * 消息重发次数
     */
    private static final int REPEAT = 500;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for (int x = 0; x < REPEAT; x++) {
            String hello = "【"+x+"】hello word!".getBytes()+HostInfo.SEPARATOR;
            ctx.writeAndFlush(hello);
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            // 接收返回数据内容
            String data = msg.toString();
            // 输出服务器端的响应内容
            System.out.println(data);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
