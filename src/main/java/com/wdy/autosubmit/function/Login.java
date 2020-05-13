package com.wdy.autosubmit.function;

import com.wdy.autosubmit.pojo.LoginDate;
import com.wdy.autosubmit.pojo.User;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.message.BasicNameValuePair;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author WDY
 * @Date 2020-05-09 13:19
 * @Description TODO
 */
public class Login {

    public boolean login(HttpClient httpClient,CookieStore cookieStore,String loginUrl, String username, String password) throws NoSuchPaddingException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, InvalidAlgorithmParameterException {

        HttpGet httpGet = new HttpGet(loginUrl);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.16 Safari/537.36 Edg/79.0.309.12");
        HttpResponse getResponse = httpClient.execute(httpGet);
        InputStream content = getResponse.getEntity().getContent();


        //登录
        HttpPost httpPost = new HttpPost(loginUrl);
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.16 Safari/537.36 Edg/79.0.309.12");
        LoginDate date = new GetLoginDate().getDate(loginUrl,content);
        //加密后的密码
        String passwd = new encryptAES().getPassword(password, date.getPwdDefaultEncryptSalt());
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username",username));
        nvps.add(new BasicNameValuePair("password",passwd));
        nvps.add(new BasicNameValuePair("lt",date.getLt()));
        nvps.add(new BasicNameValuePair("dllt",date.getDllt()));
        nvps.add(new BasicNameValuePair("execution",date.getExecution()));
        nvps.add(new BasicNameValuePair("_eventId",date.get_eventId()));
        nvps.add(new BasicNameValuePair("rmShown",date.getRmShown()));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));

        HttpResponse response = httpClient.execute(httpPost);
        String location = "";
        try {
            location = response.getFirstHeader("Location").getValue();
        }catch (Exception e){}
        System.out.println(location);
        if(location==null){
            return false;
        }
        HttpGet httpGet1 = new HttpGet(location);
        try {
            httpClient.execute(httpGet1);
        } catch (Exception e) {}

        List<Cookie> cookies = cookieStore.getCookies();
        AtomicInteger flag = new AtomicInteger(0);
        cookies.forEach((k)->{
            if ("MOD_AUTH_CAS".equals(k.getName())) {
                flag.set(1);
            }
        });

        if (flag.get() == 1) {
            System.out.println("登陆成功");
            return true;
        }else{
            System.out.println("登录失败！！！");
            return false;
        }

    }

}
