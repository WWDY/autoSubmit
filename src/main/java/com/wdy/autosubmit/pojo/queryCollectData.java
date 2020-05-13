package com.wdy.autosubmit.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author WDY
 * @Date 2020-05-11 11:16
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class queryCollectData implements Serializable {
    private String formWid;
    private String isHandled;
    private String priority;
    private String isRead;
    private String wid;
    private String currentTime;
    private String createTime;
    private String subject;
    private String endTime;
    private String senderUserName;
    private String content;

}
