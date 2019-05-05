package com.bat.thirdparty.erp.k3cloudwebapiclient;

public class SerializerManager {

    public static ISerializerProxy Create()
            throws Exception {
        return new JsonSerializerProxy();
    }
}
