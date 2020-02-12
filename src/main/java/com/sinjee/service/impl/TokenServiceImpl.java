package com.sinjee.service.impl;

import com.sinjee.common.Constant;
import com.sinjee.common.RedisUtil;
import com.sinjee.common.UUIDUtil;
import com.sinjee.exceptions.MyException;
import com.sinjee.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 小小极客
 * 时间 2020/2/1 22:07
 * @ClassName TokenServiceImpl
 * 描述 token服务实现类
 **/
@Service
@Slf4j
public class TokenServiceImpl implements TokenService {

    private static final String TOKEN_NAME = "token";

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String createToken() {
        String str = UUIDUtil.genUUID() ;
        StringBuffer token = new StringBuffer();
        token.append(Constant.Redis.TOKEN_PREFIX).append(str);
        redisUtil.setString(token.toString(), token.toString(), Constant.Redis.EXPIRE_TIME_2MINUTE) ;
        return token.toString();
    }

    @Override
    public void checkToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_NAME);
        log.info("toke={}",token);
        //1.token是否带过来
        if (StringUtils.isBlank(token)) {// header中不存在token
            token = request.getParameter(TOKEN_NAME);
            if (StringUtils.isBlank(token)) {// parameter中也不存在token
                throw new MyException(110,"parameter中也不存在token");
            }
        }

        //2.token是否存在
        if (!redisUtil.existsKey(token)){
            throw new MyException(111,"请勿重复提交");
        }

        boolean success = redisUtil.deleteKey(token) ;
        if (!success){
            throw new MyException(111,"请勿重复提交");
        }

    }
}
