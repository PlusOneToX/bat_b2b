package com.bat.thirdparty.erp.k3cloudwebapiclient;

public class AsyncResult<T> {

    public Exception Exception;
    public T ReturnValue;
    public Boolean Successful;

    public static <T> AsyncResult<T> CreateSuccessfulResult(T result) {
        AsyncResult resultRtn = new AsyncResult();
        resultRtn.Exception = null;
        resultRtn.ReturnValue = result;
        resultRtn.Successful = Boolean.valueOf(true);
        return resultRtn;
    }

    public static <T> AsyncResult<T> CreateUnSuccessfulResult(Exception ex) {
        AsyncResult result = new AsyncResult();
        result.Exception = ex;
        result.Successful = Boolean.valueOf(false);
        return result;
    }
}
