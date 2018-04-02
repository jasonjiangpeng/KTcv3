package opencv.zhongke.ktcv.utils;

import java.util.List;

import opencv.zhongke.ktcv.objectmiss.NConstants;

/**
 * Created by LostboyJason on 2018/3/26.
 */

public class NString {
    public static String  listToString(List<Integer> integers){
        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0; i <integers.size() ; i++) {
            stringBuilder.append(i+"值"+integers.get(i)+";");
        }
        return stringBuilder.toString();
    }
    public static String  listToStringObjectMiss(List<Integer> integers){
        StringBuilder stringBuilder=new StringBuilder();
        for (int i = 0; i <integers.size() ; i++) {
            if (integers.get(i)== NConstants.MOBJECTMISS)
            stringBuilder.append("丢失"+(i+1)+"产品");
        }
        return stringBuilder.toString();
    }
    public static String  short2String(short[]  shorts){
        StringBuilder stringBuilder=new StringBuilder();
        int  size=shorts.length;
        for (int i = 0; i <size ; i++) {
          stringBuilder.append(shorts[i]);
        }
        return stringBuilder.toString();
    }


}
