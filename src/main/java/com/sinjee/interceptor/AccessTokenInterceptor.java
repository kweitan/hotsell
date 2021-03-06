package com.sinjee.interceptor;

import com.sinjee.annotation.AccessTokenIdempotency;
import com.sinjee.common.Constant;
import com.sinjee.common.RedisUtil;
import com.sinjee.exceptions.MyException;
import com.sinjee.wechat.dto.BuyerInfoDTO;
import com.sinjee.wechat.service.BuyerInfoService;
import com.sinjee.wechat.utils.WechatAccessTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author 小小极客
 * 时间 2020/2/3 9:29
 * @ClassName AccessTokenInterceptor
 * 描述 AccessTokenInterceptor
 **/
@Slf4j
@Component
public class AccessTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisUtil redisUtil ;

    @Autowired
    private BuyerInfoService buyerInfoService ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        log.info("进入拦截器AccessTokenInterceptor");

        // 如果不是映射到方法直接通过
        if(!(object instanceof HandlerMethod)){
            return true;
        }
        String accessToken = request.getHeader("accessToken");// 从 http 请求头中取出 token
        log.info("accessToken={}",accessToken);
        HandlerMethod handlerMethod=(HandlerMethod)object;
        Method method=handlerMethod.getMethod();

        //检查是否有AccessTokenIdempotency注释，否跳过认证
        AccessTokenIdempotency accessTokenIdempotency = method.getAnnotation(AccessTokenIdempotency.class) ;
        if (null != accessTokenIdempotency){
            if (StringUtils.isBlank(accessToken)){
                throw new MyException(201,"请授权登录");
            }

            Map<String,Object> map = WechatAccessTokenUtil.getMap(accessToken) ;

            //校验openid
            String openid = (String)map.get("openid") ;
            //String key = HashUtil.signByMD5(openid,md5Salt);

            boolean isExist = redisUtil.existsKey(openid);
            if (!isExist){

                //从数据库查
                BuyerInfoDTO buyerInfoDTO = buyerInfoService.find(openid) ;
                if (null == buyerInfoDTO || StringUtils.isBlank(buyerInfoDTO.getOpenId())){
                    throw new MyException(201,"请还未授权登录过");
                }

                //重新更新redis
                redisUtil.setString(openid,buyerInfoDTO, Constant.Redis.EXPIRE_TIME_7DAY) ;
            }

            //205 表示token日期失效 重新刷新token 告诉微信小程序刷新token
            Date lastDate = (Date)map.get("expiresTime") ;
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00")) ;
            calendar.setTime(lastDate);
            long lastTime = calendar.getTimeInMillis() ;

            //表示已经过期
            if (System.currentTimeMillis() > lastTime){
                throw new MyException(205,"已经过期,重新请求token");
            }

            request.setAttribute("openid",openid);


        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
