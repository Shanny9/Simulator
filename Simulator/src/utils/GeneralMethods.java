package utils;

import java.util.Date;

public class GeneralMethods {
	@SuppressWarnings("deprecation")
	public static Date secToDate(int seconds){
		int hour = seconds/3600;
		seconds = seconds - hour*3600;
		int min = seconds/60;
		seconds = seconds - min*60;
		int sec = seconds;
		//System.out.println(new Date (2016,3,30,hour,min,sec));
		return new Date (2016,3,30,hour,min,sec);
	}
}
