package com.example.cloudsimpluswebapp.utils;

import org.springframework.stereotype.Component;

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


   /* static public LocalDate getDateFromString(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = null;
        if (dateStr != null && !dateStr.isEmpty()) {
            date = LocalDate.parse(dateStr, formatter);
        }
        return date;
    }*/

    /*static public Date getDateFromString(String dateStr)  {;
        Date date = null;
        if (dateStr != null && !dateStr.isEmpty()) {
            date = Date.valueOf(dateStr);
        }
        return date;
    }*/
}
