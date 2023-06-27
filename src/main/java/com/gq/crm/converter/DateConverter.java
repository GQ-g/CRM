package com.gq.crm.converter;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther: 胖叔讲java
 * @Date: 2022/10/18 - 10 - 18 - 10:23
 * @Decsription: com.study.crm.converter
 * @version: 1.0
 */

public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String s) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
