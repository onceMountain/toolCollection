import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
    private static String patternYMD = "yyyy-MM-dd";
    private static String patternYMDHMS = "yyyy-MM-dd HH:mm:ss";

    private static final ThreadLocal<SimpleDateFormat> SIMPLEDATEFORMAT = new ThreadLocal<>();
    private static final Object LOCK_FLAG = new Object();

    /**
     * 本地线程获取SimpleDateFormat
     * @param pattern 日期时间格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String pattern) {
        SimpleDateFormat dateFormat = SIMPLEDATEFORMAT.get();
        if (dateFormat == null) {
            synchronized (LOCK_FLAG) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(false);
                    SIMPLEDATEFORMAT.set(dateFormat);
                }
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    public static String toStrYMD(Date date) {
        return toStrWithPattern(date, patternYMD);
    }

    public static Date parseYMD(String date) {
        return parseDateWithPattern(date, patternYMD);
    }

    public static String toStrYMDHMS(Date date) {
        return toStrWithPattern(date, patternYMDHMS);
    }

    public static Date parseYMDHMS(String date) {
        return parseDateWithPattern(date, patternYMDHMS);
    }

    public static String toStrWithPattern(Date date, String pattern) {
        if (date == null) {
            throw new RuntimeException("date to str fail,date object is null!");
        }
        try {
            SimpleDateFormat sdf = getDateFormat(pattern);
            return sdf.format(date);
        } catch (Exception e) {
            throw new RuntimeException("date to str fail!", e);
        }
    }

    public static Date parseDateWithPattern(String date, String pattern) {
        if (date == null) {
            throw new RuntimeException("parse fail,date String is null!");
        }
        try {
            SimpleDateFormat sdf = getDateFormat(pattern);
            return sdf.parse(date);
        } catch (Exception e) {
            throw new RuntimeException("parse fail!", e);
        }
    }

    public static long dayLong = 1000 * 60 * 60 * 24;
    public static long hourLong = 1000 * 60 * 60;
    public static long minuteLong = 1000 * 60;

    public static Date changeDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            throw new RuntimeException("changeDate fail,date is null!");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, year);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.DATE, day);
        calendar.add(Calendar.HOUR, hour);
        calendar.add(Calendar.MINUTE, minute);
        calendar.add(Calendar.SECOND, second);
        return calendar.getTime();
    }

    public static String changeDate(String date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            throw new RuntimeException("changeDate fail,date is null!");
        }
        return toStrYMDHMS(changeDate(parseYMDHMS(date), year, month, day, hour, minute, second));
    }

    public static Date changeDateWithDay(Date date, int day) {
        return changeDate(date, 0, 0, day, 0, 0, 0);
    }

    public static String changeDateWithDay(String date, int day) {
        return changeDate(date, 0, 0, day, 0, 0, 0);
    }

    public static Date changeDateToDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static boolean compareDate(String dateStr1, String dateStr2) throws ParseException {
        if (dateStr1 == null || dateStr2 == null) {
            throw new RuntimeException("compareDate fail,dateStr is null!");
        }
        DateFormat df = getDateFormat(patternYMDHMS);
        Date d1 = df.parse(dateStr1);
        Date d2 = df.parse(dateStr2);
        return d2.getTime() > d1.getTime();
    }

    public static boolean compareDate(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new RuntimeException("compareDate fail,date is null!");
        }
        return date2.getTime() > date1.getTime();
    }
}
