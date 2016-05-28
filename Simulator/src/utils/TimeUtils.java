package utils;

public class TimeUtils {

	public static int convertToSimulTime(int runTime, int pauseTime, int time) {
		int numOfFullRunTimes = time / runTime;
		int fullSessionDuration = numOfFullRunTimes * (runTime + pauseTime);
		int timeInSession = pauseTime + time % runTime;

		return fullSessionDuration + timeInSession;
	}

	public static int stretch(int defaultTotRunTime, int newTotRunTime,int time) {
		double mul = (double) newTotRunTime / defaultTotRunTime;
		return (int) (time * mul);
	}
}
