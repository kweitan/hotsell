package com.sinjee.common;

import com.sinjee.vo.ResultVO;

/**
 * @author 小小极客
 * 时间 2019/12/15 12:14
 * @ClassName ResultVOUtil
 * 描述 ResultVOUtil工具类
 **/
public class ResultVOUtil {
    public static ResultVO success(Object object) {
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        return resultVO;
    }

    public static ResultVO success() {
        return success(null);
    }

    public static ResultVO error(Integer code, String msg) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMessage(msg);
        return resultVO;
    }
}
