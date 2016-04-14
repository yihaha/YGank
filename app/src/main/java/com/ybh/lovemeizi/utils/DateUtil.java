package com.ybh.lovemeizi.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by y on 2016/4/14.
 */
public class DateUtil {
    /**
     * date转换成String类型
     * @param date
     * @param format
     * @return
     */
    public static String onDate2String(Date date,String ...format){
        String defaFormat="";
        if (format!=null&&format.length>0){
            defaFormat=new SimpleDateFormat(format[0]).format(date);
        }else {
            defaFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        return defaFormat;
    }
}
