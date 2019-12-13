package com.sinjee;

import com.sinjee.common.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 小小极客
 * 时间 2019/12/13 22:05
 * @ClassName RedisTest
 * 描述 RedisTest
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    //注入Redis
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void set(){
        redisUtil.setString("keyword","您好啊");
        System.out.println(redisUtil.getString("keyword"));
    }
}
