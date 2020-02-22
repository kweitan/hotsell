package com.sinjee.exceptions;

import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 小小极客
 * 时间 2019/12/15 12:13
 * @ClassName ExceptionHandler
 * 描述 统一异常处理
 **/
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ResultVO handlerSellerException(MyException e) {
        log.error("Exception: ", e);
        log.info("Exception message = {}",e.getMessage());
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = TokenException.class)
    @ResponseBody
    public ResultVO handlerException(MyException e,Object object) {
        log.error("Exception: ", e);
        return ResultVOUtil.error(e.getCode(), e.getMessage(),object);
    }

    /**
     * 捕获登录异常处理 跳转登录界面
     * @return
     */
    @ExceptionHandler(value = SellerAuthorizeException.class)
    @ResponseBody
    public ResultVO handlerAuthorizeException() {
        return ResultVOUtil.error(911,"已经过期,重新登录请求token");
    }

    @ExceptionHandler(value = LimitAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleResponseBankException() {

    }

}
