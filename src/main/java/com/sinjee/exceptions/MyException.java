package com.sinjee.exceptions;

import com.sinjee.enums.ResultEnum;
import lombok.Getter;

/**
 * @author 小小极客
 * 时间 2019/12/13 22:34
 * @ClassName MyException
 * 描述 自定义异常处理
 **/
@Getter
public class MyException extends RuntimeException{
    private Integer code;

    public MyException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public MyException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
