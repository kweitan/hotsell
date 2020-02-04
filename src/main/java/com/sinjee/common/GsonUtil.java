package com.sinjee.common;

import com.google.gson.Gson;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间 2020 - 01 -19
 * 单例模式 只有第一次调用getInstance方法时，虚拟机才加载 Inner 并初始化instance ，
 * 只有一个线程可以获得对象的初始化锁，其他线程无法进行初始化，保证对象的唯一性。
 * 目前此方式是所有单例模式中最推荐的模式
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

    public <T> List<T> parseString2List(String result,Class clazz){
        Type type = new ParameterizedTypeImpl(clazz);
        return this.gson.fromJson(result,type);
    }

    private static class Inner {
        private static final GsonUtil instance = new GsonUtil();
    }

    private  class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

}
