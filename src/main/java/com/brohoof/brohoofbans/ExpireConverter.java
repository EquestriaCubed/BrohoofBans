package com.brohoof.brohoofbans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ExpireConverter {

    private static final long MILLIS_IN_A_DAY = 86400000L;
    private static final long MILLIS_IN_A_HOUR = 3600000L;
    private static final long MILLIS_IN_A_MINUTE = 60000L;

    public static long getExpires(String filter) throws IllegalArgumentException {
        if (filter == null)
            return 0L;
        if (filter.equalsIgnoreCase("now"))
            return System.currentTimeMillis();
        String[] groupings = filter.split("-");
        if (groupings.length == 0)
            throw new IllegalArgumentException("Invalid date specified");
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(0);
        for (String str : groupings) {
            int type;
            switch (str.charAt(str.length() - 1)) {
                case 'm':
                    type = Calendar.MINUTE;
                    break;
                case 'h':
                    type = Calendar.HOUR;
                    break;
                case 'd':
                    type = Calendar.DATE;
                    break;
                case 'w':
                    type = Calendar.WEEK_OF_YEAR;
                    break;
                case 'y':
                    type = Calendar.YEAR;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown date value specified");
            }
            cal.add(type, Integer.valueOf(str.substring(0, str.length() - 1)));
        }
        return cal.getTimeInMillis();
    }

    /**
     *
     * @param diff time in miliseconds
     * @return a friendly string that says how much time remains until the param,
     *         and now
     */
    public static String getFriendlyTime(long diff) {
        int days = (int) (diff / MILLIS_IN_A_DAY);
        diff -= days * MILLIS_IN_A_DAY;
        int hours = (int) (diff / MILLIS_IN_A_HOUR);
        diff -= hours * MILLIS_IN_A_HOUR;
        int mins = (int) (diff / MILLIS_IN_A_MINUTE);
        diff -= mins * MILLIS_IN_A_MINUTE;
        int seconds = (int) (diff / 1000);
        return days + "d-" + hours + "h-" + mins + "m-" + seconds + "s";
    }

    /**
     * Gets a timestamp from a unix time.
     *
     * @param seconds in seconds
     * @return
     */
    public static String getTimeStamp(long miliseconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating
        return sdf.format(new Date(miliseconds));
    }
}
