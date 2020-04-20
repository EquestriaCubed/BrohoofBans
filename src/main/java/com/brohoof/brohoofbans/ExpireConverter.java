package com.brohoof.brohoofbans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ExpireConverter {
    /**
     * Gets a timestamp from a unix time.
     *
     * @param time
     *            in seconds
     * @return
     */
    public String getTimeStamp(long time) {
        // SYSTEM TIME IN MILISECONDS
        time *= 1000L;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getDefault()); // give a timezone reference for formating
        return sdf.format(new Date(time));
    }

    /**
     * 
     * @param time
     *            time in seconds
     * @return a friendly string that says how much time remains until the param, and now
     */
    public String getFriendlyTime(long time) {
        time -= System.currentTimeMillis() / 1000;
        double days = time / (double) 60 / 60 / 24;
        double hours = (days - (int) days) * 60;
        double mins = (hours - (int) hours) * 60;
        int seconds = (int) ((mins - (int) mins) * 60);
        return (int) days + "d-" + (int) hours + "h-" + (int) mins + "m-" + seconds + "s";
    }

    public long getExpires(String filter) throws IllegalArgumentException {
        if (filter == null)
            return 0L;
        if (filter.equalsIgnoreCase("now"))
            return System.currentTimeMillis();
        final String[] groupings = filter.split("-");
        if (groupings.length == 0)
            throw new IllegalArgumentException("Invalid date specified");
        final Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(0);
        for (final String str : groupings) {
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
}
