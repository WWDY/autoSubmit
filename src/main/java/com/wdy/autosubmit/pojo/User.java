package com.wdy.autosubmit.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.Email;

/**
 * @Author WDY
 * @Date 2020-05-11 15:19
 * @Description TODO
 */
@Data
@TableName("user")
public class User {
    @TableId
    private String username;
    private String password;
    @TableField(value = "emailAdress")
    private String emailAdress;
    private String adress;

}
