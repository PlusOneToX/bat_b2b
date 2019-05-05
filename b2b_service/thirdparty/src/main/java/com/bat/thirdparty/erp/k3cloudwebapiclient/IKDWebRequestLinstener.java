package com.bat.thirdparty.erp.k3cloudwebapiclient;

public abstract interface IKDWebRequestLinstener<T> {

    public abstract void onRequsetSuccess(ApiRequest<T> paramApiRequest);

    public abstract void onRequsetFailed(ApiRequest<T> paramApiRequest);

    public abstract void onRequsetError(ApiRequest<T> paramApiRequest, Exception paramException);
}
