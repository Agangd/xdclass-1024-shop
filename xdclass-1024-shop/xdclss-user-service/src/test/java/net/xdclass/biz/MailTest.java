package net.xdclass.biz;


import lombok.extern.slf4j.Slf4j;
import net.xdclass.UserApplication;
import net.xdclass.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class MailTest {

    @Autowired
    private MailService mailService;

    @Test
    public void sendMailTest(){

        mailService.sendMail("1638670400@qq.com","漂亮姐姐","漂亮姐姐郑美瑶");
    }
}
