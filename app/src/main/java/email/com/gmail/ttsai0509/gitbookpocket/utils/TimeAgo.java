package email.com.gmail.ttsai0509.gitbookpocket.utils;

import java.util.Date;

public enum TimeAgo {
    ;

    private static final long SECOND = 1000;
    private static final long MINUTE = 60 * SECOND;
    private static final long HOUR = 60 * MINUTE;
    private static final long DAY = 24 * HOUR;
    private static final long WEEK = 7 * DAY;
    private static final long MONTH = 30 * DAY;
    private static final long YEAR = 365 * DAY;

    private static final String[] text = new String[]{
            "%d years ago", "a year ago",
            "%d months ago", "a month ago",
            "%d weeks ago", "a week ago",
            "%d days ago", "a day ago",
            "%d minutes ago", "a minute ago",
            "%d seconds ago", "a second ago",
            "just now"
    };

    public static String format(Date then) {
        Date now = new Date();
        long ago = now.getTime() - then.getTime();
        long yearsAgo = ago / YEAR;
        if (yearsAgo > 0)
            return String.format(text[yearsAgo > 1 ? 0 : 1], yearsAgo);
        long monthsAgo = ago / MONTH;
        if (monthsAgo > 0)
            return String.format(text[monthsAgo > 1 ? 2 : 3], monthsAgo);
        long weeksAgo = ago / WEEK;
        if (weeksAgo > 0)
            return String.format(text[weeksAgo > 1 ? 4 : 5], weeksAgo);
        long hoursAgo = ago / HOUR;
        if (hoursAgo > 0)
            return String.format(text[hoursAgo > 1 ? 6 : 7], hoursAgo);
        long daysAgo = ago / DAY;
        if (daysAgo > 0)
            return String.format(text[daysAgo > 1 ? 8 : 9], daysAgo);
        long minutesAgo = ago / MINUTE;
        if (minutesAgo > 0)
            return String.format(text[minutesAgo > 1 ? 10 : 11], minutesAgo);
        long secondsAgo = ago / SECOND;
        if (secondsAgo > 0)
            return String.format(text[secondsAgo > 1 ? 12 : 13], secondsAgo);
        return text[12];
    }

}
