package com.wdy.autosubmit.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;


/**
 * @Author WDY
 * @Date 2020-05-11 14:32
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpClientWithCookieStore {
    private CloseableHttpClient httpClient;
    private CookieStore cookieStore;
}
