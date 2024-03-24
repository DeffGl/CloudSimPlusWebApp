package com.example.cloudsimpluswebapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    static public Date getDateFromString(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (dateStr != null && !dateStr.isEmpty()){
           date = dateFormat.parse(dateStr);
        }
        return date;
    }
}
