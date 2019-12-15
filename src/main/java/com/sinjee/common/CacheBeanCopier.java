package com.sinjee.common;

import org.springframework.cglib.beans.BeanCopier;

import java.util.HashMap;
import java.util.Map;

/**
 * CacheBeanCopier -> BeanCopier 秒杀 BeanUtils
 * 属性拷贝 通过缓存实现高效拷贝
 * @author 小小极客
 * 时间 2019/10/12 22:47
 **/
public class CacheBeanCopier {

    static Map<String, BeanCopier> CACHE_BEANCOPIER_MAP = new HashMap<>() ;

    private static String getKey(Class<?> sourcClass,Class<?> destClass){
        return sourcClass.getName()+destClass.getName() ;
    }

    public static void copy(Object sourceObject,Object destObject){
        if(sourceObject != null && destObject != null){
            String key = getKey(sourceObject.getClass(),destObject.getClass()) ;
            BeanCopier beanCopier = null ;
            if (CACHE_BEANCOPIER_MAP.containsKey(key)){
                beanCopier = CACHE_BEANCOPIER_MAP.get(key) ;
            }else {
                beanCopier = BeanCopier.create(sourceObject.getClass(),destObject.getClass(),
                        false) ;
                CACHE_BEANCOPIER_MAP.put(key,beanCopier) ;
            }
            beanCopier.copy(sourceObject,destObject,null);
        }
    }
}
