package cn.fish.rpc;

import cn.fish.rpc.common.CommonUtils;
import cn.fish.rpc.common.IHelloService;
import cn.fish.rpc.netty.NettyClient;

public class ClientBootstrap {

    public static void main(String[] args) throws InterruptedException {

        NettyClient consumer = new NettyClient();
        // 创建一个代理对象
        IHelloService service = (IHelloService) consumer.getBean(IHelloService.class, CommonUtils.providerName);

        for (; ; ) {
            Thread.sleep(1000);
            System.out.println(service.hello("are you ok ?"));
        }
    }

}