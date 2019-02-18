package cn.fish.bio.client;

import cn.fish.info.HostInfo;
import cn.fish.info.InputUtil;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/2/18 18:28
 */
public class BIOEchoClient {

    public static void main(String[] args) throws IOException {

        /**定义连接的主机信息*/
        Socket client = new Socket(HostInfo.HOST_NAME,HostInfo.PORT);
        /**获取服务器端的响应数据*/
        Scanner scanner = new Scanner(client.getInputStream());
        scanner.useDelimiter("\n");
        /**向服务器端发送信息内容*/
        PrintStream out = new PrintStream(client.getOutputStream());
        boolean flag = true;
        while (flag) {
            String inputData = InputUtil.getString("请输入要发送的数据：").trim();
            /**把数据发送到服务器端上*/
            out.println(inputData);
            if (scanner.hasNext()) {
                String var = scanner.next().trim();
                System.out.println(var);
            }
            if ("byebye".equalsIgnoreCase(inputData)) {
                flag = false ;
            }
        }
        client.close();

    }

}
