package com.wdy.autosubmit.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author WDY
 * @Date 2020-05-11 12:22
 * @Description TODO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FormDatas implements Serializable {
    private String address;
    private String schoolTaskWid;
}
