package com.sinjee.common;

import com.google.gson.Gson;

/**
 * 创建时间 2020 - 01 -19
 * 单例模式 只有第一次调用getInstance方法时，虚拟机才加载 Inner 并初始化instance ，只有一个线程可以获得对象的初始化锁，其他线程无法进行初始化，保证对象的唯一性。目前此方式是所有单例模式中最推荐的模式
 * @author kweitan
 */
public class GsonUtil {

    private Gson gson ;

    private GsonUtil(){
        this.gson = new Gson() ;
    }

    public static GsonUtil getInstance(){
        return Inner.instance;
    }

    public String toStr(Object src){
        return this.gson.toJson(src) ;
    }

    private static class Inner {
        private static final GsonUtil instance = new GsonUtil();
    }

}
