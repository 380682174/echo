package cn.fish.bio.server;

import cn.fish.info.HostInfo;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/2/15 21:02
 */
public class BIOEchoServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(HostInfo.PORT);
        System.out.println("服务器端已经启动，监听的端口为：" + HostInfo.PORT);
        boolean flag = true;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        while (flag) {
            Socket socket = serverSocket.accept();
            executorService.submit(new EchoClientHandler(socket));
        }
        executorService.shutdown();
        serverSocket.close();

    }

    public static class EchoClientHandler implements Runnable {

        /**
         * 每一个客户端都需要启动一个任务(task)来执行。
         */
        private Socket client;

        private Scanner scanner;

        private PrintStream out;

        /**
         * 循环标记
         */
        private boolean flag = true;

        public EchoClientHandler(Socket client){
            this.client = client;
            try {
                this.scanner = new Scanner(this.client.getInputStream());
                this.scanner.useDelimiter("\n");
                this.out = new PrintStream(this.client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (this.flag) {
                if (this.scanner.hasNext()) {
                    String var = this.scanner.next().trim();
                    System.out.println("{服务器端}："+var);
                    if ("byebye".equals(var)) {
                        this.out.println("BYE BYE BYE");
                        this.flag = false;
                    } else {
                        this.out.println("【ECHO】:"+var);
                    }
                }
            }
            this.scanner.close();
            this.out.close();
            try {
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
