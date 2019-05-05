package com.bat.thirdparty.common.util;


import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BeanUtils {


    /**
     * 复制且覆盖
     * @param sourceObj
     * @param targetObj
     */
    public static void copyAndCover(Object sourceObj,Object targetObj){
        BeanCopier beanCopier = BeanCopier.create(sourceObj.getClass(),targetObj.getClass(),false);
        beanCopier.copy(sourceObj,targetObj,null);
    }

    public static<T> T copy(Object sourceObj,Class<T> targetClass){
        if(sourceObj ==null){
            return null;
        }
        T target = BeanFactory.INSTANCE.getBean(targetClass);
        BeanCopier beanCopier = BeanCopier.create(sourceObj.getClass(),target.getClass(),false);
        beanCopier.copy(sourceObj,target,null);
        return target;
    }

    /**
     * 列表复制
     * @param sourcelist 源列表
     * @param targetClass 目标对象Class
     * @param <T> 返回的目标对象Class列表
     * @return
     */
    public static<T> List<T> copyList(Collection sourcelist, Class<T> targetClass){
        if(sourcelist ==null || sourcelist.size()==0){
            return null;
        }
        List<T> list = new ArrayList<>();
        sourcelist.stream().forEach(sourceObj -> {
            T target = (T) BeanFactory.INSTANCE.getBean(targetClass);
            //对象复制
            BeanCopier beanCopier = BeanCopier.create(sourceObj.getClass(),target.getClass(),false);
            beanCopier.copy(sourceObj,target,null);
            list.add(target);
        });
        return list;
    }

}
