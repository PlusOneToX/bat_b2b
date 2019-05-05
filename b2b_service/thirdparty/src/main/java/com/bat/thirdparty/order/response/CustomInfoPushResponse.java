package com.bat.thirdparty.order.response;

public class CustomInfoPushResponse {
        private String msg;
        private String code;
        private String userMsg;
        private String data;
        
        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserMsg() {
            return userMsg;
        }

        public void setUserMsg(String userMsg) {
            this.userMsg = userMsg;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }