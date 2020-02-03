package com.sinjee.exceptions;

import com.sinjee.enums.ResultEnum;
import lombok.Getter;

/**
 * @author 小小极客
 * 时间 2020/2/3 14:01
 * @ClassName TokenException
 * 描述 TokenException
 **/
@Getter
public class TokenException extends RuntimeException{
    private Integer code;

    public TokenException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());

        this.code = resultEnum.getCode();
    }

    public TokenException(Integer code, String message,Object object) {
        super(message);
        this.code = code;
    }
}
