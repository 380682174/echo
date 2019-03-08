package cn.fish.msgpack;

import cn.fish.vo.Member;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author devin.jiang
 * @CreateDate 2019/3/8 14:52
 */
public class MessagePackDemoA {

    public static void main(String[] args) throws IOException {

        List<Member> allMembers = new ArrayList<Member>() ;
        for(int x = 0 ; x < 10 ; x ++) {
            Member member = new Member() ;
            member.setMid("FISH - " + x);
            member.setName("Hello - " + x);
            member.setAge(10);
            member.setSalary(1.1);
            allMembers.add(member) ;
        }

        MessagePack messagePack = new MessagePack();
        byte[] datas = messagePack.write(allMembers);
        System.out.println(datas.length);
        List<Member> members = messagePack.read(datas,Templates.tList(messagePack.lookup(Member.class)));
        System.out.println(members);


    }

}
