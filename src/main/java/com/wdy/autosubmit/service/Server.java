package com.wdy.autosubmit.service;

import com.wdy.autosubmit.Dao.UserMapper;
import com.wdy.autosubmit.function.Service;
import com.wdy.autosubmit.function.Submit;
import com.wdy.autosubmit.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author WDY
 * @Date 2020-05-11 14:38
 * @Description TODO
 */
@Controller
public class Server {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @PostMapping(value = "/api/saveData")
    @ResponseBody
    public String getUser(@RequestBody User user){
        if (user.getUsername()!=null){
            System.out.println(user);
            if(new Submit().login(user)){
                List<User> userList = this.getUserList();
                for (User u : userList) {
                    if (u.getUsername().equals(user.getUsername())) {
                        return "请勿重复提交";
                    }
                }
                userMapper.insert(user);
                return "提交成功";
            }else {
                return "学号或密码错误，请重新提交";
            }

        }else {
            return "学号或密码错误，请重新提交";
        }
    }

    /**
     * 秒，分，时，日期，月份，星期
     */
    @Scheduled(cron = "0 0 6 * * *")
    public void submit(){
        Submit submit = new Submit();
        try{
            submit.submitForm(this.getUserList(),mailSender);
        }catch (Exception e){}

    }

    public List<User> getUserList(){
        List<User> users = userMapper.selectList(null);
        return users;
    }

}
