package cn.fish.nio.client;

import cn.fish.info.HostInfo;
import cn.fish.info.InputUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/2/18 19:33
 */
public class NIOEchoClient {

    public static void main(String[] args) throws IOException {
        // 打开客户端连接通道
        SocketChannel socketChannel = SocketChannel.open();
        //连接
        socketChannel.connect(new InetSocketAddress(HostInfo.HOST_NAME,HostInfo.PORT));
        // 开辟缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(500);
        boolean flag = true;
        while (flag) {
            byteBuffer.clear();
            String inputData = InputUtil.getString("请输入要发送的数据：").trim();
            // 将输入的数据保存在缓冲区之中
            byteBuffer.put(inputData.getBytes());
            // 重置缓冲区
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            //在读之前清空缓冲区
            byteBuffer.clear();
            int readCount = socketChannel.read(byteBuffer);
            // 重置缓冲区
            byteBuffer.flip();
            String message = new String(byteBuffer.array(),0,readCount);
            System.out.println(message);
            if("byebye".equalsIgnoreCase(inputData)) {
                flag = false ;
            }
        }
        socketChannel.close();

    }


}
