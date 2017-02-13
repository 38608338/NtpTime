package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	public static final String DEFAULT_DATE_PATTERN="yyyy-MM-dd";
	public static final String DEFAULT_TIME_PATTERN="HH:mm:ss";
	public static final String DEFAULT_DATETIME_PATTERN="yyyy-MM-dd HH:mm:ss";
	
	public static void main(String[] args) {
		System.out.println(DEFAULT_DATETIME_PATTERN);
		
		SimpleDateFormat sdf=new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
		System.out.println(sdf.format(new Date()));
	}
}
