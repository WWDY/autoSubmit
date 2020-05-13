package com.wdy.autosubmit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wdy.autosubmit.Dao.UserMapper;
import com.wdy.autosubmit.function.Service;
import com.wdy.autosubmit.function.Submit;
import com.wdy.autosubmit.service.Server;
import jdk.nashorn.internal.ir.CallNode;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;

@SpringBootTest
class AutosubmitApplicationTests {

    @Test
    void test(){

    }


}
