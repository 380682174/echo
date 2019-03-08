package cn.fish.netty.serious;

import cn.fish.vo.Member;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/3/8 17:08
 */
public class JSONDecoder extends MessageToMessageDecoder<ByteBuf> {


    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> list) throws Exception {
        int len = msg.readableBytes();
        byte[] data = new byte[len];
        msg.getBytes(msg.readerIndex(),data,0,len);
        list.add(JSON.parseObject(new String(data)).toJavaObject(Member.class));
    }
}
