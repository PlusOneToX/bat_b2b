package com.bat.flexible.manager.common.utils.code;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bat.flexible.api.base.common.dto.BaseSequenceQry;
import com.bat.flexible.manager.common.constant.exchange.ExchangeConstant;
import com.bat.flexible.api.FlexibleCustomException;
import com.bat.flexible.manager.error.exchange.ExchangeCardErrorCode;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NumUtils {

    /**
     * 获取小数点位数
     * @param decimal
     * @return
     */
    public static Integer getDecimalsLength(BigDecimal decimal){
        if(decimal ==null){
            return null;
        }
        //抹掉后面的00000补位
        decimal=decimal.stripTrailingZeros();
        String num = String.valueOf(decimal);
        Integer index = num.indexOf(".");
        if(index<0){
            return 0;
        }
        return num.length()-1-index;
    }

    /**
     * 获取指定长度的小数点字符串
     * @param num
     * @return
     */
    public static String getZeroByLength(int num){
        String str ="";
        for(int x=0;x<num;x++){
            str=str+"0";
        }
        return str;
    }

    /**
     * 根据父的序号获取最新的
     * @param parentSequence
     * @return
     */
    public static BaseSequenceQry getSequence(BigDecimal parentSequence){
        BaseSequenceQry sequenceQry = new BaseSequenceQry();
        sequenceQry.setParentSequence(parentSequence);
        if(parentSequence==null || parentSequence.compareTo(BigDecimal.ZERO)==0){
            return sequenceQry;
        }
        //判断现在父类是多少位小数
        Integer num = getDecimalsLength(parentSequence);
        if(num==0){
            //递增0.1
            sequenceQry.setSequenceAdd(new BigDecimal("0.1"));
        }
        if(num>0){
            String numAdd ="0."+ getZeroByLength(num)+"01";
            sequenceQry.setSequenceAdd(new BigDecimal(numAdd));
        }
        sequenceQry.setSequenceStart(sequenceQry.getSequenceAdd().add(parentSequence));
        return sequenceQry;
    }

    /**
     * 获取8到12位随机 移除 1 L I 0 O 五个
     * @return
     */
    public static String getRandomNum(){
        int  maxNum = 31;
        int i;
        int count = 0;
        char[] str = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',  'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z',  '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < 10){
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    /**
     * 获取N位数字
     * @return
     */
    public static String getRandomByLength(Integer num){

        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        for(int x=0;x<num;x++){
            pwd.append(r.nextInt(10));
        }
        return pwd.toString();
    }

    /**
     * 获取N位数字、不重复的
     * @return
     */
    public static List<Integer> getListByNumLength(Integer num){
        if(num>10 || num <1){
            throw new FlexibleCustomException("字段太长或者过短");
        }
        Long length = 1L;
        for (int x=0;x<num;x++){
            length = length*10;
        }
        List<Integer> list= new ArrayList<>(length.intValue());
        for(int x=0;x<length;x++){
            list.add(x);
        }
        return list;
    }

    /**
     * 获取N位数字、不重复的
     * @param num 数字长度
     * @param jsonArray 随机池
     * @param alreadyCount 已经生成的
     * @return
     */
    public static String getDiffereNumber(Integer num,JSONArray jsonArray,Integer alreadyCount){
        if(jsonArray==null || jsonArray.size()==0){
            throw new FlexibleCustomException("列表不能为空");
        }
        if(alreadyCount==null){
            alreadyCount=0;
        }
        Random random = new Random();
        Integer result = random.nextInt(jsonArray.size()-alreadyCount);
        String useNum = (String) jsonArray.get(result);
        //对调位置
        jsonArray.set(result,jsonArray.get(jsonArray.size()-alreadyCount-1));
        //跟后面的对调
        jsonArray.set(jsonArray.size()-alreadyCount-1,useNum);
        if(num>useNum.length()){
            Integer length = num-useNum.length();
            //0补位
            for(int x=0;x<length;x++){
                useNum = "0"+useNum;
            }
        }
        return useNum;
    }

    public static String createRandomNumber(){
        return null;
    }

    /**
     * 获取txt随机线程池
     * @param numLength 6位还是7位
     * @return
     */
    public static JSONArray getRandomPool(Integer numLength){
        if(numLength==null || numLength <6 || numLength >7){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_RANDOM_NUM_LENGTH_ONLY_SIX_SEVEN);
        }
        try {
            File file = new File("");
            String courseFile6 = file.getCanonicalPath()+"/"+ ExchangeConstant.ExchangeCodeRandomTxt+"_"+numLength+".txt";
            File txtFile6 = new File(courseFile6);
            if(!txtFile6.exists()){
                throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_RANDOM_NUMBER_TXT_NULL);
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(txtFile6));
            String temp;
            StringBuilder builder = new StringBuilder();
            while ((temp =bufferedReader.readLine())!=null){
                builder.append(temp);
            }
            bufferedReader.close();
            String arr = builder.toString();
            System.out.println("开始转换"+new Date());
            JSONArray jsonArray = JSONArray.parseArray(arr);
            System.out.println("转换JsonArray"+new Date());
    /*        List<String> list = JSON.parseArray(arr,String.class);
            System.out.println("转换list完毕"+new Date());*/
            System.out.println("长度为"+jsonArray.size());
            return jsonArray;
        } catch (IOException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_RANDOM_NUMBER_QUERY_FAIL);
        }
    }

    /**
     * 设置更新txt随机数池
     * @param numLength
     * @return
     */
    public static void updateRandomPool(Integer numLength,List<Object> list){
        if(numLength==null || numLength <6 || numLength >7 || list.size()==0){
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_CODE_RANDOM_NUM_LENGTH_ONLY_SIX_SEVEN);
        }
        try {
            File file = new File("");
            String courseFile = file.getCanonicalPath()+"/"+ ExchangeConstant.ExchangeCodeRandomTxt+"_"+numLength+".txt";
            File txtFile = new File(courseFile);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(txtFile));
            StringBuilder builder = new StringBuilder();
            for (int x=0;x<list.size();x++){
                builder.append(list.get(x)+"、");
            }
            String str = builder.toString().substring(0,builder.toString().length()-1);
            String [] arr = str.split("、");
            String ss= JSON.toJSONString(arr);
            bufferedWriter.write(ss);
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FlexibleCustomException(ExchangeCardErrorCode.EXCHANGE_CARD_RANDOM_NUMBER_UPDATE_FAIL);
        }
    }



}
