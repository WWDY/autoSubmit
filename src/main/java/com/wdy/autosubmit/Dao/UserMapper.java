package com.wdy.autosubmit.Dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wdy.autosubmit.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Author WDY
 * @Date 2020-05-11 15:15
 * @Description TODO
 */

public interface UserMapper extends BaseMapper<User> {
}
