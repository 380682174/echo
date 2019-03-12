package cn.fish.rpc.provider;


import cn.fish.rpc.common.IHelloService;

/**
 * 实现类
 */
public class HelloServiceImpl implements IHelloService {

    public String hello(String msg) {

        return msg != null ? msg + " -----> I am fine." : "I am fine.";

    }

}