package com.sinjee.exceptions;

import com.sinjee.common.ResultVOUtil;
import com.sinjee.vo.ResultVO;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = TokenException.class)
    @ResponseBody
    public ResultVO handlerException(MyException e,Object object) {
        log.error("Exception: ", e);
        return ResultVOUtil.error(e.getCode(), e.getMessage(),object);
    }

}
