package com.bat.financial.pay.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import cn.hutool.core.util.IdUtil;

/**
 * @author: lim
 * @description: 如果没有并发，订单号只在一个线程内产生，那么由于程序是顺序执行的，不同订单的生成时间一定不同，因此用时间就可以区分各个订单。
 *
 *               如果存在并发，且订单号是由一个进程中的多个线程产生的，那么只要把线程ID添加到序列号中就可以保证订单号唯一(实现)。
 *
 *               如果存在并发，且订单号是由同一台主机中的多个进程产生的，那么只要把进程ID添加到序列号中就可以保证订单号唯一(实现)。
 *
 *               如果存在并发，且订单号是由不同台主机产生的，那么MAC地址、IP地址或CPU序列号等能够区分主机的号码添加到序列号中就可以保证订单号唯一(未实现)。
 * 
 *               长度限制； wxs:string[6,32] 户系统内部订单号，只能是数字、大小写字母_-*且在同一个商户号下唯一<br/>
 *               alipay:商户订单号,64个字符以内、只能包含字母、数字、下划线；需保证在商户端不重复<br/>
 *               kuaiqian: 30以内 只允许使用字母、数字、- 、_, 并以字母或数字开头，每个商户 提交的订单号，必须在自身账户 交易中唯一
 *
 *               生成规则：自己定义的:平台标志，是否C端标志，动作标志，平台(wx|al|kq|rq)(c0|c1)(ce|rf)雪花算法id 长度26-30 超过30 截掉<br/>
 *               ps:为了照顾快钱的30长度限制没有使用UUID，UUID取消中划线有32
 * @date: 2018/5/22 11:08
 */
public class TradeNoUtils {
    private static String getId() {
        Long machineId = getprocessid() & 31;
        Long dataCenterId = getThreadId() & 31;
        return IdUtil.createSnowflake(machineId, dataCenterId).nextIdStr();
    }

    public static String getCreateTradeNo(String prefix) {
        StringBuilder sb = new StringBuilder(prefix);
        return sb.append("c0ce").append(getId()).toString();
    }

    public static String getCreateTradeNo(String prefix, Short customerFlag) {
        StringBuilder sb = new StringBuilder(prefix);
        return sb.append("c").append(customerFlag).append("ce").append(getId()).toString();
    }

    public static String getRefundTradeNo(String prefix) {
        StringBuilder sb = new StringBuilder(prefix);
        return sb.append("c0rf").append(getId()).toString();
    }

    public static String getRefundTradeNo(String prefix, Short customerFlag) {
        StringBuilder sb = new StringBuilder(prefix);
        return sb.append("c").append(customerFlag).append("rf").append(getId()).toString();
    }

    /**
     * 获取进程id
     * 
     * @return
     */
    private static Long getprocessid() {
        RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
        return Long.valueOf(mxBean.getName().split("@")[0]);
    }

    /**
     * 获取线程id
     * 
     * @return
     */
    private static Long getThreadId() {
        return Thread.currentThread().getId();
    }

}
