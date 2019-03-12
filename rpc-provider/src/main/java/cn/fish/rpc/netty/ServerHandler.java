package cn.fish.rpc.netty;

import cn.fish.rpc.common.CommonUtils;
import cn.fish.rpc.common.IHelloService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 用于处理请求数据
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {

        // 如何符合约定，则调用本地方法，返回数据
        if (msg.toString().startsWith(CommonUtils.providerName)) {

            //HelloService#hello#are you ok ?
            String[] msgArr = msg.toString().split("#");
            String interfaceName = msgArr[0];
            String methodName = msgArr[1];
            String param = msgArr[2];
            Class clazz = Class.forName("cn.fish.rpc.provider."+interfaceName.substring(1,interfaceName.length())+"Impl");
            Method method = clazz.getMethod(methodName,String.class);
            IHelloService clazzInstance = (IHelloService) clazz.newInstance();
            String result = (String) method.invoke(clazzInstance,param);

            //String result = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(result);
        }

    }
}