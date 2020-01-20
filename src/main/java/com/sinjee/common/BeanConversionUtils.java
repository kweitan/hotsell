package com.sinjee.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName BeanConversionUtils
 * @Description 属性拷贝工具类
 * @Author pc
 * @Date 2019/10/6 1:00
 * @Version 1.0
 **/
@Slf4j
public class BeanConversionUtils {

    /**
     * list -> another list
     * @param target 目标List中实体类
     * @param sourceList 源List
     * @param <T>
     * @param <F>
     * @return
     */
    public static <T,F> List<F> listCopyToAnotherList(Class<F> target, List<T> sourceList){
        if (!CollectionUtils.isEmpty(sourceList)) {
            List<F> targetList = new ArrayList();
            try{
                for (T t : sourceList) {
                    F f = target.newInstance();
                    BeanUtils.copyProperties(t,f);
                    targetList.add(f) ;
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
            return targetList ;
        }else {
            return null ;
        }
    }

    /** BeanCopier*/
    public static <T,F> List<F> copyToAnotherList(Class<F> target, List<T> sourceList){
        if (!CollectionUtils.isEmpty(sourceList)) {
            List<F> targetList = new ArrayList();
            try{
                for (T t : sourceList) {
                    F f = target.newInstance();
                    CacheBeanCopier.copy(t,f);
                    targetList.add(f) ;
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
            return targetList ;
        }else {
            return null ;
        }
    }

}
