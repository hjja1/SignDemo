package demo.hjj.com.newsigndemo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
 * Author: Administrator
 * Created Date:2015-5-16
 * Copyright @ 2015 BU
 * Description: 日期工具类
 *
 * History:
 */
public class TimeUtil {
	public static String date2String(Date date, String s) {
		SimpleDateFormat sdf=new SimpleDateFormat(s);
		return  sdf.format(date);
	}

	public static Date string2Date(String dateStr, String s) {
		try {
			SimpleDateFormat sdf=new SimpleDateFormat(s);
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();
	}


	public static int getDaysOfMonth(int year,int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		Log.e("11111111111",cal.getActualMaximum(Calendar.DAY_OF_MONTH)+"");
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

}
