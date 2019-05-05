package com.bat.thirdparty.erp.k3cloudwebapiclient;

public abstract interface ISerializerProxy {

    public abstract String Serialize(Object paramObject);

    public abstract Object Deserialize(String paramString, Class<?> paramClass);
}
