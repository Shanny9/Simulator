package utils;

import java.util.Date;

public class GeneralMethods {
	
	@SuppressWarnings("deprecation")
	public static Date secToDate(int seconds) {
		int hour = (seconds / (60 * 60)) % 24;
		int min = (seconds / 60) % 60;
		int sec = seconds % 60;
		return new Date(1970, 0, 1, hour, min, sec);
	}
}
