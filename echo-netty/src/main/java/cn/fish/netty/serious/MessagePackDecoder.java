package cn.fish.netty.serious;

import cn.fish.vo.Member;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/3/8 15:08
 */
public class MessagePackDecoder extends MessageToMessageDecoder<ByteBuf> {
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> list) throws Exception {

        //获取读取数据的长度
        int len = msg.readableBytes();
        //准备读取数据的空间
        byte[] data = new byte[len];
        //读取数据
        msg.getBytes(msg.readerIndex(),data,0,len);
        MessagePack messagePack = new MessagePack();
        System.out.println(messagePack.read(data));
        list.add(messagePack.read(data,messagePack.lookup(Member.class)));

    }
}
