package com.bat.goods.api.utils;

public enum BeanFactory {

    INSTANCE;


    public <T> T getBean(Class<T> classType){
        if( classType==null){
            return null;
        }
        String className = classType.getCanonicalName();
        try {
            Class<T> clazz =(Class<T>) Class.forName(className);
            Object obj = clazz.newInstance();
            //需要检测checkType是不是obj的字节码对象
            if(!classType.isInstance(obj)){
                throw new RuntimeException("字节码和对象不兼容");
            }
            return (T)obj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
