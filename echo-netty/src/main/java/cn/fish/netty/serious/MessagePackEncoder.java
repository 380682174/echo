package cn.fish.netty.serious;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/3/8 15:17
 */
public class MessagePackEncoder extends MessageToByteEncoder<Object> {


    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {

        MessagePack messagePack = new MessagePack();
        //进行对象编码操作
        byte[] data = messagePack.write(msg);
        byteBuf.writeBytes(data);

    }
}
