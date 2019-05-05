package com.bat.thirdparty.erp.k3cloudwebapiclient;

public abstract interface IAsyncActionCallBack<T> {

    public abstract void CallBack(AsyncResult<T> paramAsyncResult);
}
