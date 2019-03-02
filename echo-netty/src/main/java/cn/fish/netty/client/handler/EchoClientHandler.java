package cn.fish.netty.client.handler;

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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        try {
            ByteBuf byteBuf = (ByteBuf) msg;
            String outputData = byteBuf.toString(CharsetUtil.UTF_8);
            if ("quit!!!".equalsIgnoreCase(outputData)) {
                System.out.println("结束本次会话之旅！！！");
                ctx.close();
            } else {
                System.out.println("输出服务端返回的响应内容："+outputData);
                String inputData = InputUtil.getString("请输入内容：");
                byte[] datas = inputData.getBytes();
                ByteBuf byteBuf1 = Unpooled.buffer(datas.length);
                byteBuf1.writeBytes(datas);
                ctx.writeAndFlush(byteBuf1);
            }
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
