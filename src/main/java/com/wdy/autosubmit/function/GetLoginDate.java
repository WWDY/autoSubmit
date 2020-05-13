package com.wdy.autosubmit.function;


import com.wdy.autosubmit.pojo.LoginDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * @Author WDY
 * @Date 2020-05-09 11:35
 * @Description TODO
 */
public class GetLoginDate {
    public LoginDate getDate(String loginUrl, InputStream content) throws IOException, NoSuchPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        LoginDate loginDate = new LoginDate();

        Document dom = Jsoup.parse(content,"utf-8",loginUrl);

        Elements elements = dom.getElementsByTag("input");
        //System.out.println(elements);
        String pwdDefaultEncryptSalt = dom.getElementById("pwdDefaultEncryptSalt").val();
        loginDate.setPwdDefaultEncryptSalt(pwdDefaultEncryptSalt);
        Elements input = elements.select("[type='hidden']");
        input.forEach(k->{
            String name = k.attr("name");
            String value = k.attr("value");
            if("lt".equals(name)){
                loginDate.setLt(value);
            } else if ("dllt".equals(name)) {
                loginDate.setDllt(value);
            } else if ("execution".equals(name)) {
                loginDate.setExecution(value);
            } else if ("_eventId".equals(name)) {
                loginDate.set_eventId(value);
            } else if ("rmShown".equals(name)) {
                loginDate.setRmShown(value);
            }

        });


        return loginDate;
    }
}
