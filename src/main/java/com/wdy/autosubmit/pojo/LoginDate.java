package com.wdy.autosubmit.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author WDY
 * @Date 2020-05-09 12:56
 * @Description TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDate {
    private String lt;
    private String dllt;
    private String execution;
    private String _eventId;
    private String rmShown;
    private String pwdDefaultEncryptSalt;
}
