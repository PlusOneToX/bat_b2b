package com.bat.warehouse.manager.common.utils;


import com.bat.warehouse.api.base.common.exception.WarehouseException;

public class CronUtils {


    /**
     * 根据分钟获取cron表达式
     * @param time
     * @return
     */
    public static String getCronByMin(Integer time){
        String cron = "0";
        Integer seconds =0;
        Integer minutes=0;

        Integer hours=0;
        Integer days=0;
        Integer months =0;

        //先判断日
        days = time/60/24;
        if(days>=30){
            throw new WarehouseException("暂不支持超大时间间隔定时器");
        }
        hours =  (time-days*24*60)/60;
        //分钟
        minutes = time-days*60*24-hours*60;
        if(minutes>0){
            cron=cron+" 0/"+minutes;
        }else {
            cron =cron+" 0";
        }
        if(hours>0){
            cron=cron+" 0/"+hours;
        }else {
            cron =cron+" 0";
        }
        if(days>0){
            cron=cron+" 0/"+days;
        }else {
            cron =cron+" 0";
        }

        return cron+" * * ? ";
    }

    public static void main(String[] args) {
        String cronByMin = getCronByMin(130);
        System.out.println(cronByMin);
    }
}
