package com.wdy.autosubmit.function;

import com.wdy.autosubmit.pojo.User;
import javafx.application.Application;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author WDY
 * @Date 2020-05-09 10:24
 * @Description TODO
 */
public class Submit {
    public  void submitForm(List<User> users, JavaMailSenderImpl mailSender) {
        CookieStore cookieStore = new BasicCookieStore();
        String loginUrl = "http://authserver.aynu.edu.cn/authserver/login?service=https%3A%2F%2Faynu.campusphere.net%2Fportal%2Flogin";
        Login login = new Login();
        Service service = new Service();
            //获取登录后带cookie的httpclient
            users.forEach(user->{
                boolean loginStatus = false;
                try {
                    CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
                    loginStatus = login.login(httpClient, cookieStore, loginUrl, user.getUsername(), user.getPassword());
                    if (loginStatus) {
                        service.submitForm(httpClient,user,mailSender);
                        cookieStore.clear();
                        httpClient.close();
                    }
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){}


            });


    }
    public boolean login(User user){

        Login login = new Login();
        boolean flag = false;
        CookieStore cookieStore = new BasicCookieStore();
        String loginUrl = "http://authserver.aynu.edu.cn/authserver/login?service=https%3A%2F%2Faynu.campusphere.net%2Fportal%2Flogin";
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        try {
            flag = login.login(httpClient,cookieStore,loginUrl,user.getUsername(),user.getPassword());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return flag;
    }

}
