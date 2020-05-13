package com.wdy.autosubmit.function;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wdy.autosubmit.pojo.FormDatas;
import com.wdy.autosubmit.pojo.User;
import com.wdy.autosubmit.pojo.queryCollectData;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.alibaba.fastjson.serializer.SerializerFeature.*;

/**
 * @Author WDY
 * @Date 2020-05-09 17:03
 * @Description TODO
 */
public class Service {


    private queryCollectData rowsData = null;
    private FormDatas formDatas = null;

    //查询是否有未提交任务
    public boolean getFrom(HttpClient Client){

        HttpClient httpClient = Client;
        //查询最新待提交表单接口
        String queryCollectWidUrl = "https://aynu.campusphere.net/wec-counselor-collector-apps/stu/collector/queryCollectorProcessingList";

        HttpPost httpPost = new HttpPost(queryCollectWidUrl);
        httpPost.setHeader("Accept","application/json, text/plain, */*");
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 10; CLT-AL01 Build/HUAWEICLT-AL01; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/79.0.3945.116 Mobile Safari/537.36  cpdaily/8.1.14 wisedu/8.1.14");
        httpPost.setHeader("content-type","application/json");
        httpPost.setHeader("Accept-Encoding","gzip,deflate");
        httpPost.setHeader("Accept-Language","zh-CN,en-US;q=0.8");
        httpPost.setHeader("Content-Type","application/json;charset=UTF-8");

        JSONObject params = new JSONObject();
        params.put("pageSize",6);
        params.put("pageNumber",1);

        //接受响应的json数据
        JSONObject response = null;

        try {
            StringEntity stringEntity = new StringEntity(params.toString());
            stringEntity.setContentEncoding("UTF-8");
            //发送json数据需要设置contentTyp
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            HttpResponse res = httpClient.execute(httpPost);
            // 返回json格式：
            String result = EntityUtils.toString(res.getEntity());
            response = JSONObject.parseObject(result);

            JSONObject datas = response.getJSONObject("datas");


            JSONArray rows = datas.getJSONArray("rows");
            if(rows.size()>=1){
                rowsData = rows.getObject(0, queryCollectData.class);
            }

            if (rows.size() < 1) {
                System.out.println("当前暂无问卷提交任务");
                return false;
            } else if (rowsData.getIsHandled() .equals("1") ) {
                System.out.println("今日已提交");
                return false;
            } else {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
    //处理未提交表单
    public void detailCollector(HttpClient Client,String adress){

        String detailCollectorUrl = "https://aynu.campusphere.net/wec-counselor-collector-apps/stu/collector/detailCollector";
        HttpPost httpPost = new HttpPost(detailCollectorUrl);
        httpPost.setHeader("Accept","application/json, text/plain, */*");
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 10; CLT-AL01 Build/HUAWEICLT-AL01; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/79.0.3945.116 Mobile Safari/537.36  cpdaily/8.1.14 wisedu/8.1.14");
        httpPost.setHeader("content-type","application/json");
        httpPost.setHeader("Accept-Encoding","gzip,deflate");
        httpPost.setHeader("Accept-Language","zh-CN,en-US;q=0.8");
        httpPost.setHeader("Content-Type","application/json;charset=UTF-8");

        JSONObject datas = new JSONObject();
        datas.put("collectorWid",rowsData.getWid());

        StringEntity stringEntity = null;
        try {

            stringEntity = new StringEntity(datas.toString());
            stringEntity.setContentEncoding("UTF-8");
            //发送json数据需要设置contentTyp
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);

            HttpResponse response = Client.execute(httpPost);
            JSONObject formData = JSONObject.parseObject(EntityUtils.toString(response.getEntity()));

            JSONObject collectorDatas = formData.getJSONObject("datas").getJSONObject("collector");

            //定位地址
            String address = adress;

            String schoolTaskWid = collectorDatas.getString("schoolTaskWid");

            FormDatas formDatas1 = new FormDatas();
            formDatas1.setAddress(address);
            formDatas1.setSchoolTaskWid(schoolTaskWid);
            formDatas = formDatas1;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //获取待提交表单数据
    public JSONArray getFormFields(HttpClient httpClient){

        JSONArray jsonArray = null;

        String getFormFieldsUrl = "https://aynu.campusphere.net/wec-counselor-collector-apps/stu/collector/getFormFields";

        HttpPost httpPost = new HttpPost(getFormFieldsUrl);
        httpPost.setHeader("Accept","application/json, text/plain, */*");
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 10; CLT-AL01 Build/HUAWEICLT-AL01; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/79.0.3945.116 Mobile Safari/537.36  cpdaily/8.1.14 wisedu/8.1.14");
        httpPost.setHeader("content-type","application/json");
        httpPost.setHeader("Accept-Encoding","gzip,deflate");
        httpPost.setHeader("Accept-Language","zh-CN,en-US;q=0.8");
        httpPost.setHeader("Content-Type","application/json;charset=UTF-8");

        JSONObject params = new JSONObject();
        params.put("pageSize","30");//当前我们需要问卷选项有19个，pageSize可适当调整
        params.put("pageNumber","1");
        params.put("formWid",rowsData.getFormWid());
        params.put("collectorWid",rowsData.getWid());



        try {

            StringEntity stringEntity = new StringEntity(params.toString());
            stringEntity.setContentType("application/json");
            stringEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(stringEntity);

            HttpResponse response = httpClient.execute(httpPost);

            String res = EntityUtils.toString(response.getEntity());

            JSONObject formList = JSONObject.parseObject(res);

            //表单数据
            jsonArray = formList.getJSONObject("datas").getJSONArray("rows");

            //过滤表单数据
            for (int i = 0; i < jsonArray.size(); i++) {

                JSONObject obj = jsonArray.getJSONObject(i);
                //过滤fieldItems，既选择题目
                JSONArray fieldItems = obj.getJSONArray("fieldItems");

                if(fieldItems.size()>=1){
                    for (int j = 0; j < fieldItems.size(); j++) {
                        if (fieldItems.getJSONObject(j).getInteger("isSelected")==null) {
                            Object fieldItems1 = jsonArray.getJSONObject(i).getJSONArray("fieldItems").remove(j);
                        }
                    }
                }
                //过滤多选题
                if (obj.getString("colName").equals("field007")) {
                    jsonArray.getJSONObject(i).getJSONArray("fieldItems").clear();
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }


    public void  submitForm(HttpClient httpClient, User user, JavaMailSenderImpl mailSender){
        System.out.println("submitForm");
        boolean flag = this.getFrom(httpClient);
        if (flag == false) {
            return;
        }
        this.detailCollector(httpClient,user.getAdress());
        JSONArray form = this.getFormFields(httpClient);


        String submitUrl = "https://aynu.campusphere.net/wec-counselor-collector-apps/stu/collector/submitForm";
        HttpPost httpPost = new HttpPost(submitUrl);
        httpPost.setHeader("CpdailyStandAlone","0");
        httpPost.setHeader("User-Agent","Mozilla/5.0 (Linux; Android 10; CLT-AL01 Build/HUAWEICLT-AL01; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/79.0.3945.116 Mobile Safari/537.36  cpdaily/8.1.14 wisedu/8.1.14");
        httpPost.setHeader("Content-Type","application/json; charset=UTF-8");
        httpPost.setHeader("extension","1");
        httpPost.setHeader("Accept-Encoding","gzip");
        httpPost.setHeader("Cpdaily-Extension", "7vGWlfMTok9XC2Sz+EGOWdIzrLu9t82o6JZIBNMmvdF8BryaXycAggrfdVS1 osAYMwJ0RR5GSkMKyjqumYvvI7JHcC1rSHQ4olTVG1rkP5sYaDOyq+He2xUh tdgR9Ydcdmqf/zHivo3pdgqtDCpx9CAO9meEZDptptwteeuwL553IJWPH5Hr g3n1rX7j2jSlbrpkhwDCcnXrNxkbLIeYN0fOxHZT6SS4V2k4IS/cwgTUR0Xt lXD6Yti/Wbkt+bY9gacP3Oue9ZQ=");
        httpPost.setHeader("Host","aynu.campusphere.net");
        httpPost.setHeader("Connection","Keep-Alive");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formWid",rowsData.getFormWid());
        jsonObject.put("address",formDatas.getAddress());
        jsonObject.put("collectWid",rowsData.getWid());
        jsonObject.put("schoolTaskWid",formDatas.getSchoolTaskWid());
        jsonObject.put("form",form);

        StringEntity stringEntity = null;



        try {

            stringEntity = new StringEntity(jsonObject.toJSONString(),"UTF-8");
            stringEntity.setContentType("application/json");
            stringEntity.setContentEncoding("UTF-8");


                httpPost.setEntity(stringEntity);
                HttpResponse response = httpClient.execute(httpPost);
                String jsonBody = EntityUtils.toString(response.getEntity());
                JSONObject message = JSONObject.parseObject(jsonBody);
                String status = message.getString("message");
                if("SUCCESS".equals(status)){
                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setSubject("今日校园提交成功");
                    mailMessage.setText("今日提交成功！24小时后，脚本将再次自动提交");
                    mailMessage.setTo(user.getEmailAdress());
                    mailMessage.setFrom("wdy668@vip.qq.com");
                    mailSender.send(mailMessage);
                    System.out.println("今日提交成功！24小时后，脚本将再次自动提交");
                } else if ("该收集已填写无需再次填写".equals(status)) {
                    System.out.println("该收集已填写无需再次填写");
                } else {
                    SimpleMailMessage mailMessage = new SimpleMailMessage();
                    mailMessage.setSubject("今日校园提交出错，请手动提交！！！");
                    mailMessage.setText("今日校园提交出错，请手动提交！！！");
                    mailMessage.setTo(user.getEmailAdress());
                    mailMessage.setFrom("wdy668@vip.qq.com");
                    mailSender.send(mailMessage);
                    System.out.println("提交出错，请手动提交！！！");
                }


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }  catch (Exception e) {
                e.printStackTrace();
            }



    }

}
