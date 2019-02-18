package cn.fish.nio.server;

import cn.fish.info.HostInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/2/18 18:54
 */
public class NIOEchoServer {

    private static class EchoClientHandler implements Runnable {

        /**客户端通道**/
        private SocketChannel clientChannel;
        /**循环标记*/
        private boolean flag = true;

        public EchoClientHandler(SocketChannel clientChannel) {
            this.clientChannel = clientChannel;
            // 严格意义上来讲，当已经成功的连接上了服务器，并且需要进行进一步处理之前要发送一些消息给客户端
        }

        public void run() {
            /**申请50个缓冲区*/
            ByteBuffer byteBuffer = ByteBuffer.allocate(500);

            try {
                //需要不断进行交互
                while (this.flag) {
                    byteBuffer.clear();
                    /**向缓冲区中读取数据*/
                    int readCount = this.clientChannel.read(byteBuffer);
                    String readMessage = new String(byteBuffer.array(),0,readCount).trim();
                    System.out.println("{服务端：}"+readMessage);
                    // 回应数据信息
                    String writeMessage = "【ECHO】" + readMessage + "\n" ;
                    if ("byebye".equalsIgnoreCase(readMessage)) {
                        writeMessage = "【EXIT】拜拜，下次再见！" ;
                        flag = false;
                    }
                    //数据输入通过缓存的形式完成，而数据的输出同样需要进行缓存操作
                    byteBuffer.clear();
                    //发送内容
                    byteBuffer.put(writeMessage.getBytes());
                    //重置缓冲区
                    byteBuffer.flip();
                    this.clientChannel.write(byteBuffer);
                }
                this.clientChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) throws IOException {
        // 1、NIO的实现考虑到性能的问题以及响应时间问题，需要设置一个线程池，采用固定大小的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 2、NIO的处理是基于Channel控制的，所以有一个Selector就是负责管理所有的Channel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 3、需要为其设置一个非阻塞的状态机制
        serverSocketChannel.configureBlocking(false);
        // 4、服务器上需要提供有一个网络的监听端口
        serverSocketChannel.bind(new InetSocketAddress(HostInfo.PORT));
        // 5、需要设置一个Selector，作为一个选择器的出现，目的是管理所有的Channel
        Selector selector = Selector.open();
        // 6、将当前的Channel注册到Selector之中
        // 连接时处理
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        System.out.println("服务器已经启动成功，服务器的监听端口为：" + HostInfo.PORT);
        // 7、NIO采用的是轮询模式，每当发现有用户连接的时候就需要启动一个线程（线程池管理）
        /**接收轮询状态*/
        int selectKey = 0;
        //实现实时轮询
        while ((selectKey = selector.select()) > 0) {
            // 获取全部的Key
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                // 获取每一个Key的信息
                SelectionKey selectionKey = selectionKeyIterator.next();
                // 为连接模式
                if (selectionKey.isAcceptable()) {
                    // 等待连接
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    if (socketChannel != null) {
                        executorService.submit(new EchoClientHandler(socketChannel));
                    }
                }
                selectionKeyIterator.remove();
            }
        }
        executorService.shutdown();
        serverSocketChannel.close();
    }

}
