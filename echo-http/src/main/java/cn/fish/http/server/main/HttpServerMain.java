package cn.fish.http.server.main;

import cn.fish.http.server.HttpServer;
import cn.fish.info.HostInfo;

public class HttpServerMain {
    public static void main(String[] args) throws Exception {
        /*if (args.length != 1) {
            System.err.println(
                    "Usage: " + HttpServer.class.getSimpleName() +
                            " <port>");
            return;
        }
        int port = Integer.parseInt(args[0]);*/
        new HttpServer(HostInfo.PORT).start();
    }
}