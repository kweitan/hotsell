package com.sinjee.admin.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.sinjee.common.KeyUtil;
import com.sinjee.common.MathUtil;
import com.sinjee.common.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * 创建时间 2020 - 02 -19
 * Captcha测试controller
 * @author kweitan
 */
@Controller
@RequestMapping("/admin")
@Slf4j
public class CaptchaController {

    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Autowired
    RedisUtil redisUtil ;

    /**
     * 显示验证码
     * @param request
     * @param response
     * @throws Exception
     */
    @CrossOrigin(origins = "*")
    @RequestMapping("/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //验证码
            String createText = defaultKaptcha.createText();
            log.info("验证码={}",createText);
            //验证码存放到redis 有效期2分钟
            String ip = KeyUtil.getIpAddr(request) ;
            log.info("ip={}",ip);
            redisUtil.setString(KeyUtil.getIpAddr(request),createText,120);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

}
