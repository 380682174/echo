package cn.fish.rpc;


import cn.fish.rpc.common.HostInfo;
import cn.fish.rpc.netty.NettyServer;

public class ServerBootstrap {

    public static void main(String[] args) {

        NettyServer.startServer(HostInfo.HOST_NAME, HostInfo.PORT);

    }

}