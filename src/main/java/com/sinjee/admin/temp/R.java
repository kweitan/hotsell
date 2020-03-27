package com.sinjee.admin.temp;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建时间 2020 - 03 -27
 * 统一结果类
 * 外界只可以调用统一返回类的方法，不可以直接创建，因此构造器私有；
 * 内置静态方法，返回对象；
 * 为便于自定义统一结果的信息，建议使用链式编程，将返回对象设类本身，即return this;
 * 响应数据由于为json格式，可定义为JsonObject或Map形式；
 *
 * 使用例子
 *
 * @RestController
 * @RequestMapping("/api/v1/users")
 * public class TeacherAdminController {
 *
 *     @Autowired
 *     private UserService userService;
 *
 *     @GetMapping
 *     public R list() {
 *         List<Teacher> list = teacherService.list(null);
 *         return R.ok().data("itms", list).message("用户列表");
 *     }
 * }
 *
 *
 * @author kweitan
 */
@Data
public class R {
    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<>();

    // 构造器私有
    private R(){}

    // 通用返回成功
    public static R ok() {
        R r = new R();
        r.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        r.setCode(ResultCodeEnum.SUCCESS.getCode());
        r.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return r;
    }

    // 通用返回失败，未知错误
    public static R error() {
        R r = new R();
        r.setSuccess(ResultCodeEnum.UNKNOWN_ERROR.getSuccess());
        r.setCode(ResultCodeEnum.UNKNOWN_ERROR.getCode());
        r.setMessage(ResultCodeEnum.UNKNOWN_ERROR.getMessage());
        return r;
    }

    // 设置结果，形参为结果枚举
    public static R setResult(ResultCodeEnum result) {
        R r = new R();
        r.setSuccess(result.getSuccess());
        r.setCode(result.getCode());
        r.setMessage(result.getMessage());
        return r;
    }

    /**------------使用链式编程，返回类本身-----------**/

    // 自定义返回数据
    public R data(Map<String,Object> map) {
        this.setData(map);
        return this;
    }

    // 通用设置data
    public R data(String key,Object value) {
        this.data.put(key, value);
        return this;
    }

    // 自定义状态信息
    public R message(String message) {
        this.setMessage(message);
        return this;
    }

    // 自定义状态码
    public R code(Integer code) {
        this.setCode(code);
        return this;
    }

    // 自定义返回结果
    public R success(Boolean success) {
        this.setSuccess(success);
        return this;
    }
}